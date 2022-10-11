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
package io.github.c0urante.kafka.sound.demo.ui;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public abstract class TextWindow {

    private final JFrame frame;
    private final JTextPane textPane;

    public TextWindow(String title) {
        this.textPane = textPane();
        this.frame = frame(title, this.textPane);
    }

    private static JTextPane textPane() {
        JTextPane result = new JTextPane();
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 100);
        result.setFont(font);

        // https://stackoverflow.com/questions/12546056/how-to-make-jtextarea-non-selectable
        result.setEnabled(false);
        result.setDisabledTextColor(Color.BLACK);

        // https://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
        StyledDocument doc = result.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, 0, center, false);

        return result;
    }

    private static JFrame frame(String title, JTextPane textPane) {
        JFrame result = new JFrame(title);
        result.setLayout(new BorderLayout());
        result.add(textPane, BorderLayout.CENTER);
        result.setSize(1500, 750);
        result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        result.setVisible(true);
        return result;
    }

    public JFrame frame() {
        return frame;
    }

    protected void updateText(String text) {
        this.textPane.setText(text);
    }

}