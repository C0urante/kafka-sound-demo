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
package io.github.c0urante.kafka.sound.demo;

import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.junit.Assert.assertNotNull;

public final class AudioTestUtils {

    public static final String DEFAULT_AUDIO_FILE = "audio/cello.raw";
    public static final String OPEN_STRINGS_FILE = "audio/open_strings.raw";
    public static final String BACH_FILE = "audio/bach.raw";
    public static final String QUEEN_OPENING_FILE = "audio/queen_opening.raw";
    public static final String QUEEN_GUITAR_1_FILE = "audio/queen_guitar_1_sul_pont.raw";
    public static final String QUEEN_GUITAR_2_FILE = "audio/queen_guitar_2_sul_pont.raw";
    public static final int SAMPLE_RATE = 44100;
    public static final int SAMPLE_SIZE = 16;
    public static final int CHANNELS_COUNT = 1;
    public static final boolean SIGNED = true;
    public static final boolean BIG_ENDIAN = false;
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(
            SAMPLE_RATE,
            SAMPLE_SIZE,
            CHANNELS_COUNT,
            SIGNED,
            BIG_ENDIAN
    );

    public static void testAlgorithmOnDefaultFile(UnaryOperator<List<Short>> algorithm) {
        testAlgorithmOnFile(DEFAULT_AUDIO_FILE, algorithm);
    }

    public static void testAlgorithmOnFile(String file, UnaryOperator<List<Short>> algorithm) {
        byte[] inputEncodedSamples = readResourceFile(file);
        int emptyBytes = SAMPLE_RATE * (SAMPLE_SIZE / 8) * 4; // Four seconds of silence afterward
        byte[] testEncodedSamples = new byte[inputEncodedSamples.length + emptyBytes];
        System.arraycopy(inputEncodedSamples, 0, testEncodedSamples, 0, inputEncodedSamples.length);
        testAlgorithmOnSamples(testEncodedSamples, algorithm);
    }

    public static UnaryOperator<List<Short>> batch(UnaryOperator<Short> algorithm) {
        return samples -> {
            samples.replaceAll(algorithm);
            return samples;
        };
    }

    @Test
    public void findMaxSampleBach() {
        System.out.println(findMaxSample(BACH_FILE));
    }

    @Test
    public void findMaxSampleQueenOpening() {
        System.out.println(findMaxSample(QUEEN_OPENING_FILE));
    }

    @Test
    public void findMaxSampleQueenGuitar2() {
        System.out.println(findMaxSample(QUEEN_GUITAR_2_FILE));
    }

    private static class MaxSample {
        public final short sample;
        public final double time;

        public MaxSample(short sample, int location) {
            this.sample = sample;
            this.time = location * 1.0 / AudioFormats.SAMPLE_RATE;
        }

        @Override
        public String toString() {
            return String.format("Sample %d at time %3.5f", sample, time);
        }

    }

    public static MaxSample findMaxSample(String file) {
        byte[] inputEncodedSamples = readResourceFile(file);
        List<Short> samples = SampleUtils.decodeSamples(inputEncodedSamples);

        short max = 0;
        short min = 0;
        int maxLocation = 0;
        int minLocation = 0;

        for (int i = 0; i < samples.size(); i++) {
            short sample = samples.get(i);
            if (sample > max) {
                max = sample;
                maxLocation = i;
            }
            if (sample < min) {
                min = sample;
                minLocation = i;
            }
        }

        // Edge case that leads to overflow
        if (min == Short.MIN_VALUE) {
            return new MaxSample(Short.MAX_VALUE, minLocation);
        } else if (max >= Math.abs(min)) {
            return new MaxSample(max, maxLocation);
        } else {
            return new MaxSample(min, minLocation);
        }
    }

    private static void testAlgorithmOnSamples(byte[] inputEncodedSamples, UnaryOperator<List<Short>> algorithm) {
        List<Short> samples = SampleUtils.decodeSamples(inputEncodedSamples);
        samples = algorithm.apply(samples);
        byte[] outputEncodedSamples = SampleUtils.encodeSamples(samples);
        playSamples(outputEncodedSamples);
    }

    private static byte[] readResourceFile(String path) {
        URL resourceFile = AudioTestUtils.class.getClassLoader().getResource(path);
        assertNotNull(resourceFile);
        try {
            return Files.readAllBytes(new File(resourceFile.getPath()).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void playSamples(byte[] encodedSamples) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, AUDIO_FORMAT);
        if (!AudioSystem.isLineSupported(info)) {
            throw new IllegalArgumentException("Audio format not supported on this machine");
        }

        SourceDataLine audioOutput;
        try {
            audioOutput = (SourceDataLine) AudioSystem.getLine(info);
            audioOutput.open(AUDIO_FORMAT, 1024);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        audioOutput.start();

        int bytesWritten = 0;
        while (bytesWritten < encodedSamples.length) {
            int bytesLeft = encodedSamples.length - bytesWritten;
            bytesWritten += audioOutput.write(encodedSamples, bytesWritten, bytesLeft);
        }
    }

    private static byte[] readSamples(TargetDataLine audioInput) {
        byte[] buffer = new byte[SAMPLE_RATE * 10];
        int bytesRead;
        bytesRead = audioInput.read(buffer, 0, buffer.length);

        if (bytesRead == buffer.length) {
            return buffer;
        } else {
            byte[] reducedSizeBuffer = new byte[bytesRead];
            System.arraycopy(buffer, 0, reducedSizeBuffer, 0, bytesRead);
            return reducedSizeBuffer;
        }
    }

}
