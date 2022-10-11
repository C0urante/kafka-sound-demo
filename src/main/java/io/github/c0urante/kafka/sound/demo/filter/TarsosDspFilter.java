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
package io.github.c0urante.kafka.sound.demo.filter;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import io.github.c0urante.kafka.sound.demo.util.AudioFormats;
import io.github.c0urante.kafka.sound.demo.util.SampleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TarsosDspFilter implements FilterAlgorithm {

    private final AudioProcessor tarsosProcessor;
    private final int windowSize;
    private final List<Short> bufferedSamples;

    protected TarsosDspFilter(
            AudioProcessor tarsosProcessor,
            int windowSize
    ) {
        this.tarsosProcessor = tarsosProcessor;
        this.windowSize = windowSize;
        this.bufferedSamples = new ArrayList<>(windowSize);
    }

    @Override
    public List<Short> filter(List<Short> samples) {
        List<Short> result = new ArrayList<>();
        int remainingSamples = windowSize - bufferedSamples.size();

        int i;
        if (samples.size() >= remainingSamples) {
            i = remainingSamples;
            bufferedSamples.addAll(samples.subList(0, remainingSamples));
            result.addAll(emptyBuffer());
        } else {
            i = 0;
        }

        while (samples.size() - i >= windowSize) {
            bufferedSamples.addAll(samples.subList(i, i + windowSize));
            result.addAll(emptyBuffer());
            i += windowSize;
        }

        if (i < samples.size()) {
            bufferedSamples.addAll(samples.subList(i, samples.size()));
        }

        return result;
    }

    private List<Short> emptyBuffer() {
        float[] convertedSamples = SampleUtils.toFloats(bufferedSamples);
        bufferedSamples.clear();
        AudioEvent audioEvent = new AudioEvent(AudioFormats.TARSOS_FORMAT);
        audioEvent.setFloatBuffer(convertedSamples);
        // The processor will mutate our converted samples array
        tarsosProcessor.process(audioEvent);
        return SampleUtils.fromFloats(convertedSamples);
    }

}
