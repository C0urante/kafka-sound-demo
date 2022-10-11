/*
 * kafka-sound-demo - Audio-based demonstration of Kafka, Kafka Connect, and Kafka Streams
 * Copyright © 2023 Chris Egerton (fearthecellos@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.c0urante.kafka.sound.demo.streams;

import io.github.c0urante.kafka.sound.demo.util.CliUtils;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse;

public abstract class StreamsApplication {

    private static final Logger log = LoggerFactory.getLogger(StreamsApplication.class);

    private final String applicationId;
    private final ApplicationConfig appConfig;
    private final StreamsConfig streamsConfig;

    protected StreamsApplication(String applicationId, String[] args) {
        this.applicationId = applicationId;

        Properties userProps = CliUtils.readProps(args);
        this.appConfig = new ApplicationConfig(userProps);

        Properties streamsProps = new Properties();
        streamsProps.putAll(userProps);
        baseStreamsConfig().forEach(streamsProps::putIfAbsent);
        this.streamsConfig = new StreamsConfig(streamsProps);
    }

    public void start() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<byte[], byte[]> encodedSamplesStream = builder.stream(appConfig.inputTopic());
        defineTopology(encodedSamplesStream);

        KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfig);
        streams.setUncaughtExceptionHandler(StreamsApplication::handleError);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Stopping Kafka Streams application");
            streams.close();
        }));

        log.info("Starting Kafka Streams application");
        streams.start();
    }

    protected abstract void defineTopology(KStream<byte[], byte[]> encodedSamplesStream);

    private Map<String, Object> baseStreamsConfig() {
        Map<String, Object> result = new HashMap<>();

        result.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        result.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArraySerde.class);
        result.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArraySerde.class);

        return result;
    }

    private static StreamThreadExceptionResponse handleError(Throwable exception) {
        log.error("Streams task encountered unexpected error", exception);
        return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
    }

    protected <K> KStream<K, Short> samples(KStream<K, byte[]> encodedSamplesStream) {
        return encodedSamplesStream
                .flatMapValues(SampleUtils::decodeSamples);
    }

    protected void publishProcessedSamples(KStream<?, Short> samplesStream) {
        KStream<?, byte[]> encodedSamplesStream = samplesStream
                .mapValues(new Grouping<>(appConfig.outputValueSize()))
                .mapValues(SampleUtils::encodeSamples);
        encodedSamplesStream.to(appConfig.outputTopic());
    }

}
