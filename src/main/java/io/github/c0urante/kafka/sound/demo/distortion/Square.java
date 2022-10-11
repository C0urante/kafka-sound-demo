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
package io.github.c0urante.kafka.sound.demo.distortion;

import io.github.c0urante.kafka.sound.demo.util.SampleUtils;

/**
 * If the sample is above a certain threshold, replace it with a constant value.
 * If it's below the opposite of that threshold, replace it with the opposite constant value.
 * Otherwise, leave it as-is.
 * Forms "square" (ish) waves.
 */
public class Square implements ClippingAlgorithm {

    public static final double DEFAULT_FLOOR = 0.01;
    public static final double DEFAULT_AMPLITUDE = 0.05;

    private final short floor;
    private final short amplitude;

    public Square() {
        this(DEFAULT_FLOOR, DEFAULT_AMPLITUDE);
    }

    public Square(double floor, double amplitude) {
        this.floor = (short) (Short.MAX_VALUE * floor);
        this.amplitude = (short) (Short.MAX_VALUE * amplitude);
    }

    @Override
    public String name() {
        String amplitudeStr = String.format("%.2f",SampleUtils.normalize(amplitude));
        return "Square clip (amplitude " + amplitudeStr + ")";
    }

    @Override
    public short clip(short sample) {
        if (Math.abs(sample) < floor) {
            // Without this check, even the smallest noise gets amplified to full volume
            // This is just a small hack to help prevent otherwise-inaudible noise from
            // becoming overpowering
            return sample;
        } else {
            return sample >= 0 ? amplitude : (short) -amplitude;
        }
    }

}
