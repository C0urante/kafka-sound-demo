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

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.kafka.common.config.ConfigDef.Range;
import static org.apache.kafka.common.config.ConfigDef.Type;

class ApplicationConfig extends AbstractConfig {

    public static final String INPUT_TOPIC_CONFIG = "input.topic";
    public static final String INPUT_TOPIC_DOC =
            "Topic to read raw audio from";

    public static final String OUTPUT_TOPIC_CONFIG = "output.topic";
    public static final String OUTPUT_TOPIC_DOC =
            "Topic to write processed audio to (if the application produces output)";

    public static final String OUTPUT_VALUE_SIZE_CONFIG = "output.value.size";
    public static final int OUTPUT_VALUE_SIZE_DEFAULT = 1024;
    public static final String OUTPUT_VALUE_SIZE_DOC =
            "Size of values (in bytes) for records written to the output topic";

    public static ConfigDef configDef() {
        return new ConfigDef()
                .define(
                        INPUT_TOPIC_CONFIG,
                        Type.STRING,
                        ConfigDef.Importance.HIGH,
                        INPUT_TOPIC_DOC
                ).define(
                        OUTPUT_TOPIC_CONFIG,
                        Type.STRING,
                        ConfigDef.Importance.HIGH,
                        OUTPUT_TOPIC_DOC
                ).define(
                        OUTPUT_VALUE_SIZE_CONFIG,
                        Type.INT,
                        OUTPUT_VALUE_SIZE_DEFAULT,
                        Range.atLeast(1),
                        ConfigDef.Importance.LOW,
                        OUTPUT_VALUE_SIZE_DOC
                );
    }

    private final String inputTopic;
    private final String outputTopic;
    private final int outputValueSize;

    public ApplicationConfig(Properties props) {
        this(new HashMap<>(props));
    }

    public ApplicationConfig(Map<?, ?> props) {
        super(configDef(), props);

        this.inputTopic = getString(INPUT_TOPIC_CONFIG);
        this.outputTopic = getString(OUTPUT_TOPIC_CONFIG);
        this.outputValueSize = getInt(OUTPUT_VALUE_SIZE_CONFIG);
    }

    public String inputTopic() {
        return inputTopic;
    }

    public String outputTopic() {
        return outputTopic;
    }

    public int outputValueSize() {
        return outputValueSize;
    }

}
