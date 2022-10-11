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
package io.github.c0urante.kafka.sound.demo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class CircularIterator<E> {

    private final List<E> elements;
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @SafeVarargs
    public CircularIterator(E... elements) {
        this(Arrays.asList(elements));
    }

    public CircularIterator(List<E> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public E next() {
        return get(this::nextIndex);
    }

    public E prev() {
        return get(this::prevIndex);
    }

    public E current() {
        return get(IntUnaryOperator.identity());
    }

    private int nextIndex(int i) {
        return (i + 1) % elements.size();
    }

    private int prevIndex(int i) {
        return i == 0 ? elements.size() - 1 : i - 1;
    }

    private E get(IntUnaryOperator updateIndex) {
        int index = currentIndex.updateAndGet(updateIndex);
        return elements.get(index);
    }

}
