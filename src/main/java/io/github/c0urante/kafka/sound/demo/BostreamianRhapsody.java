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

import io.github.c0urante.kafka.sound.demo.distortion.Distortion;
import io.github.c0urante.kafka.sound.demo.distortion.HyperbolicTangent;
import io.github.c0urante.kafka.sound.demo.pedal.SimpleBluetoothPedal;
import io.github.c0urante.kafka.sound.demo.reverb.Reverb;
import io.github.c0urante.kafka.sound.demo.streams.StreamsApplication;
import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BostreamianRhapsody {

    private static final Logger log = LoggerFactory.getLogger(BostreamianRhapsody.class);

    public static void main(String[] args) {
        Distortion distortion = new Distortion(new HyperbolicTangent());
        Reverb reverb = new Reverb(0.85);
        createkeyBindings(distortion);
        new Streams(args, distortion, reverb).start();
    }

    private static void createkeyBindings(Distortion distortion) {
        new SimpleBluetoothPedal() {
            private boolean distortionEnabled = false;
            private static final double AMPLIFICATION_FACTOR = 15;
            private static final int AMPLIFICATION_TOGGLE_DELAY = AudioFormats.SAMPLE_RATE / 2;

            @Override
            protected synchronized void left() {
                String adjustment;
                if (distortionEnabled) {
                    distortion.amplificationFactor().setDelayed(1, AMPLIFICATION_TOGGLE_DELAY);
                    adjustment = "Disabling";
                } else {
                    distortion.amplificationFactor().setDelayed(AMPLIFICATION_FACTOR, AMPLIFICATION_TOGGLE_DELAY);
                    adjustment = "Enabling";
                }
                distortionEnabled = !distortionEnabled;
                log.info("{} distortion", adjustment);
            }

            @Override
            protected synchronized void right() {
                // No-op
            }
        }.start();
    }

    private static class Streams extends StreamsApplication {
        private static final String APPLICATION_ID = "kafka-sound-demo-bostreamian-rhapsody";

        private final Distortion distortion;
        private final Reverb reverb;

        public Streams(String[] args, Distortion distortion, Reverb reverb) {
            super(APPLICATION_ID, args);
            this.distortion = distortion;
            this.reverb = reverb;
        }

        @Override
        protected void defineTopology(KStream<byte[], byte[]> encodedSamplesStream) {
            KStream<byte[], Short> processedSamples = samples(encodedSamplesStream)
                    .mapValues(distortion)
                    .mapValues(reverb);

            publishProcessedSamples(processedSamples);
        }
    }

}
