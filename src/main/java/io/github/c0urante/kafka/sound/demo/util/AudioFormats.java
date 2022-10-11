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

import be.tarsos.dsp.io.TarsosDSPAudioFormat;

import javax.sound.sampled.AudioFormat;

public class AudioFormats {

    public static final TarsosDSPAudioFormat.Encoding TARSOS_ENCODING =
            TarsosDSPAudioFormat.Encoding.PCM_SIGNED;
    public static final int SAMPLE_RATE = 44100;
    public static final int SAMPLE_SIZE = 16;
    public static final int CHANNELS_COUNT = 1;
    public static final boolean SIGNED = true;
    public static final boolean BIG_ENDIAN = false;
    public static final int FRAME_SIZE = SAMPLE_SIZE * CHANNELS_COUNT / 8;

    public static final AudioFormat JAVA_SDK = new AudioFormat(
            SAMPLE_RATE,
            SAMPLE_SIZE,
            CHANNELS_COUNT,
            SIGNED,
            BIG_ENDIAN
    );

    public static TarsosDSPAudioFormat TARSOS_FORMAT = new TarsosDSPAudioFormat(
                TarsosDSPAudioFormat.Encoding.PCM_SIGNED,
                (float) SAMPLE_RATE,
                16,
                1,
                SAMPLE_SIZE * CHANNELS_COUNT / 8,
                (float) SAMPLE_RATE * FRAME_SIZE,
                BIG_ENDIAN
        );

}
