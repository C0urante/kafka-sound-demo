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
package io.github.c0urante.kafka.sound.demo.filter;

import io.github.c0urante.kafka.sound.demo.AudioTestUtils;
import io.github.c0urante.kafka.sound.demo.InteractiveTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InteractiveTest.class)
public class FilterTest {

    @Test
    public void testLowPassSpTarsos_64_440() {
        testLowPassSpTarsos(64, 440);
    }

    @Test
    public void testLowPassSpTarsos_256_440() {
        testLowPassSpTarsos(256, 440);
    }

    @Test
    public void testLowPassSpTarsos_1024_440() {
        testLowPassSpTarsos(1024, 440);
    }

    @Test
    public void testLowPassSpTarsos_1024_880() {
        testLowPassSpTarsos(1024, 880);
    }

    @Test
    public void testLowPassSpTarsos_1024_1760() {
        testLowPassSpTarsos(1024, 1760);
    }

    @Test
    public void testLowPassSpTarsos_1024_3520() {
        testLowPassSpTarsos(1024, 3520);
    }

    @Test
    public void testLowPassSpTarsos_64_7040() {
        testLowPassSpTarsos(64, 7040);
    }

    @Test
    public void testLowPassSpTarsos_128_7040() {
        testLowPassSpTarsos(128, 7040);
    }

    @Test
    public void testLowPassSpTarsos_256_7040() {
        testLowPassSpTarsos(256, 7040);
    }

    @Test
    public void testLowPassSpTarsos_512_7040() {
        testLowPassSpTarsos(512, 7040);
    }

    @Test
    public void testLowPassSpTarsos_1024_7040() {
        testLowPassSpTarsos(1024, 7040);
    }

    @Test
    public void testLowPassSpTarsos_1024_14080() {
        testLowPassSpTarsos(1024, 14080);
    }

    @Test
    public void testLowPassSpTarsos_1024_28160() {
        testLowPassSpTarsos(1024, 28160);
    }

    private void testLowPassSpTarsos(int windowSize, float cutoff) {
        testFilterAlgorithm(new LowPassSingleStage(windowSize, cutoff));
    }

    private void testFilterAlgorithm(FilterAlgorithm algorithm) {
        AudioTestUtils.testAlgorithmOnFile(AudioTestUtils.BACH_FILE, algorithm::filter);
    }

}
