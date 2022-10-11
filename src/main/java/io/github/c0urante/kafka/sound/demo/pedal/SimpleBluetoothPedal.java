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
package io.github.c0urante.kafka.sound.demo.pedal;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleBluetoothPedal extends BluetoothPedal{

    private final AtomicBoolean leftPressed;
    private final AtomicBoolean rightPressed;

    public SimpleBluetoothPedal() {
        this.leftPressed = new AtomicBoolean(false);
        this.rightPressed = new AtomicBoolean(false);
    }

    @Override
    protected void leftDown() {
        if (!leftPressed.getAndSet(true))
            left();
    }

    @Override
    protected void leftUp() {
        leftPressed.set(false);
    }

    @Override
    protected void rightDown() {
        if (!rightPressed.getAndSet(true))
            right();
    }

    @Override
    protected void rightUp() {
        rightPressed.set(false);
    }

    protected abstract void left();
    protected abstract void right();

}
