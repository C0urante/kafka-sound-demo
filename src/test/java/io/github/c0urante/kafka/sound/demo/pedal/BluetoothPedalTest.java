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

import io.github.c0urante.kafka.sound.demo.InteractiveTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Category(InteractiveTest.class)
public class BluetoothPedalTest {

    @Test
    public void testHiddenWindow() throws Exception {
        TestPedal pedal = new TestPedal() {
            @Override
            protected void leftDown() {
                System.out.println("Left down");
            }

            @Override
            protected void leftUp() {
                System.out.println("Left up");
            }

            @Override
            protected void rightDown() {
                System.out.println("Right down");
            }

            @Override
            protected void rightUp() {
                System.out.println("Right up");
            }
        };

        pedal.start();
        pedal.await();
    }

    @Test
    public void testVisibleWindow() throws Exception {
        JTextPane text = new JTextPane();
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 100);
        text.setFont(font);

        text.setEnabled(false);
        text.setDisabledTextColor(Color.BLACK);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(text, BorderLayout.CENTER);
        frame.setSize(1000, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        TestPedal pedal = new TestPedal() {
            private final AtomicInteger consecutiveEvents = new AtomicInteger();
            @Override
            protected void leftDown() {
                int i = consecutiveEvents.incrementAndGet();
                text.setText("Left down (" + i + ")");
            }

            @Override
            protected void leftUp() {
                consecutiveEvents.set(0);
                text.setText("");
            }

            @Override
            protected void rightDown() {
                int i = consecutiveEvents.incrementAndGet();
                text.setText("Right down (" + i + ")");
            }

            @Override
            protected void rightUp() {
                consecutiveEvents.set(0);
                text.setText("");
            }
        };

        pedal.start(frame);
        pedal.await();
    }

    private static abstract class TestPedal extends BluetoothPedal {

        private final CountDownLatch closed;

        public TestPedal() {
            this.closed = new CountDownLatch(1);
        }

        @Override
        public void start(JFrame frame) {
            super.start(frame);
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowClosed(WindowEvent e) {
                    closed.countDown();
                }

                @Override
                public void windowOpened(WindowEvent e) {
                }
                @Override
                public void windowClosing(WindowEvent e) {
                }
                @Override
                public void windowIconified(WindowEvent e) {
                }
                @Override
                public void windowDeiconified(WindowEvent e) {
                }
                @Override
                public void windowActivated(WindowEvent e) {
                }
                @Override
                public void windowDeactivated(WindowEvent e) {
                }
            });
        }

        public void await() throws InterruptedException {
            this.closed.await();
        }
    }

}
