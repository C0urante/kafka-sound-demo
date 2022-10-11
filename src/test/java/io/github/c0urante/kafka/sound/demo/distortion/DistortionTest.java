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
package io.github.c0urante.kafka.sound.demo.distortion;

import io.github.c0urante.kafka.sound.demo.AudioTestUtils;
import io.github.c0urante.kafka.sound.demo.InteractiveTest;
import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(InteractiveTest.class)
public class DistortionTest {

    @Test
    public void testSquareClip_1() {
        // Warning: this sounds like shit
        // Floor is too high, there's a lot of cutoff
        // Sounds like my poor cello is choking :(
        testSquareClip(0.1);
    }

    @Test
    public void testSquareClip_08() {
        // Still quite shitty, but at least better than 0.1
        testSquareClip(0.08);
    }

    @Test
    public void testSquareClip_06() {
        // Almost sounds alright
        testSquareClip(0.06);
    }

    @Test
    public void testSquareClip_05() {
        // Sweet spot
        testSquareClip(0.05);
    }

    @Test
    public void testSquareClip_04() {
        testSquareClip(0.04);
    }

    @Test
    public void testSquareClip_03() {
        testSquareClip(0.03);
    }

    @Test
    public void testSquareClip_02() {
        testSquareClip(0.02);
    }

    private void testSquareClip(double floor) {
        testAmplifiedDistortionAlgorithm(1, new Square(floor, Square.DEFAULT_AMPLITUDE));
    }

    @Test
    public void testCubicNonLinearityClip_10() {
        testCubicNonLinearityClip( 10);
    }

    @Test
    public void testCubicNonLinearityClip_5() {
        testCubicNonLinearityClip( 5);
    }

    @Test
    public void testCubicNonLinearityClip_4() {
        testCubicNonLinearityClip( 4);
    }

    @Test
    public void testCubicNonLinearityClip_3() {
        testCubicNonLinearityClip( 3);
    }

    @Test
    public void testCubicNonLinearityClip_2() {
        testCubicNonLinearityClip( 2);
    }

    @Test
    public void testCubicNonLinearityClip_1_5() {
        testCubicNonLinearityClip( 1.5);
    }

    private void testCubicNonLinearityClip(double amplificationFactor) {
        testAmplifiedDistortionAlgorithm(amplificationFactor, new CubicNonLinearity());
    }

    @Test
    public void testExponentialClip_20() {
        testExponentialClip( 20);
    }

    @Test
    public void testExponentialClip_15() {
        testExponentialClip( 15);
    }

    @Test
    public void testExponentialClip_10() {
        testExponentialClip( 10);
    }

    private void testExponentialClip(double amplificationFactor) {
        testAmplifiedDistortionAlgorithm(amplificationFactor, new Exponential());
    }
    @Test
    public void testOverdrive_25() {
        testOverdrive( 25);
    }

    @Test
    public void testOverdrive_20() {
        testOverdrive( 20);
    }

    @Test
    public void testOverdrive_15() {
        testOverdrive( 15);
    }

    @Test
    public void testOverdrive_10() {
        testOverdrive( 10);
    }

    @Test
    public void testOverdrive_5() {
        testOverdrive( 5);
    }

    @Test
    public void testOverdrive_4() {
        testOverdrive( 4);
    }

    @Test
    public void testOverdrive_3() {
        testOverdrive( 3);
    }

    @Test
    public void testOverdrive_2() {
        testOverdrive( 2);
    }

    @Test
    public void testOverdrive_1_5() {
        testOverdrive( 1.5);
    }

    private void testOverdrive(double amplificationFactor) {
        testAmplifiedDistortionAlgorithm(amplificationFactor, new Overdrive());
    }

    @Test
    public void testReciprocal_5() {
        testReciprocal(5);
    }

    @Test
    public void testReciprocal_3() {
        testReciprocal(3);
    }

    @Test
    public void testReciprocal_1_5() {
        testReciprocal(1.5);
    }

    private void testReciprocal(double amplificationFactor) {
        testAmplifiedDistortionAlgorithm(amplificationFactor, new Reciprocal());
    }

    @Test
    public void testHyperbolicTangent_20() {
        testHyperbolicTangent(20);
    }

