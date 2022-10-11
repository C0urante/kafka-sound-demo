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
package io.github.c0urante.kafka.sound.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class CliUtils {

    public static Properties readProps(String[] args) {
        return readProps(propsFile(args));
    }

    private static String propsFile(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("Usage: streams <config_file>");
        }
        return args[0];
    }

    private static Properties readProps(String filename) {
        Properties config = new Properties();
        try (InputStream configFileStream = Files.newInputStream(Paths.get(filename))) {
            config.load(configFileStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file", e);
        }
        return config;
    }

}
