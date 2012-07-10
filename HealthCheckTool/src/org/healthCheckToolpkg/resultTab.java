package org.healthCheckToolpkg;

/**
 * Created with IntelliJ IDEA.
 * User: azvolskiy
 * Date: 7/10/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class resultTab extends JPanel /* implements ActionListener */ {
    private JTextArea textArea = new JTextArea();

    public resultTab(String text) {
        textArea.setText(text);
        add(textArea);
    }

}