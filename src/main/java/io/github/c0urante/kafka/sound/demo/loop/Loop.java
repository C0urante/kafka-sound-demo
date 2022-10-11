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
package io.github.c0urante.kafka.sound.demo.loop;

import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import io.github.c0urante.kafka.sound.demo.util.CircularIterator;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Loop implements ValueMapper<Short, Short> {

    private static final Logger log = LoggerFactory.getLogger(Loop.class);
    private static final int CURRENT_SAMPLE_BIAS = 3;

    private final double loopAmplificationFactor;

    private boolean looping;
    private List<List<Short>> newLoopSamples;
    private List<List<Short>> savedLoopSamples;
    private CircularIterator<List<Short>> newLoop;
    private CircularIterator<List<Short>> savedLoop;


    public Loop(double loopAmplificationFactor) {
        if (loopAmplificationFactor <= 0 || loopAmplificationFactor > 1) {
            throw new IllegalArgumentException(
                    "Invalid loop amplification factor: " + loopAmplificationFactor + "; must be in range(0, 1]");
        }
        this.loopAmplificationFactor = loopAmplificationFactor;

        this.looping = false;
    }

    @Override
    public synchronized Short apply(Short sample) {
        if (savedLoop != null) {
            // We've already closed the loop
            List<Short> savedLooped = savedLoop.next();
            List<Short> newLooped = newLoop != null ? newLoop.next() : null;

            List<Short> audible = new ArrayList<>(savedLooped);
            if (newLooped != null)
                audible.addAll(newLooped);

            // Calculate the sample we're going to play to the user
            // Over-represent the current input sample so that it's not drowned out by older samples
            IntStream.of(CURRENT_SAMPLE_BIAS).forEach(i -> audible.add(sample));
            // Surprisingly, summing works fine here instead of averaging
            // There doesn't seem to be much risk of overflow (or really, clipping)
            // until numerous layers are added to the loop
            short result = sum(audible);

            if (looping && newLooped != null) {
                newLooped.add((short) (sample * loopAmplificationFactor));
            }

            return result;
        } else {
            if (looping) {
                // We're still filling in the first loop
                assert newLoopSamples != null;

                List<Short> firstLayer = new ArrayList<>();
                firstLayer.add(sample);
                newLoopSamples.add(firstLayer);
            }

            return sample;
        }
    }

    public synchronized void loop() {
        if (looping) {
            if (savedLoop == null) {
                // We just finished the first loop; keep looping
                savedLoopSamples = deepCopy(newLoopSamples);
                savedLoop = new CircularIterator<>(savedLoopSamples);

                // Start adding new layers immediately
                newLoopSamples = emptyLoop(savedLoopSamples.size());
                newLoop = new CircularIterator<>(newLoopSamples);

                log.info("Finished new loop");
            } else {
                // Don't save the samples just yet--we may still want to wipe them
                looping = false;

                log.info("Finished new layer(s)");
            }
        } else {
            if (savedLoopSamples == null) {
                // Time to begin the first loop
                // Conservatively, we'll want to loop for at least one second
                this.newLoopSamples = new ArrayList<>(AudioFormats.SAMPLE_RATE);

                log.info("Starting new loop");
            } else {
                if (newLoopSamples != null) {
                    // It's finally safe to save the new samples, right before we begin
                    // recording new layers
                    assert newLoopSamples.size() == savedLoopSamples.size();

                    for (int i = 0; i < savedLoopSamples.size(); i++) {
                        savedLoop.next().addAll(newLoop.next());
                    }
                }

                newLoopSamples = emptyLoop(savedLoopSamples.size());
                newLoop = new CircularIterator<>(newLoopSamples);

                log.info("Starting new layer(s)");
            }
            looping = true;
        }
    }

    public synchronized void clear() {
        if (newLoopSamples != null) {
            newLoopSamples = null;
            newLoop = null;

            if (looping && savedLoop == null) {
                log.info("Aborting new loop");
            } else {
                log.info("Wiping most recent layers");
            }
        } else if (savedLoopSamples != null) {
            savedLoopSamples = null;
            savedLoop = null;

            log.info("Wiping saved loop");
        } else {
            assert !looping;

            log.info("No loop to clear");
        }
        looping = false;
    }

    private List<List<Short>> emptyLoop(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> new ArrayList<Short>())
                .collect(Collectors.toList());
    }

    private <E> List<List<E>> deepCopy(List<List<E>> nestedList) {
        return nestedList.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    private short sum(List<Short> samples) {
        long sum = samples.stream().mapToLong(Short::longValue).sum();
        return SampleUtils.coerceToSample(sum);
    }

}
