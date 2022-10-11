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
import java.util.List;

public class SampleUtils {

    public static short coerceToSample(double modifiedSample) {
        if (modifiedSample > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        } else if (modifiedSample < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        } else {
            return (short) modifiedSample;
        }
    }

    public static short coerceToSample(long modifiedSample) {
        if (modifiedSample > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        } else if (modifiedSample < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        } else {
            return (short) modifiedSample;
        }
    }

    public static float[] toFloats(List<Short> shortSamples) {
        float[] result = new float[shortSamples.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = shortSamples.get(i);
        return result;
    }

    public static List<Short> fromFloats(float[] floatSamples) {
        List<Short> result = new ArrayList<>();
        for (float floatSample : floatSamples)
            result.add((short) floatSample);
        return result;
    }

    public static double normalize(short sample) {
        return ((double) sample) / (Short.MAX_VALUE + 1);
    }

    public static short denormalize(double sample) {
        return (short) (sample * (Short.MAX_VALUE + 1));
    }

    public static short sign(short s) {
        return s >= 0 ? (short) 1 : (short) -1;
    }

    public static double sign(double d) {
        return d >= 0 ? 1 : -1;
    }

    public static short abs(short sample) {
        // Edge case that would otherwise break Math::abs
        sample = sample == Short.MIN_VALUE ? Short.MIN_VALUE + 1 : sample;
        return (short) Math.abs(sample);
    }

    public static List<Short> decodeSamples(byte[] encodedSamples) {
        if (encodedSamples.length % 2 != 0)
            throw new IllegalArgumentException("Encoded sample array must have exactly two bytes per sample");

        List<Short> result = new ArrayList<>(encodedSamples.length / 2);
        for (int i = 0; i < encodedSamples.length - 1; i += 2) {
            byte littleByte = encodedSamples[i];
            byte bigByte = encodedSamples[i + 1];
            short sample = (short) ((littleByte & 0xFF) | (bigByte << 8));
            result.add(sample);
        }
        return result;
    }

    public static byte[] encodeSamples(List<Short> samples) {
        byte[] result = new byte[samples.size() * 2];
        for (int i = 0; i < samples.size(); i+= 1) {
            short sample = samples.get(i);
            byte littleByte = (byte) (sample & 0xFF);
            byte bigByte = (byte) ((sample >> 8) & 0xFF);
            result[i * 2] = littleByte;
            result[(i * 2) + 1] = bigByte;
        }
        return result;
    }

    public static int msToSamples(int ms) {
        return ms * AudioFormats.SAMPLE_RATE / 1000;
    }

}
