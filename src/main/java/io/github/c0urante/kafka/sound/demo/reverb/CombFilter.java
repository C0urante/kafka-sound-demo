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

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CombFilter {

    private volatile Supplier<Double> decay;
    private final Queue<Short> delayedSamples;

    public CombFilter(int delay, Supplier<Double> decay) {
        if (delay <= 0)
            throw new IllegalArgumentException("Delay must be positive");

        this.decay = decay;
        // GUYS LOOK I USED A LINKED LIST IRL
        this.delayedSamples = new LinkedList<>();
        // Don't need to store our delay; just assume that everything was silent for
        // the complete duration of it before we started tracking samples
        IntStream.range(0, delay).forEach(i -> delayedSamples.add((short) 0));
    }

    public synchronized short apply(short sample) {
        // Queue::poll is more elegant but IntelliJ won't stfu about NPEs from unboxing
        // Lol now it's trying to tell me there's a typo in stfu
        double outputSample = ((delayedSamples.remove() * decay.get()) + sample);
        delayedSamples.add(SampleUtils.coerceToSample(outputSample));
        // We divide by (1 + decay) to make sure that we don't get overflow errors
        return (short) (outputSample / (1 + decay.get()));
    }

}
