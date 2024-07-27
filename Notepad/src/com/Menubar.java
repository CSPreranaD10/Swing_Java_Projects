package com;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.Component;

public class Menubar extends JFrame implements ActionListener {

	public Menubar() {
		setTitle("Notepad");
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon/notepad_icon.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		textArea.setBounds(0, 0, 1361, 900);
		getContentPane().add(textArea);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setAutoscrolls(true);
		scrollBar.setBounds(521, 0, 17, 316);
		getContentPane().add(scrollBar);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		fileMenu.setForeground(new Color(0, 0, 0));
		fileMenu.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
		fileMenu.setBackground(SystemColor.window);
		menuBar.add(fileMenu);
		
		JMenuItem newitem = new JMenuItem("New");
		newitem.setMnemonic(KeyEvent.VK_N);
		newitem.addActionListener(this);
		fileMenu.add(newitem);
		
		JMenuItem newWin = new JMenuItem("New Window");
		newWin.setMnemonic(KeyEvent.VK_N);  //,ActionEvent.CTRL_MASK
		fileMenu.add(newWin);
		
		JMenuItem openitem = new JMenuItem("Open");
		openitem.setMnemonic(KeyEvent.VK_O);
		fileMenu.add(openitem);
		
		JMenuItem saveitem = new JMenuItem("Save");
		saveitem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(saveitem);
		
		JMenuItem saveasitem = new JMenuItem("Save As");
		saveasitem.setMnemonic(KeyEvent.VK_S);
		fileMenu.add(saveasitem);
		
		JMenuItem printitem = new JMenuItem("Print");
		printitem.setMnemonic(KeyEvent.VK_P);
		fileMenu.add(printitem);
		
		JMenuItem setupitem = new JMenuItem("Print Setup");
		fileMenu.add(setupitem);
		
		JMenuItem exititem = new JMenuItem("Exit");
		exititem.setMnemonic(KeyEvent.VK_ESCAPE);
		fileMenu.add(exititem);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(editMenu);
		
		JMenuItem undoitem = new JMenuItem("Undo");
		undoitem.setMnemonic(KeyEvent.VK_Z);
		editMenu.add(undoitem);
		
		JMenuItem cutitem = new JMenuItem("Cut");
		cutitem.setMnemonic(KeyEvent.VK_X);
		editMenu.add(cutitem);
		
		JMenuItem copyitem = new JMenuItem("copy");
		copyitem.setMnemonic(KeyEvent.VK_C);
		editMenu.add(copyitem);
		
		JMenuItem pasteitem = new JMenuItem("Paste");
		pasteitem.setMnemonic(KeyEvent.VK_V);
		editMenu.add(pasteitem);
		
		JMenuItem selectallitem = new JMenuItem("Select All");
		selectallitem.setMnemonic(KeyEvent.VK_A);
		editMenu.add(selectallitem);
		
		JMenuItem finditem = new JMenuItem("Find");
		finditem.setMnemonic(KeyEvent.VK_F);
		editMenu.add(finditem);
		
		JMenuItem replaceitem = new JMenuItem("Replace");
		editMenu.add(replaceitem);
		
		JMenuItem timeitem = new JMenuItem("Time / Date");
		editMenu.add(timeitem);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("New menu item");
		editMenu.add(mntmNewMenuItem_8);
		
		JMenu formatMenu = new JMenu("Format");
		menuBar.add(formatMenu);
		
		JCheckBoxMenuItem wordwrapitem = new JCheckBoxMenuItem("Word Wrap");
		formatMenu.add(wordwrapitem);
		
		JMenuItem fontitem = new JMenuItem("Font");
		formatMenu.add(fontitem);
		
		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		JMenuItem zoominItem = new JMenuItem("Zoom in");
		viewMenu.add(zoominItem);
		
		JMenuItem zoomoutItem = new JMenuItem("Zoom Out");
		viewMenu.add(zoomoutItem);
		
		JMenuItem defaultzoomItem = new JMenuItem("Default Zoom");
		viewMenu.add(defaultzoomItem);
		
		JMenuItem statusbarItem = new JMenuItem("Status bar");
		viewMenu.add(statusbarItem);
		
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);
		
		
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new Menubar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {


		if (e.getSource() == "New") {
			
		}
	}
}
