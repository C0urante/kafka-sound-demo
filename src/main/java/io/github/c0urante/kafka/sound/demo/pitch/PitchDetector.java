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
package io.github.c0urante.kafka.sound.demo.pitch;

import io.github.c0urante.kafka.sound.demo.util.CircularIterator;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PitchDetector implements ValueMapper<byte[], List<String>> {

    private static final List<PitchAlgorithm> ALL_ALGORITHMS = Arrays.asList(
            new Yin(),
            new AverageMagnitudeDifference(),
            new DynamicWavelet(),
            new McLeodPitchMethod(),
            new FastYin()
    );

    private final CircularIterator<PitchAlgorithm> algorithms;
    // We may end up changing algorithms in the middle of processing a single batch
    // of samples, but aurally that should be no different from changing algorithms
    // in between two batches
    // So we don't need synchronization or anything fancy, this can just be volatile
    private volatile PitchAlgorithm algorithm;

    public PitchDetector() {
        this(ALL_ALGORITHMS);
    }

    private PitchDetector(List<PitchAlgorithm> pitchAlgorithms) {
        if (pitchAlgorithms.isEmpty())
            throw new IllegalArgumentException("At least one algorithm must be provided");
        this.algorithms = new CircularIterator<>(pitchAlgorithms);
        this.algorithm = pitchAlgorithms.get(0);
    }

    @Override
    public List<String> apply(byte[] encodedSamples) {
        return algorithm.pitches(encodedSamples).stream()
                .map(p -> p.name)
                .collect(Collectors.toList());
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
