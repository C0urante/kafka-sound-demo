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

import java.util.Timer;
import java.util.TimerTask;

public abstract class RepeatingBluetoothPedal extends BluetoothPedal {

    private final long leftDelay;
    private final long rightDelay;
    private final Timer timer;
    private boolean leftPressed;
    private boolean rightPressed;
    private TimerTask leftTask;
    private TimerTask rightTask;

    public RepeatingBluetoothPedal(long delay) {
        this(delay, delay);
    }

    public RepeatingBluetoothPedal(long leftDelay, long rightDelay) {
        this.leftDelay = leftDelay;
        this.rightDelay = rightDelay;
        this.timer = new Timer(true);
        this.leftPressed = false;
        this.rightPressed = false;
        this.leftTask = null;
        this.rightTask = null;
    }

    @Override
    protected synchronized void leftDown() {
        if (leftPressed)
            return;

        leftPressed = true;
        cancel(leftTask, rightTask);
        rightTask = null;

        leftTask = new LeftKeyPress();
        run(leftTask, leftDelay);
    }

    @Override
    protected synchronized void leftUp() {
        leftPressed = false;
        cancel(leftTask);
        leftTask = null;
    }

    @Override
    protected synchronized void rightDown() {
        if (rightPressed)
            return;

        rightPressed = true;
        cancel(rightTask, leftTask);
        leftTask = null;

        rightTask = new RightKeyPress();
        run(rightTask, rightDelay);
    }

    @Override
    protected synchronized void rightUp() {
        rightPressed = false;
        cancel(rightTask);
        this.rightTask = null;
    }

    private static void cancel(TimerTask... tasks) {
        for (TimerTask task : tasks) {
            if (task != null) {
                task.cancel();
            }
        }
    }

    private void run(TimerTask task, long delay) {
        timer.schedule(task, 0, delay);
    }

    protected abstract void left();
    protected abstract void right();

    private class LeftKeyPress extends TimerTask {
        @Override
        public void run() {
            left();
        }
    }

    private class RightKeyPress extends TimerTask {
        @Override
        public void run() {
            right();
        }
    }

}
