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
package io.github.c0urante.kafka.sound.demo.pedal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public abstract class BluetoothPedal {

    private static final Logger log = LoggerFactory.getLogger(BluetoothPedal.class);
    private static final Collection<Integer> LEFT_KEYS = Arrays.asList(
            KeyEvent.VK_UP, KeyEvent.VK_PAGE_UP, KeyEvent.VK_LEFT
    );
    private static final Collection<Integer> RIGHT_KEYS = Arrays.asList(
            KeyEvent.VK_DOWN, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_RIGHT
    );
    private static final Object LEFT_PEDAL_DOWN = "left pedal down";
    private static final Object LEFT_PEDAL_UP = "left pedal up";
    private static final Object RIGHT_PEDAL_DOWN = "right pedal down";
    private static final Object RIGHT_PEDAL_UP = "right pedal up";

    /**
     * Creates a hidden JFrame and listens to that window for keyboard input
     */
    public void start() {
        // Yeah, this is sadly the best way I could find to read keyboard input in Java
        // without using a scanner and a terminal
        // Could be worse 🥴
        JFrame hiddenFrame = new JFrame();
        hiddenFrame.setUndecorated(true);
        hiddenFrame.setOpacity(0.01f);
        hiddenFrame.setVisible(true);
        hiddenFrame.toFront();
        hiddenFrame.requestFocus();
        hiddenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        start(hiddenFrame);
    }

    /**
     * Attaches to the provided {@link JFrame} and listens to it for keyboard input
     * @param frame the provided {@link JFrame}; may not be null
     */
    public void start(JFrame frame) {
        Objects.requireNonNull(frame);

        JRootPane pain = new JRootPane();

        for (int event : LEFT_KEYS) {
            pain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(event, 0),
                    LEFT_PEDAL_DOWN
            );
            pain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(event, 0, true),
                    LEFT_PEDAL_UP
            );
        }

        for (int event : RIGHT_KEYS) {
            pain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(event, 0),
                    RIGHT_PEDAL_DOWN
            );
            pain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(event, 0, true),
                    RIGHT_PEDAL_UP
            );
        }

        pain.getActionMap().put(LEFT_PEDAL_DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Left pedal pressed");
                try {
                    leftDown();
                } catch (Throwable t) {
                    log.error("Unexpected error while handling left pedal press", t);
                    throw t;
                }
            }
        });
        pain.getActionMap().put(LEFT_PEDAL_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
               log.debug("Left pedal released");
               try {
                   leftUp();
               } catch (Throwable t) {
                   log.error("Unexpected error while handling left pedal release", t);
                   throw t;
               }
            }
        });
        pain.getActionMap().put(RIGHT_PEDAL_DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Right pedal pressed");
                try {
                    rightDown();
                } catch (Throwable t) {
                    log.error("Unexpected error while handling right pedal press", t);
                    throw t;
                }
            }
        });
        pain.getActionMap().put(RIGHT_PEDAL_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Right pedal released");
                try {
                    rightUp();
                } catch (Throwable t) {
                    log.error("Unexpected error while handling right pedal release", t);
                    throw t;
                }
            }
        });

        frame.add(pain);
    }

    protected abstract void leftDown();
    protected abstract void leftUp();
    protected abstract void rightDown();
    protected abstract void rightUp();

}
