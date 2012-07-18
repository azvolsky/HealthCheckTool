package org.healthchecktool.ui.mainform;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.healthchecktool.ui.tabs.connectionTab;
import org.healthchecktool.util.config.config;

public class mainForm {
		private JTabbedPane jTabbedPane;
		private JFrame jFrame;
		private config conf;
		
	public mainForm(config conf) {
		this.conf = conf;
		jTabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.WRAP_TAB_LAYOUT);
		jTabbedPane.add("Connections",new connectionTab(jTabbedPane,conf));
		jTabbedPane.add("Configurations",new JPanel());
		jFrame = new JFrame("HealthCheckTool");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = jFrame.getContentPane();
		container.add(jTabbedPane);
		jFrame.setSize(550,450);
		jFrame.pack( );
		jFrame.setLocation(200,200);
	}
	public void showFarme() {
		jFrame.setVisible(true);
	}
}

