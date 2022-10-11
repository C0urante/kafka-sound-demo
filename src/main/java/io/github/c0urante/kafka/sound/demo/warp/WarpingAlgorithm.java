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
package io.github.c0urante.kafka.sound.demo.warp;

import io.github.c0urante.kafka.sound.demo.util.SampleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class WarpingAlgorithm {

    private boolean positiveWave;
    private int length;
    private short peak;

    protected WarpingAlgorithm() {
        this.positiveWave = true;
        this.length = 0;
        this.peak = 0;
    }

    public List<Short> warp(List<Short> samples) {
        List<Short> result = new ArrayList<>();
        for (Short sample : samples)
            result.addAll(buffer(sample));
        return result;
    }

    public abstract String name();
    protected abstract List<Short> warp(int length, short peak);

    private List<Short> buffer(short sample) {
        if (length == 0) {
            // Very first sample; buffer it and continue
            positiveWave = positive(sample);
            length = 1;
            return Collections.emptyList();
        }

        boolean positiveSample = positive(sample);
        if (positiveSample == positiveWave) {
            // We haven't crossed the X-axis; buffer the sample and continue
            length++;
            peak = (short) Math.max(peak, SampleUtils.abs(sample));
            return Collections.emptyList();
        } else {
            // We've crossed the X-axis; empty the buffer and then store the new sample
            List<Short> result = warp(positiveWave, length, peak);
            positiveWave = positiveSample;
            length = 1;
            peak = SampleUtils.abs(sample);
            return result;
        }
    }

    private List<Short> warp(boolean positive, int length, short peak) {
        List<Short> reuslt = warp(length, peak);
        if (!positive)
            reuslt.replaceAll(s -> (short) (-1 * s));
        return reuslt;
    }

    private static boolean positive(short sample) {
        return sample >= 0;
    }

}
