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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class TriangleTest {

    @Test
    public void testSingleSample() {
        IntStream.of(1, 2, 3, 4, 5, 7, 11, 13, 14, 15, 16, 255, 256, Short.MAX_VALUE).forEach(peak -> {
            List<Integer> actual = triangle(1, peak);
            assertEquals(Arrays.asList(peak), actual);
        });
    }

    @Test
    public void testTwoSamples() {
        IntStream.of(1, 2, 3, 4, 5, 7, 11, 13, 14, 15, 16, 255, 256, Short.MAX_VALUE).forEach(peak -> {
            List<Integer> actual = triangle(2, peak);
            assertEquals(Arrays.asList(0, peak), actual);
        });
    }

    @Test
    public void testFourSamples() {
        IntStream.of(1, 2, 3, 4, 5, 7, 11, 13, 14, 15, 16, 255, 256, Short.MAX_VALUE).forEach(peak -> {
            List<Integer> actual = triangle(4, peak);
            List<Integer> expected = Arrays.asList(
                    0,
                    peak / 2,
                    peak,
                    peak - (short) (Math.ceil((peak / 2.0)))
            );
            assertEquals(expected, actual);
        });
    }

    private List<Integer> triangle(int length, int peak) {
        List<Short> result = Triangle.triangle(length, (short) peak);
        return result.stream().map(Short::intValue).collect(Collectors.toList());
    }

}
