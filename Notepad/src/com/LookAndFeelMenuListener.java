package com;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LookAndFeelMenuListener implements ActionListener {

	String classname;
	Component jf;

	LookAndFeelMenuListener(String cname, Component jf) {
		this.jf = jf;
		classname = new String(cname);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			UIManager.setLookAndFeel(classname);
			SwingUtilities.updateComponentTreeUI(jf);
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}

}
