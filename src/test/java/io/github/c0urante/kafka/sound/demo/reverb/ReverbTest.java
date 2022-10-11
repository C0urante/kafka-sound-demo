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
public class ReverbTest {

    @Test
    public void testReverb_999_80_120() {
        // Don't listen to this
        testReverb(0.999, 80, 120);
    }

    @Test
    public void testReverb_999_80_120_200_280() {
        testReverb(0.999, 80, 120, 200, 280);
    }

    @Test
    public void testReverb_999_80_120_200_280_440_520_680_760() {
        testReverb(0.999, 80, 120, 200, 280, 440, 520, 680, 760);
    }

    @Test
    public void testReverb_999_80_120_200_280_440_520_680_760_920_1160_1240_1480_1640_1720_1880() {
        testReverb(0.999, 80, 120, 200, 280, 440, 520, 680, 760, 920, 1160, 1240, 1480, 1640, 1720, 1880);
    }

    @Test
    public void testReverb_999_lots() {
        testReverb(0.999, 20, 30, 50, 70, 110, 130, 170, 190, 230, 290, 310, 370, 410, 430, 470, 530, 570, 590, 610, 710, 730, 790, 830, 870, 890, 910, 930);
    }

    @Test
    public void testReverb_90_80_120() {
        testReverb(0.90, 80, 120);
    }

    @Test
    public void testReverb_90_80_120_200_280() {
        testReverb(0.90, 80, 120, 200, 280);
    }

    @Test
    public void testReverb_90_80_120_200_280_440_520_680_760() {
        testReverb(0.90, 80, 120, 200, 280, 440, 520, 680, 760);
    }

    @Test
    public void testReverb_90_80_120_200_280_440_520_680_760_920_1160_1240_1480_1640_1720_1880() {
        testReverb(0.90, 80, 120, 200, 280, 440, 520, 680, 760, 920, 1160, 1240, 1480, 1640, 1720, 1880);
    }

    @Test
    public void testReverb_90_23_31_37_41_43_47_53_59_61_67_71_73_79_83_89_97() {
        // All-pass filter? Where we're going, we don't need all-pass filters!
        testReverb(0.90, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97);
    }

    @Test
    public void testReverb_85_80_120() {
        testReverb(0.85, 80, 120);
    }

    @Test
    public void testReverb_85_30_45_75_105() {
        testReverb(0.85, 30, 45, 75, 105);
    }

    @Test
    public void testReverb_85_80_120_200_280() {
        testReverb(0.85, 80, 120, 200, 280);
    }

    @Test
    public void testReverb_85_80_120_200_280_440_520_680_760() {
        testReverb(0.85, 80, 120, 200, 280, 440, 520, 680, 760);
    }

    @Test
    public void testReverb_85_80_120_200_280_440_520_680_760_920_1160_1240_1480_1640_1720_1880() {
        testReverb(0.85, 80, 120, 200, 280, 440, 520, 680, 760, 920, 1160, 1240, 1480, 1640, 1720, 1880);
    }

    @Test
    public void testReverb_85_23_31_37_41_43_47_53_59_61_67_71_73_79_83_89_97() {
        // All-pass filter? Where we're going, we don't need all-pass filters!
        testReverb(0.85, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97);
    }

    @Test
    public void testReverb_80_30_45_75_105() {
        testReverb(0.80, 30, 45, 75, 105);
    }

    @Test
    public void testReverb_80_30_37_45_59_75_89_105() {
        testReverb(0.8, 30, 37, 45, 59, 75, 89, 105);
    }

    @Test
    public void testReverb_80_23_31_37_41_43_47_53_59_61_67_71_73_79_83_89_97() {
        // All-pass filter? Where we're going, we don't need all-pass filters!
        testReverb(0.8, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97);
    }

    @Test
    public void testReverb_70_80_120() {
        testReverb(0.70, 80, 120);
    }

    @Test
    public void testReverb_70_30_45_75_105() {
        testReverb(0.70, 30, 45, 75, 105);
    }

    @Test
    public void testReverb_70_80_120_200_280() {
        testReverb(0.70, 80, 120, 200, 280);
    }

    @Test
    public void testReverb_70_80_120_200_280_440_520_680_760() {
        testReverb(0.70, 80, 120, 200, 280, 440, 520, 680, 760);
    }

    @Test
    public void testReverb_70_80_120_200_280_440_520_680_760_920_1160_1240_1480_1640_1720_1880() {
        testReverb(0.70, 80, 120, 200, 280, 440, 520, 680, 760, 920, 1160, 1240, 1480, 1640, 1720, 1880);
    }

    @Test
    public void testReverb_60_80_120() {
        testReverb(0.60, 80, 120);
    }

    @Test
    public void testReverb_60_80_120_200_280() {
        testReverb(0.60, 80, 120, 200, 280);
    }

    @Test
    public void testReverb_60_80_120_200_280_440_520_680_760() {
        testReverb(0.60, 80, 120, 200, 280, 440, 520, 680, 760);
    }

    @Test
    public void testReverb_60_80_120_200_280_440_520_680_760_920_1160_1240_1480_1640_1720_1880() {
        testReverb(0.60, 80, 120, 200, 280, 440, 520, 680, 760, 920, 1160, 1240, 1480, 1640, 1720, 1880);
    }

    private void testReverb(double decay, int... delays) {
        Reverb reverb = new Reverb(decay, delays);
        AudioTestUtils.testAlgorithmOnFile(AudioTestUtils.QUEEN_OPENING_FILE, AudioTestUtils.batch(reverb::apply));
    }

}
