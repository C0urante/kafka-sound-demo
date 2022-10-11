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

import io.github.c0urante.kafka.sound.demo.distortion.ClippingAlgorithm;
import io.github.c0urante.kafka.sound.demo.distortion.CubicNonLinearity;
import io.github.c0urante.kafka.sound.demo.distortion.Distortion;
import io.github.c0urante.kafka.sound.demo.distortion.HyperbolicTangent;
import io.github.c0urante.kafka.sound.demo.distortion.Square;
import io.github.c0urante.kafka.sound.demo.filter.Filter;
import io.github.c0urante.kafka.sound.demo.filter.LowPassFourStage;
import io.github.c0urante.kafka.sound.demo.filter.LowPassSingleStage;
import io.github.c0urante.kafka.sound.demo.reverb.Reverb;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;
import java.util.function.UnaryOperator;

@Category(InteractiveTest.class)
public class MultipleEffectsTest {

    @Test
    public void control() {
        testMultipleEffects(AudioTestUtils.QUEEN_GUITAR_2_FILE);
    }

    @Test
    public void squareClipControl() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1))
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_110() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 110)::filter
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_220() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 220)::filter
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_440() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 440)::filter
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_880() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 880)::filter
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_1760() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 1760)::filter
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_3520() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 3520)::filter
        );
    }

    @Test
    public void testSquareClip_03_SingleStageLowPass_1024_7040() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassSingleStage(1024, 7040)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourStageLowPass_1024_110() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 110)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourStageLowPass_1024_220() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 220)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourStageLowPass_1024_440() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 440)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourStageLowPass_1024_880() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 880)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourtageLowPass_1024_1760() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 1760)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourtageLowPass_1024_3520() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 3520)::filter
        );
    }

    @Test
    public void testSquareClip_03_FourtageLowPass_1024_7040() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                new LowPassFourStage(1024, 7040)::filter
        );
    }

    @Test
    public void testSquareClip_03_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.03, 0.1)),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testSquareClip_04_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.04, 0.1)),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testSquareClip_05_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.05, 0.1)),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testSquareClip_06_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.06, 0.1)),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testSquareClip_07_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new Square(0.07, 0.1)),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testCubicNonLinearityClip_03_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new CubicNonLinearity()),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testCubicNonLinearityClip_04_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new CubicNonLinearity()),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testCubicNonLinearityClip_05_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new CubicNonLinearity()),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testCubicNonLinearityClip_06_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new CubicNonLinearity()),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testCubicNonLinearityClip_07_Reverb_70_30_45_75_105() {
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                distortion(new CubicNonLinearity()),
                reverb(0.70, 30, 45, 75, 105)
        );
    }

    @Test
    public void testGraduallyIncreasingCubicNonLinearityClipLowPassFilter() {
        Distortion distortion = new Distortion(new CubicNonLinearity());
        distortion.amplificationFactor().setDelayed(25, SampleUtils.msToSamples(1500));
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                AudioTestUtils.batch(distortion::apply),
                new LowPassFourStage(1024, 3000)::filter
        );
    }

    @Test
    public void testGraduallyDecreasingCubicNonLinearityClipLowPassFilter() {
        Distortion distortion = new Distortion(new CubicNonLinearity());
        distortion.amplificationFactor().setNow(25);
        distortion.amplificationFactor().setDelayed(1, SampleUtils.msToSamples(3000));
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                AudioTestUtils.batch(distortion::apply),
                new LowPassFourStage(1024, 3000)::filter
        );
    }

    @Test
    public void testGraduallyApplyingHyperbolicTangent_1() {
        // Fuck it, good enough
        // Gets a little quieter when distorted but the gentle transition makes a huge difference
        // Hopefully nobody cares too much that this still sounds absolutely heinous
        Distortion distortion = new Distortion(new HyperbolicTangent());
        distortion.amplificationFactor().setDelayed(15, 22050);
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_1_FILE,
                AudioTestUtils.batch(distortion::apply)
        );
    }

    @Test
    public void testGraduallyApplyingHyperbolicTangent_2() {
        Distortion distortion = new Distortion(new HyperbolicTangent());
        distortion.amplificationFactor().setDelayed(15, 22050);
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                AudioTestUtils.batch(distortion::apply)
        );
    }

    @Test
    public void testGraduallyRevertingHyperbolicTangent() {
        Distortion distortion = new Distortion(new HyperbolicTangent());
        distortion.amplificationFactor().setNow(10);
        distortion.amplificationFactor().setDelayed(1, 44100);
        testMultipleEffects(
                AudioTestUtils.QUEEN_GUITAR_2_FILE,
                AudioTestUtils.batch(distortion::apply)
        );
    }

    private UnaryOperator<List<Short>> distortion(ClippingAlgorithm algorithm) {
        return AudioTestUtils.batch(algorithm::clip);
    }

    private UnaryOperator<List<Short>> reverb(double decay, int... delays) {
        Reverb reverb = new Reverb(decay, delays);
        return AudioTestUtils.batch(reverb::apply);
    }

    @SafeVarargs
    private final void testMultipleEffects(
            String file, UnaryOperator<List<Short>>... effects
    ) {
        UnaryOperator<List<Short>> combinedEffects = samples -> {
            for (UnaryOperator<List<Short>> effect : effects)
                samples = effect.apply(samples);
            return samples;
        };
        AudioTestUtils.testAlgorithmOnFile(file, combinedEffects);
    }

}
