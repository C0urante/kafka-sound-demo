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

import io.github.c0urante.kafka.sound.demo.loop.Loop;
import io.github.c0urante.kafka.sound.demo.pedal.SimpleBluetoothPedal;
import io.github.c0urante.kafka.sound.demo.streams.StreamsApplication;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoopPedalDemo {

    private static final Logger log = LoggerFactory.getLogger(LoopPedalDemo.class);

    public static void main(String[] args) {
        Loop loop = new Loop(0.8);
        createkeyBindings(loop);
        new Streams(args, loop).start();
    }

    private static void createkeyBindings(Loop loop) {
        new SimpleBluetoothPedal() {
            @Override
            protected void left() {
                loop.loop();
            }

            @Override
            protected void right() {
                loop.clear();
            }
        }.start();
    }

    private static class Streams extends StreamsApplication {
        private static final String APPLICATION_ID = "kafka-sound-demo-loop-pedal";

        private final Loop loop;

        public Streams(String[] args, Loop loop) {
            super(APPLICATION_ID, args);
            this.loop = loop;
        }

        @Override
        protected void defineTopology(KStream<byte[], byte[]> encodedSamplesStream) {
            KStream<byte[], Short> processedSamples = samples(encodedSamplesStream)
                    .mapValues(loop);

            publishProcessedSamples(processedSamples);;
        }
    }

}
