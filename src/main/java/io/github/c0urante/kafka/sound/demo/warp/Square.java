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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Square extends WarpingAlgorithm {

    @Override
    public String name() {
        return "Square";
    }

    @Override
    protected List<Short> warp(int length, short peak) {
        return IntStream.range(0, length).mapToObj(i -> peak).collect(Collectors.toList());
    }

}
