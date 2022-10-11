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

import io.github.c0urante.kafka.sound.demo.util.CircularIterator;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class Filter implements ValueMapper<List<Short>, List<Short>> {

    private static final int MAX_CUTOFF = 30_000;
    private static final int WINDOW_SIZE = 1024;
    private static final List<BiFunction<Float, Float, FilterAlgorithm>> ALL_ALGORITHMS = Arrays.asList(
            (low, high) -> new BandPass(WINDOW_SIZE, low, high),
            (low, high) -> new HighPass(WINDOW_SIZE, low),
            (low, high) -> new LowPassFourStage(WINDOW_SIZE, high),
            (low, high) -> new LowPassSingleStage(WINDOW_SIZE, high)
    );

    private final CircularIterator<BiFunction<Float, Float, FilterAlgorithm>> algorithms;
    private float min;
    private float max;
    private volatile FilterAlgorithm algorithm;

    public Filter() {
        this(ALL_ALGORITHMS);
    }

    public Filter(BiFunction<Float, Float, FilterAlgorithm> algorithm) {
        this(Collections.singletonList(algorithm));
    }

    public Filter(List<BiFunction<Float, Float, FilterAlgorithm>> algorithms) {
        if (algorithms.isEmpty())
            throw new IllegalArgumentException("At least one algorithm must be provided");
        this.min = 0;
        this.max = MAX_CUTOFF;
        this.algorithms = new CircularIterator<>(algorithms);
        this.algorithm = createAlgorithm(algorithms.get(0));
    }

    @Override
    public List<Short> apply(List<Short> samples) {
        return algorithm.filter(samples);
    }

    public synchronized String nextAlgorithm() {
        // Unreadable garbage FTW
        return (this.algorithm = createAlgorithm(algorithms.next())).name();
    }

    public synchronized String prevAlgorithm() {
        // Unreadable garbage FTW
        return (this.algorithm = createAlgorithm(algorithms.prev())).name();
    }

    public String currentAlgorithm() {
        return this.algorithm.name();
    }

    public synchronized void min(float min) {
        this.min = min;
        this.algorithm = createAlgorithm(algorithms.current());
    }

    public synchronized void max(float max) {
        this.max = max;
        this.algorithm = createAlgorithm(algorithms.current());
    }

    public float min() {
        return min;
    }

    public float max() {
        return max;
    }

    private FilterAlgorithm createAlgorithm(BiFunction<Float, Float, FilterAlgorithm> algorithm) {
        return algorithm.apply(min, max);
    }

}
