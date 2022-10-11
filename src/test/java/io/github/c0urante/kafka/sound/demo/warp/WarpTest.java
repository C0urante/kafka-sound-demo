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
package io.github.c0urante.kafka.sound.demo.warp;

import io.github.c0urante.kafka.sound.demo.AudioTestUtils;
import io.github.c0urante.kafka.sound.demo.InteractiveTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InteractiveTest.class)
public class WarpTest {

    @Test
    public void testSineOpenStrings() {
        testWarpingAlgorithm(AudioTestUtils.OPEN_STRINGS_FILE, new Sine());
    }

    @Test
    public void testTriangleOpenStrings() {
        testWarpingAlgorithm(AudioTestUtils.OPEN_STRINGS_FILE, new Triangle());
    }

    @Test
    public void testSemiCircleOpenStrings() {
        testWarpingAlgorithm(AudioTestUtils.OPEN_STRINGS_FILE, new SemiCircle());
    }

    @Test
    public void testSawtoothOpenStrings() {
        testWarpingAlgorithm(AudioTestUtils.OPEN_STRINGS_FILE, new Sawtooth());
    }

    @Test
    public void testSquareOpenStrings() {
        testWarpingAlgorithm(AudioTestUtils.OPEN_STRINGS_FILE, new Square());
    }

    @Test
    public void testSineBach() {
        testWarpingAlgorithm(AudioTestUtils.BACH_FILE, new Sine());
    }

    @Test
    public void testTriangleBach() {
        testWarpingAlgorithm(AudioTestUtils.BACH_FILE, new Triangle());
    }

    @Test
    public void testSemiCircleBach() {
        testWarpingAlgorithm(AudioTestUtils.BACH_FILE, new SemiCircle());
    }

    @Test
    public void testSawtoothBach() {
        testWarpingAlgorithm(AudioTestUtils.BACH_FILE, new Sawtooth());
    }

    @Test
    public void testSquareBach() {
        testWarpingAlgorithm(AudioTestUtils.BACH_FILE, new Square());
    }

    @Test
    public void testSineQueenGuitar() {
        testWarpingAlgorithm(AudioTestUtils.QUEEN_GUITAR_2_FILE, new Sine());
    }

    @Test
    public void testTriangleQueenGuitar() {
        testWarpingAlgorithm(AudioTestUtils.QUEEN_GUITAR_2_FILE, new Triangle());
    }

    @Test
    public void testSemiCircleQueenGuitar() {
        testWarpingAlgorithm(AudioTestUtils.QUEEN_GUITAR_2_FILE, new SemiCircle());
    }

    @Test
    public void testSawtoothQueenGuitar() {
        testWarpingAlgorithm(AudioTestUtils.QUEEN_GUITAR_2_FILE, new Sawtooth());
    }

    @Test
    public void testSquareQueenGuitar() {
        testWarpingAlgorithm(AudioTestUtils.QUEEN_GUITAR_2_FILE, new Square());
    }

    private void testWarpingAlgorithm(String file, WarpingAlgorithm algorithm) {
        Warp warp = new Warp(algorithm);
        AudioTestUtils.testAlgorithmOnFile(file, warp::apply);
    }

}
