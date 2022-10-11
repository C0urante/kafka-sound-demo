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

public class SemiCircle extends WarpingAlgorithm {

    @Override
    public String name() {
        return "Semi-circle";
    }

    @Override
    public List<Short> warp(int length, short peak) {
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
        for (int i = 0; i < length; i++) {
            double x = i * 1.0 / length;
            // Derived from (2x - 1)^2 + y^2 = 1
            double y = 2 * Math.sqrt(x * (1 - x));
            short sample = (short) (peak * y);
            result.add(sample);
        }

        return result;
    }

}
