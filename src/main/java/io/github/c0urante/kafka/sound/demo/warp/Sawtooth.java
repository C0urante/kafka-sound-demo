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
import java.util.List;

public class Sawtooth extends WarpingAlgorithm {

    @Override
    public String name() {
        return "Sawtooth";
    }

    @Override
    protected List<Short> warp(int length, short peak) {
        List<Short> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            double progress = i * 1.0 / length;
            short sample = (short) (progress * peak);
            result.add(sample);
        }
        return result;
    }

}
