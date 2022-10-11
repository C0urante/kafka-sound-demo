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

import io.github.c0urante.kafka.sound.demo.pedal.SimpleBluetoothPedal;
import io.github.c0urante.kafka.sound.demo.pitch.PitchDetector;
import io.github.c0urante.kafka.sound.demo.streams.StreamsApplication;
import io.github.c0urante.kafka.sound.demo.ui.TextWindow;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class PitchDetectionDemo {

    private static final Logger log = LoggerFactory.getLogger(PitchDetectionDemo.class);

    public static void main(String[] args) {
        PitchDetector pitchDetector = new PitchDetector();
        PitchWindow pitchWindow = new PitchWindow(pitchDetector.currentAlgorithm());
        createkeyBindings(pitchDetector, pitchWindow);
        new Streams(args, pitchDetector, pitchWindow).start();
    }

    private static void createkeyBindings(PitchDetector pitchDetector, PitchWindow pitchWindow) {
        new SimpleBluetoothPedal() {
            @Override
            protected void left() {
                newAlgorithm(pitchDetector.prevAlgorithm());
            }

            @Override
            protected void right() {
                newAlgorithm(pitchDetector.nextAlgorithm());
            }

            private void newAlgorithm(String algorithm) {
                log.info("Switched pitch detection algorithm to {}", algorithm);
                pitchWindow.setAlgorithm(algorithm);
                pitchWindow.setPitch("");
            }
        }.start(pitchWindow.frame());
    }

    private static class PitchWindow extends TextWindow {

        private volatile String pitch;
        private volatile String algorithm;

        public PitchWindow(String initialAlgorithm) {
            super("Pitch detection à la Kafka");
            this.pitch = "";
            this.algorithm = initialAlgorithm;
            updateText();
        }

        public void setPitch(String pitchName) {
            this.pitch = Objects.requireNonNull(pitchName);
            updateText();
        }

        public void setAlgorithm(String algorithmName) {
            this.algorithm = Objects.requireNonNull(algorithmName);
            updateText();
        }

        private void updateText() {
            String text = String.format("%nPitch: %-2s%n%nAlgorithm: %s", this.pitch, this.algorithm);
            updateText(text);
        }
    }

    private static class Streams extends StreamsApplication {
        private static final String APPLICATION_ID = "kafka-sound-demo-pitch-detection";

        private final PitchDetector pitchDetector;
        private final PitchWindow pitchWindow;

        public Streams(String[] args, PitchDetector pitchDetector, PitchWindow pitchWindow) {
            super(APPLICATION_ID, args);
            this.pitchDetector = pitchDetector;
            this.pitchWindow = pitchWindow;
        }

        @Override
        protected void defineTopology(KStream<byte[], byte[]> rawInputStream) {
            rawInputStream.flatMapValues(pitchDetector)
                    .foreach((k, v) -> pitchWindow.setPitch(v != null ? v : ""));
        }
    }

}
