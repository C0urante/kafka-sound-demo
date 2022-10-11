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
package io.github.c0urante.kafka.sound.demo.streams;

import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Grouping<E> implements ValueMapper<E, List<E>> {

    private final List<E> buffer;
    private final int size;

    public Grouping(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Invalid size: " + size + "; must be positive");

        this.size = size;
        this.buffer = new ArrayList<>(size);
    }

    @Override
    public List<E> apply(E element) {
        List<E> result = Collections.emptyList();

        buffer.add(element);
        if (buffer.size() >= size) {
            result = new ArrayList<>(buffer);
            buffer.clear();
        }

        return result;
    }

}
