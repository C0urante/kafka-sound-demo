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

import java.util.function.DoubleUnaryOperator;

public class AdjustableDouble {

    private final double min;
    private final double max;

    private volatile double currentValue;
    private double goalValue;
    private double delta;

    public AdjustableDouble(double min, double max, double initialValue) {
        if (min >= max)
            throw new IllegalArgumentException("Invalid min/max " + min + "/" + max + "; max must be greater");

        this.min = min;
        this.max = max;

        setNow(initialValue);
    }

    public synchronized void setNow(double value) {
        currentValue = value;
        goalValue = value;
        delta = 0;
    }

    public synchronized double adjustNow(DoubleUnaryOperator adjustment) {
        currentValue = boundValue(adjustment.applyAsDouble(goalValue));
        goalValue = currentValue;
        delta = 0;

        return currentValue;
    }

    public synchronized double setDelayed(double value, int delay) {
        checkDelay(delay);

        this.goalValue = boundValue(value);
        this.delta = (goalValue - currentValue) / delay;

        return goalValue;
    }

    public synchronized double adjustDelayed(DoubleUnaryOperator adjustment, int delay) {
        double proposedValue = adjustment.applyAsDouble(goalValue);
        return setDelayed(proposedValue, delay);
    }

    public synchronized double current() {
        return currentValue;
    }

    public synchronized double next() {
        if (delta != 0) {
            currentValue += delta;
            double remaining = Math.abs(currentValue - goalValue);
            if (Math.abs(remaining) < Math.abs(delta)) {
                currentValue = goalValue;
                goalValue = currentValue;
                delta = 0;
            }
        }
        return currentValue;
    }

    private double boundValue(double value) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }
    }

    private static void checkDelay(int delay) {
        if (delay <= 0)
            throw new IllegalArgumentException("Invalid delay " + delay + ": must be positive");
    }

}