    @Test
    public void testHyperbolicTangent_15() {
        testHyperbolicTangent(15);
    }

    @Test
    public void testHyperbolicTangent_10() {
        testHyperbolicTangent(10);
    }

    @Test
    public void testHyperbolicTangent_7() {
        testHyperbolicTangent(7);
    }

    @Test
    public void testHyperbolicTangent_5() {
        testHyperbolicTangent(5);
    }

    @Test
    public void testHyperbolicTangent_3() {
        testHyperbolicTangent(3);
    }

    @Test
    public void testHyperbolicTangent_1_5() {
        testHyperbolicTangent(1.5);
    }

    private void testHyperbolicTangent(double amplificationFactor) {
        testAmplifiedDistortionAlgorithm(amplificationFactor, new HyperbolicTangent());
    }

    @Test
    public void testHardClip_10_05() {
        testHardClip(10, 0.05);
    }

    @Test
    public void testHardClip_5_05() {
        testHardClip(5, 0.05);
    }

    @Test
    public void testHardClip_4_05() {
        testHardClip(4, 0.05);
    }

    @Test
    public void testHardClip_3_05() {
        testHardClip(3, 0.05);
    }

    @Test
    public void testHardClip_2_05() {
        testHardClip(2, 0.05);
    }

    @Test
    public void testHardClip_1_5_05() {
        testHardClip(1.5, 0.05);
    }

    @Test
    public void testHardClip_10_1() {
        testHardClip(10, 0.1);
    }

    @Test
    public void testHardClip_5_1() {
        testHardClip(5, 0.1);
    }

    @Test
    public void testHardClip_4_1() {
        testHardClip(4, 0.1);
    }

    @Test
    public void testHardClip_3_1() {
        testHardClip(3, 0.1);
    }

    @Test
    public void testHardClip_2_1() {
        testHardClip(2, 0.1);
    }

    @Test
    public void testHardClip_1_5_1() {
        testHardClip(1.5, 0.1);
    }

    @Test
    public void testHardClip_1() {
        testHardClip(1, 0.1);
    }

    @Test
    public void testHardClip_15() {
        testHardClip(1, 0.15);
    }

    @Test
    public void testHardClip_2() {
        testHardClip(1, 0.2);
    }

    @Test
    public void testHardClip_3() {
        testHardClip(1, 0.3);
    }

    @Test
    public void testHardClip_4() {
        testHardClip(1, 0.4);
    }

    private void testHardClip(double amplificationFactor, double ceiling) {
        testAmplifiedDistortionAlgorithm(amplificationFactor, new Hard(ceiling));
    }

    @Test
    public void testGradualHardClip_1_20_5_5() {
        testGraduallyScaledAmplifiedDistortionAlgorithm(1, 20, 5, new Hard(0.5));
    }

    @Test
    public void testGradualCubicNonLinearityClip_1_20_5_5() {
        testGraduallyScaledAmplifiedDistortionAlgorithm(1, 20, 5, new CubicNonLinearity());
    }

    @Test
    public void testGradualOverdrive_1_20_5_5() {
        // Warning: this sounds horrible
        testGraduallyScaledAmplifiedDistortionAlgorithm(1, 20, 5, new Overdrive());
    }

    private void testAmplifiedDistortionAlgorithm(double amplificationFactor, ClippingAlgorithm algorithm) {
        Distortion distortion = new Distortion(algorithm);
        distortion.amplificationFactor().setNow(amplificationFactor);
        AudioTestUtils.testAlgorithmOnFile(AudioTestUtils.QUEEN_GUITAR_2_FILE, AudioTestUtils.batch(distortion::apply));
    }

    private void testGraduallyScaledAmplifiedDistortionAlgorithm(
            double startingFactor,
            double endingFactor,
            double secondsDelay,
            ClippingAlgorithm algorithm
    ) {
        int samplesDelay = (int) (secondsDelay * AudioFormats.SAMPLE_RATE);
        Distortion distortion = new Distortion(algorithm);
        distortion.amplificationFactor().setNow(startingFactor);
        distortion.amplificationFactor().setDelayed(endingFactor, samplesDelay);
        AudioTestUtils.testAlgorithmOnFile(AudioTestUtils.QUEEN_GUITAR_2_FILE, AudioTestUtils.batch(distortion::apply));
    }

}
