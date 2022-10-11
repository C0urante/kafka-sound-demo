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
package io.github.c0urante.kafka.sound.demo;

import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Populate {

    private static final Logger log = LoggerFactory.getLogger(Populate.class);
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final int SAMPLE_RATE = 44100;
    // Sample size in bytes
    private static final int SAMPLE_SIZE = AudioFormats.SAMPLE_SIZE / Byte.SIZE;
    private static final double TRAILING_SILENCE_SECONDS = 3;

    public static void main(String[] args) throws Exception {
        if (args.length != 1 && args.length != 2) {
            throw new RuntimeException("Usage: Populate <file> [<message_size=1024>]");
        }
        String file = args[0];
        int messageSize = args.length > 1 ? Integer.parseInt(args[1]) : 1024;
        if (messageSize < 1)
            throw new RuntimeException("Message size must be at least 1");
        try (Producer<byte[], byte[]> producer = createProducer()) {
            produceFile(producer, file, messageSize);
        }
    }

    private static Producer<byte[], byte[]> createProducer() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.ACKS_CONFIG, "0");
        producerProps.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        return new KafkaProducer<>(producerProps);
    }

    private static void produceFile(Producer<byte[], byte[]> producer, String file, int messageSize) throws Exception {
        byte[] rawSamples = Files.readAllBytes(Paths.get(file));
        produceWithThrottling(producer, rawSamples, messageSize, "from file");
        int silenceBytes = (int) (TRAILING_SILENCE_SECONDS * SAMPLE_RATE * SAMPLE_SIZE);
        // Force the byte array size to be evenly divisible by the sample size
        byte[] silence = new byte[(silenceBytes / SAMPLE_SIZE) * SAMPLE_SIZE];
        produceWithThrottling(producer, silence, messageSize, "of silence");
    }

    private static void produceWithThrottling(
            Producer<byte[], byte[]> producer,
            byte[] rawSamples,
            int messageSize,
            String source
    ) throws Exception {
        int totalTime = bytesToMs(rawSamples.length);
        log.info("Taking {}ms to produce {} bytes {}", totalTime, rawSamples.length, source);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < rawSamples.length; i += messageSize) {
            double progress = i * 1.0 / rawSamples.length;
            long earliestPermittedTime = startTime + ((long) (totalTime * progress));
            long waitTime = earliestPermittedTime - System.currentTimeMillis();
            if (waitTime > 1) {
                // Only sleep if we're more than a millisecond ahead
                Thread.sleep(waitTime);
            }

            int bytesRemaining = Math.min(rawSamples.length - i, messageSize);
            byte[] value = new byte[bytesRemaining];
            System.arraycopy(rawSamples, i, value, 0, value.length);
            ProducerRecord<byte[], byte[]> record =
                    new ProducerRecord<>("sound-raw-input", value);
            producer.send(record);
        }
    }

    private static int bytesToMs(int bytes) {
        // Important: Do not multiple bytes by 1000 directly, it can lead to overflow
        return (int) (1000 * (bytes * 1.0 / (SAMPLE_RATE * SAMPLE_SIZE)));
    }

}
