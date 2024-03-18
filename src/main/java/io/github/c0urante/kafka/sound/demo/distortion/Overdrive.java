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

public class Overdrive extends NormalizedClippingAlgorithm {

    @Override
    public String name() {
        return "Overdrive";
    }

    @Override
    public double clip(double normalized) {
        double absResult;
        if (normalized <= 1.0 / 3) {
            absResult = 2 * normalized;
        } else if (normalized <= 2.0 / 3) {
            double toSquare = 2 - (3 * normalized);
            absResult = (3 - (toSquare * toSquare)) / 3;
        } else {
            absResult = 1;
        }
        return SampleUtils.sign(normalized) * absResult;
    }

}
