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
package io.github.c0urante.kafka.sound.demo.pitch;

import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchDetector;
import io.github.c0urante.kafka.sound.demo.util.PitchUtils;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TarsosDspPitchAlgorithm implements PitchAlgorithm {

    protected static final int AUDIO_BUFFER_SIZE = 1024 * 2; // TODO: Configurable

    private static final Logger log = LoggerFactory.getLogger(TarsosDspPitchAlgorithm.class);

    private final PitchDetector algorithm;
    private final FloatBuffer buffer;

    protected TarsosDspPitchAlgorithm(PitchDetector algorithm) {
        this.algorithm = algorithm;
        this.buffer = FloatBuffer.allocate(AUDIO_BUFFER_SIZE);
    }

    @Override
    public List<Pitch> pitches(byte[] encodedSamples) {
        List<Pitch> result = new ArrayList<>();

        float[] floats = SampleUtils.toFloats(SampleUtils.decodeSamples(encodedSamples));

        int offset = 0;
        if (floats.length >= buffer.remaining()) {
            offset = buffer.remaining();
            buffer.put(floats, 0, buffer.remaining());
            result.add(pitch(buffer.array()));
            buffer.clear();
        }

        while (floats.length - offset >= buffer.capacity()) {
            float[] subFloats = Arrays.copyOfRange(floats, offset, offset + buffer.capacity());
            result.add(pitch(subFloats));
            offset += buffer.capacity();
        }

        buffer.put(floats, offset, floats.length - offset);

        return result;
    }

    private Pitch pitch(float[] floats) {
        PitchDetectionResult pitchDetectionResult = algorithm.getPitch(floats);

        String pitchName;
        if (pitchDetectionResult.isPitched()) {
            pitchName = PitchUtils.pitch(pitchDetectionResult.getPitch());
            log.debug("Detected pitch {}", pitchName);
        } else {
            log.trace("No pitch detected");
            pitchName = null;
        }

        return new Pitch(pitchName, pitchDetectionResult.getProbability());
    }

}
