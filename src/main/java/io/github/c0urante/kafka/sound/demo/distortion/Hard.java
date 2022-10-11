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
 * If the sample is above a certain threshold, replace it with that threshold.
 * If it is below the opposite of that threshold, replace it with the opposite of that threshold.
 * Otherwise, preserve the sample as-is.
 */
public class Hard implements ClippingAlgorithm {

    public static final double DEFAULT_CEILING = 0.5;

    private final short ceiling;

    public Hard() {
        this(DEFAULT_CEILING);
    }

    public Hard(double ceiling) {
        this.ceiling = (short) (Short.MAX_VALUE * ceiling);
    }

    @Override
    public String name() {
        String ceilingStr = String.format("%.2f", SampleUtils.normalize(ceiling));
        return "Hard (ceiling " + ceilingStr + ")";
    }

    @Override
    public short clip(short sample) {
        if (sample > ceiling) {
            return ceiling;
        } else if (sample < -ceiling) {
            return (short) -ceiling;
        } else {
            return sample;
        }
    }

}
