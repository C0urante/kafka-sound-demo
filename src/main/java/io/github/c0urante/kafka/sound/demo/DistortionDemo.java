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
import io.github.c0urante.kafka.sound.demo.pedal.RepeatingBluetoothPedal;
import io.github.c0urante.kafka.sound.demo.streams.StreamsApplication;
import io.github.c0urante.kafka.sound.demo.ui.TextWindow;
import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistortionDemo {

    private static final Logger log = LoggerFactory.getLogger(PitchDetectionDemo.class);

    public static void main(String[] args) {
        Distortion distortion = new Distortion();
        DistortionWindow distortionWindow = new DistortionWindow();
        distortionWindow.setAlgorithm(distortion.currentAlgorithm());
        createKeyBindings(distortion, distortionWindow);
        new Streams(args, distortion).start();
    }

    private static void createKeyBindings(Distortion distortion, DistortionWindow distortionWindow) {
        new RepeatingBluetoothPedal(110, 1000) {
            private boolean amplificationIncreasing = true;
            private static final double AMPLIFICATION_SCALE_FACTOR = 1.1;
            private static final int AMPLIFICATION_DELAY = AudioFormats.SAMPLE_RATE / 10;

            @Override
            protected synchronized void left() {
                String adjustment;
                double newFactor;
                if (amplificationIncreasing) {
                    newFactor = distortion.amplificationFactor()
                            .adjustDelayed(d -> d * AMPLIFICATION_SCALE_FACTOR, AMPLIFICATION_DELAY);
                    adjustment = "Increasing";
                } else {
                    newFactor = distortion.amplificationFactor()
                            .adjustDelayed(d -> d / AMPLIFICATION_SCALE_FACTOR, AMPLIFICATION_DELAY);
                    adjustment = "Decreasing";
                }
                log.info("{} distortion factor to {}", adjustment, newFactor);
                distortionWindow.setFactor(newFactor);
            }

            @Override
            protected synchronized void leftUp() {
                super.leftUp();
                amplificationIncreasing = !amplificationIncreasing;
            }

            @Override
            protected void right() {
                String newAlgorithm = distortion.nextAlgorithm();
                log.info("Switched clipping algorithm to {}", newAlgorithm);
                distortionWindow.setAlgorithm(newAlgorithm);
            }
        }.start(distortionWindow.frame());
    }

    private static class DistortionWindow extends TextWindow {

        private volatile double factor;
        private volatile String algorithm;

        public DistortionWindow() {
            super("Audio distortion à la Kafka");
            this.factor = 1;
            this.algorithm = "";
            updateText();
        }

        public void setFactor(double factor) {
            this.factor = factor;
            updateText();
        }

        public void setAlgorithm(String algorithmName) {
            this.algorithm = algorithmName;
            updateText();
        }

        private void updateText() {
            String text = String.format("%nFactor: %6.2f%n%nAlgorithm: %s", this.factor, this.algorithm);
            updateText(text);
        }
    }

    private static class Streams extends StreamsApplication {
        private static final String APPLICATION_ID = "kafka-sound-demo-distortion";

        private final Distortion distortion;

        public Streams(String[] args, Distortion distortion) {
            super(APPLICATION_ID, args);
            this.distortion = distortion;
        }

        @Override
        protected void defineTopology(KStream<byte[], byte[]> encodedSamplesStream) {
            KStream<byte[], Short> processedSamples = samples(encodedSamplesStream)
                    .mapValues(distortion);

            publishProcessedSamples(processedSamples);
        }
    }

}
