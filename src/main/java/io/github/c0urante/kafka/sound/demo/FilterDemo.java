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

import io.github.c0urante.kafka.sound.demo.filter.Filter;
import io.github.c0urante.kafka.sound.demo.pedal.BluetoothPedal;
import io.github.c0urante.kafka.sound.demo.streams.StreamsApplication;
import io.github.c0urante.kafka.sound.demo.ui.TextWindow;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterDemo {

    private static final Logger log = LoggerFactory.getLogger(PitchDetectionDemo.class);

    public static void main(String[] args) {
        Filter filter = new Filter();
        FilterWindow filterWindow = new FilterWindow(filter.min(), filter.max(), filter.currentAlgorithm());
        createKeyBindings(filter, filterWindow);
        new Streams(args, filter).start();
    }

    private static void createKeyBindings(Filter filter, FilterWindow filterWindow) {
        new BluetoothPedal() {
            private static final double CUTOFF_SCALE_FACTOR = 1.1;
            private static final float MIN_CUTOFF = 0.1f;
            private static final float MAX_CUTOFF = 30_000;
            private boolean leftPressed = false;
            private boolean rightPressed = false;
            private boolean bothPressed = false;
            private boolean minIncreasing = true;
            private boolean maxIncreasing = false;
            private boolean minChanged = false;
            private boolean maxChanged = false;
            private float min = MIN_CUTOFF;
            private float max = MAX_CUTOFF;

            @Override
            protected synchronized void leftDown() {
                if (!leftPressed) {
                    leftPressed = true;
                    if (rightPressed) {
                        nextAlgorithm();
                        bothPressed = true;
                    }
                    return;
                } else if (rightPressed) {
                    return;
                }

                newMin();
                minChanged = true;
            }

            @Override
            protected synchronized void leftUp() {
                leftPressed = false;
                if (!bothPressed) {
                    if (!minChanged)
                        newMin();

                    String adjustment = minIncreasing ? "Increased" : "Decreased";
                    filter.min(min);
                    log.info("{} minimum frequency to {}", adjustment, min);

                    if (!minChanged)
                        return;
                } else if (!rightPressed) {
                    bothPressed = false;
                }

                minChanged = false;
                minIncreasing = !minIncreasing;
            }

            private void newMin() {
                if (minIncreasing) {
                    min = (float) Math.min(min * CUTOFF_SCALE_FACTOR, max);
                    min = Math.min(min, max);
                } else {
                    min = (float) (min / CUTOFF_SCALE_FACTOR);
                    min = Math.max(min, MIN_CUTOFF);
                }
                filterWindow.setMin(min);
            }

            @Override
            protected void rightDown() {
                if (!rightPressed) {
                    rightPressed = true;
                    if (leftPressed) {
                        nextAlgorithm();
                    }
                    return;
                } else if (leftPressed) {
                    return;
                }

                newMax();
                maxChanged = true;
            }

            @Override
            protected synchronized void rightUp() {
                rightPressed = false;
                if (!bothPressed) {
                    if (!maxChanged)
                        newMax();

                    String adjustment = maxIncreasing ? "Increased" : "Decreased";
                    filter.max(max);
                    log.info("{} maximum frequency to {}", adjustment, min);

                    if (!maxChanged)
                        return;
                } else if (!leftPressed) {
                    bothPressed = false;
                }
                maxChanged = false;
                maxIncreasing = !maxIncreasing;
            }

            private void newMax() {
                if (maxIncreasing) {
                    max = (float) Math.max(max * CUTOFF_SCALE_FACTOR, min);
                    max = Math.min(max, MAX_CUTOFF);
                } else {
                    max = (float) (max / CUTOFF_SCALE_FACTOR);
                    max = Math.max(max, min);
                }
                filterWindow.setMax(max);
            }

            private void nextAlgorithm() {
                String algorithm = filter.nextAlgorithm();
                filterWindow.setAlgorithm(algorithm);
                log.info("Switched algorithm to {}", algorithm);
            }
        }.start(filterWindow.frame());
    }

    private static class FilterWindow extends TextWindow {

        private volatile float min;
        private volatile float max;
        private volatile String algorithm;

        public FilterWindow(float initialMin, float initialMax, String initialAlgorithm) {
            super("Audio filtering à la Kafka");
            this.min = initialMin;
            this.max = initialMax;
            this.algorithm = initialAlgorithm;
            updateText();
        }

        public void setMin(float min) {
            this.min = min;
            updateText();
        }

        public void setMax(float max) {
            this.max = max;
            updateText();
        }

        public void setAlgorithm(String algorithmName) {
            this.algorithm = algorithmName;
            updateText();
        }

        private void updateText() {
            String text = String.format(
                    "%nMin: %8.2f%nMax: %8.2f%nAlgorithm: %s",
                    this.min, this.max, this.algorithm
            );
            updateText(text);
        }
    }

    private static class Streams extends StreamsApplication {
        private static final String APPLICATION_ID = "kafka-sound-demo-distortion";

        private final Filter filter;

        public Streams(String[] args, Filter filter) {
            super(APPLICATION_ID, args);
            this.filter = filter;
        }

        @Override
        protected void defineTopology(KStream<byte[], byte[]> encodedSamplesStream) {
            KStream<byte[], Short> processedSamples = encodedSamplesStream
                    .mapValues(SampleUtils::decodeSamples)
                    .mapValues(filter)
                    .flatMapValues(samples -> samples);

            publishProcessedSamples(processedSamples);
        }
    }

}
