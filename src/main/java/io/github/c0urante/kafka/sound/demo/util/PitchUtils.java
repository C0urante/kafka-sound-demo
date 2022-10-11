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

public class PitchUtils {

    public static String pitch(double originalFrequency) {
        if (originalFrequency <= 0)
            throw new IllegalStateException("Invalid frequency: " + originalFrequency + "; must be positive");

        double normalizedFrequency = originalFrequency;
        while (normalizedFrequency < 220) {
            normalizedFrequency *= 2;
        }
        while (normalizedFrequency >= 440) {
            normalizedFrequency /= 2;
        }

        String[] pitches = new String[] {
                "A", "Bb", "B", "C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A"
        };

        for (int i = 0; i < pitches.length; i++) {
            // 12TET is evil but hopefully there's no Jacob Collier-esque modulations to G half-sharp major
            // or other rambunctious shit like that
            // Insert joke here about A432, baroque tuning, questionable intonation of the performer, etc.
            double pitchMax = 220 * (Math.pow(2, ((i + 0.5) / 12)));
            if (pitchMax > normalizedFrequency) {
                return pitches[i];
            }
        }

        throw new IllegalStateException("Unable to deduce pitch for frequency " + originalFrequency);
    }

}
