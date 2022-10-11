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

import io.github.c0urante.kafka.sound.demo.pedal.RepeatingBluetoothPedal;
import io.github.c0urante.kafka.sound.demo.reverb.Reverb;
import io.github.c0urante.kafka.sound.demo.streams.StreamsApplication;
import io.github.c0urante.kafka.sound.demo.ui.TextWindow;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverbDemo {

    private static final Logger log = LoggerFactory.getLogger(PitchDetectionDemo.class);

    public static void main(String[] args) {
        double initialDecay = 0.90;
        Reverb reverb = new Reverb(
                initialDecay,
                23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
        );
        ReverbWindow reverbWindow = new ReverbWindow(initialDecay);
        createKeyBindings(reverb, reverbWindow);
        new Streams(args, reverb).start();
    }

    private static void createKeyBindings(Reverb reverb, ReverbWindow reverbWindow) {
        new RepeatingBluetoothPedal(100) {
            private static final double DECAY_DELTA = 0.01;

            @Override
            protected synchronized void left() {
                newDecay("Increased", reverb.decay().adjustNow(d -> d * DECAY_DELTA));
            }

            @Override
            protected void right() {
                newDecay("Decreased", reverb.decay().adjustNow(d -> d * DECAY_DELTA));
            }

            private void newDecay(String adjustment, double newDecay) {
                reverbWindow.setDecay(newDecay);
                log.info("{} decay to {}", adjustment, newDecay);
            }

        }.start(reverbWindow.frame());
    }

    private static class ReverbWindow extends TextWindow {
        private volatile double decay;

        public ReverbWindow(double initialDecay) {
            super("Artificial reverb à la Kafka");
            this.decay = initialDecay;
            updateText();
        }

        public void setDecay(double decay) {
            this.decay = decay;
            updateText();
        }

        private void updateText() {
            String text = String.format("%n%nDecay factor: %1.3f%n", this.decay);
            updateText(text);
        }
    }

    private static class Streams extends StreamsApplication {
        private static final String APPLICATION_ID = "kafka-sound-demo-reverb";

        private final Reverb reverb;

        public Streams(String[] args, Reverb reverb) {
            super(APPLICATION_ID, args);
            this.reverb = reverb;
        }

        @Override
        protected void defineTopology(KStream<byte[], byte[]> encodedSamplesStream) {
            KStream<byte[], Short> processedSamples = samples(encodedSamplesStream)
                    .mapValues(reverb);

            publishProcessedSamples(processedSamples);
        }
    }

}
