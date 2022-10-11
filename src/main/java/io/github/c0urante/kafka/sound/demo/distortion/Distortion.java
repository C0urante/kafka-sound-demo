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

import io.github.c0urante.kafka.sound.demo.util.CircularIterator;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import io.github.c0urante.kafka.sound.demo.util.AdjustableDouble;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Distortion implements ValueMapper<Short, Short> {

    private static final List<ClippingAlgorithm> ALL_ALGORITHMS = Arrays.asList(
            new None(),
            new Square(),
            new Overdrive(),
            new Reciprocal(),
            new CubicNonLinearity(),
            new Exponential(),
            new Hard(),
            new HyperbolicTangent()
    );

    private final AdjustableDouble factor;
    private final CircularIterator<ClippingAlgorithm> algorithms;
    private ClippingAlgorithm algorithm;

    public Distortion() {
        this(ALL_ALGORITHMS);
    }

    public Distortion(ClippingAlgorithm algorithm) {
        this(Collections.singletonList(algorithm));
    }

    public Distortion(List<ClippingAlgorithm> clippingAlgorithms) {
        if (clippingAlgorithms.isEmpty())
            throw new IllegalArgumentException("At least one algorithm must be provided");
        this.factor = new AdjustableDouble(0.1, 100, 1);
        this.algorithms = new CircularIterator<>(clippingAlgorithms);
        this.algorithm = clippingAlgorithms.get(0);
    }

    @Override
    public Short apply(Short sample) {
        // 1. Apply amplification
        double amplificationFactor = factor.next();

        // Shameless hack: use the factor to determine if we should actually distort
        if (amplificationFactor >= 0.999 && amplificationFactor <= 1.001) {
            return sample;
        }

        double result = sample * amplificationFactor;
        short amplified = SampleUtils.coerceToSample(result);

        // 2. Apply clipping algorithm
        short clipped = algorithm.clip(amplified);

        // 3. De-amplify
        return SampleUtils.coerceToSample(clipped / amplificationFactor);
    }

    public AdjustableDouble amplificationFactor() {
        return factor;
    }

    public String nextAlgorithm() {
        // Unreadable garbage FTW
        return (this.algorithm = algorithms.next()).name();
    }

    public String prevAlgorithm() {
        // Unreadable garbage FTW
        return (this.algorithm = algorithms.prev()).name();
    }

    public String currentAlgorithm() {
        return this.algorithm.name();
    }

}
