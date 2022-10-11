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

import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import io.github.c0urante.kafka.sound.demo.util.AdjustableDouble;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reverb implements ValueMapper<Short, Short> {

    private static final double MAX_DECAY = 0.999;
    private static final double MIN_DECAY = 0.001;
    // All primes between 20 and 100
    private static final int[] DEFAULT_DELAYS = new int[] {
            23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
    };

    private final List<CombFilter> combFilters;
    private final AdjustableDouble decay;

    public Reverb(double initialDecay) {
        this(initialDecay, DEFAULT_DELAYS);
    }

    public Reverb(double initialDecay, int... delays) {
        this.decay = new AdjustableDouble(MIN_DECAY, MAX_DECAY, initialDecay);
        this.combFilters = IntStream.of(delays).mapToObj(
                delay -> new CombFilter(SampleUtils.msToSamples(delay), decay::current)
        ).collect(Collectors.toList());
    }

    @Override
    public Short apply(Short sample) {
        decay.next();
        List<Short> filteredSamples = combFilters.stream()
                .map(combFilter -> combFilter.apply(sample))
                .collect(Collectors.toList());
        return average(filteredSamples);
    }

    public AdjustableDouble decay() {
        return decay;
    }

    private static short average(List<Short> shorts) {
        int sum = shorts.stream().mapToInt(Short::intValue).sum();
        return (short) (sum / shorts.size());
    }

}
