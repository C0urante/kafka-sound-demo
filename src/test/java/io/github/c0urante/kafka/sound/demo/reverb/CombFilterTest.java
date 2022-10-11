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
package io.github.c0urante.kafka.sound.demo.reverb;

import io.github.c0urante.kafka.sound.demo.AudioTestUtils;
import io.github.c0urante.kafka.sound.demo.InteractiveTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InteractiveTest.class)
public class CombFilterTest {

    @Test
    public void testCombFilter_50_85() {
        testCombFilter(50, 0.85);
    }

    @Test
    public void testCombFilter_50_7() {
        testCombFilter(50, 0.7);
    }

    @Test
    public void testCombFilter_50_5() {
        testCombFilter(50, 0.5);
    }

    @Test
    public void testCombFilter_100_85() {
        testCombFilter(100, 0.85);
    }

    @Test
    public void testCombFilter_100_7() {
        testCombFilter(100, 0.7);
    }

    @Test
    public void testCombFilter_100_5() {
        testCombFilter(100, 0.5);
    }

    @Test
    public void testCombFilter_200_85() {
        testCombFilter(200, 0.85);
    }

    @Test
    public void testCombFilter_200_7() {
        testCombFilter(200, 0.7);
    }

    @Test
    public void testCombFilter_200_5() {
        testCombFilter(200, 0.5);
    }

    private void testCombFilter(int delay, double decay) {
        int delayInSamples = delay * AudioTestUtils.SAMPLE_RATE / 1000;
        CombFilter combFilter = new CombFilter(delayInSamples, () -> decay);
        AudioTestUtils.testAlgorithmOnDefaultFile(samples -> {
            samples.replaceAll(combFilter::apply);
            return samples;
        });
    }

}
