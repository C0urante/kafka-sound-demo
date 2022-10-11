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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sine extends WarpingAlgorithm {

    @Override
    public String name() {
        return "Sine";
    }

    @Override
    protected List<Short> warp(int length, short peak) {
        if (length == 0) {
            // Should never happen, but better than throwing an exception
            // I guess if I really wanted to be a try-hard I'd add a log message here
            return Collections.emptyList();
        }

        if (peak < 0) {
            // This we truly cannot abide
            throw new IllegalArgumentException("Invalid peak " + peak + "; may not be negative");
        }

        List<Short> result = new ArrayList<>(length);
        // What's up length?
        int upLength = length / 2;
        int downLength = length - upLength;
        for (int i = 0; i < upLength; i++) {
            double progress = i * 1.0 / upLength;
            short sample = (short) (peak * Math.sin(progress * Math.PI / 2));
            result.add(sample);
        }
        for (int i = 0; i < downLength; i++) {
            double progress = i * 1.0 / downLength;
            short sample = (short) (peak * Math.sin((1 - progress) * Math.PI / 2));
            result.add(sample);
        }

        return result;
    }

}
