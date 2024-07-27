package com;

import java.awt.*;

import javax.swing.*;

public class LookAndFeelMenu {

	public static void createLookAndFeelMenuIem(JMenu jMenu, Component component) {
		final UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();

		JRadioButtonMenuItem rbmi[] = new JRadioButtonMenuItem[infos.length];
		ButtonGroup bGroup = new ButtonGroup();
		JMenu temp = new JMenu("Change Look and Feel");
		temp.setMnemonic('C');
		for (int i = 0; i < infos.length; i++) {
			rbmi[i] = new JRadioButtonMenuItem(infos[i].getName());
			rbmi[i].setMnemonic(infos[i].getName().charAt(0));
			temp.add(rbmi[i]);
			bGroup.add(rbmi[i]);
			rbmi[i].addActionListener(new LookAndFeelMenuListener(infos[i].getClassName(), component));
		}
		rbmi[0].setSelected(true);
		jMenu.add(temp);
	}
}
