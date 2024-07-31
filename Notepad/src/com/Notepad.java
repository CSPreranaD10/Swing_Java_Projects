package com;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.*;

public class Notepad implements ActionListener, MenuConstants {

	JFrame frame;
	JTextArea textArea;
	JLabel statusBar;

	private String fileName = "Untitled";
	private boolean saved = true;
	String applicationName = "Notepad";

	JMenuItem cutItem, copyItem, deleteItem, findItem, findNextItem, replaceItem, gotoItem, selectAllItem;
	JColorChooser bColorChooser = null;
	JColorChooser fColorChooser = null;
	JDialog backgroundDialog = null;
	JDialog foregroundDialog = null;

	FileOperation fileHandler;
	FindDialog findReplaceDialog = null;
	FontChooser fontDialog = null;

	Notepad() {
		frame = new JFrame(fileName + " - " + applicationName);
		textArea = new JTextArea(30, 60);
		statusBar = new JLabel("||                 Ln 1, Col 1   ", JLabel.RIGHT);

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("src/icon/notepad_icon.jpg"));
		frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
		frame.add(statusBar, BorderLayout.SOUTH);
		frame.add(new JLabel(" "), BorderLayout.EAST);
		frame.add(new JLabel(" "), BorderLayout.WEST);

		createMenuBar(frame);
		frame.pack();
		frame.setLocation(100, 50);
		frame.setVisible(true);
		frame.setLocation(150, 50);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		fileHandler = new FileOperation(this);

		textArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				int lineNumber = 0, column = 0, pos = 0;
				try {
					pos = textArea.getCaretPosition();
					lineNumber = textArea.getLineOfOffset(pos);
					column = pos - textArea.getLineStartOffset(lineNumber);
				} catch (Exception e2) {
				}
				if (textArea.getText().length() == 0) {
					lineNumber = 0;
					column = 0;
				}
				statusBar.setText("||              Ln " + (lineNumber + 1) + ",Col " + (column + 1));
			}
		});

		DocumentListener myListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				fileHandler.saved = false;
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				fileHandler.saved = false;
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				fileHandler.saved = false;
			}
		};

		textArea.getDocument().addDocumentListener(myListener);

		WindowListener frameClose = new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (fileHandler.confirmSave())
					System.exit(0);
			}
		};
		frame.addWindowListener(frameClose);
	}

	/*
	 * It creates a JMenu with name s, mnemonics key, in the menubar toMenubar it
	 * returns the Jmenu object temp
	 */
	JMenu createMenu(String s, int key, JMenuBar toMenuBar) {
		JMenu temp = new JMenu(s);
		temp.setMnemonic(key);
		toMenuBar.add(temp);
		return temp;
	}

	JMenuItem createMenuItem(String s, int key, JMenu toMenu, int aclKey, ActionListener al) {
		JMenuItem temp = new JMenuItem(s, key);
		temp.addActionListener(al);
		temp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
		toMenu.add(temp);
		return temp;
	}

	JMenuItem createMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
		JMenuItem temp = new JMenuItem(s, key);
		temp.addActionListener(al);
		toMenu.add(temp);
		return temp;
	}

	JCheckBoxMenuItem createCheckBoxMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
		JCheckBoxMenuItem temp = new JCheckBoxMenuItem(s);
		temp.setMnemonic(key);
		temp.addActionListener(al);
		temp.setSelected(false);
		toMenu.add(temp);
		return temp;
	}

	void createMenuBar(JFrame f) {
		JMenuBar mb = new JMenuBar();
		JMenuItem temp;

		JMenu fileMenu = createMenu(fileText, KeyEvent.VK_F, mb);
		JMenu editMenu = createMenu(editText, KeyEvent.VK_E, mb);
		JMenu formatMenu = createMenu(formatText, KeyEvent.VK_O, mb);
		JMenu viewMenu = createMenu(viewText, KeyEvent.VK_V, mb);
		JMenu helpMenu = createMenu(helpText, KeyEvent.VK_H, mb);

		createMenuItem(fileNew, KeyEvent.VK_N, fileMenu, KeyEvent.VK_N, this);
		createMenuItem(fileOpen, KeyEvent.VK_O, fileMenu, KeyEvent.VK_O, this);
		createMenuItem(fileSave, KeyEvent.VK_S, fileMenu, KeyEvent.VK_S, this);
		createMenuItem(fileSaveAs, KeyEvent.VK_A, fileMenu, this);
		fileMenu.addSeparator();
		temp = createMenuItem(filePageSetup, KeyEvent.VK_U, fileMenu, this);
		temp.setEnabled(false);
		createMenuItem(filePrint, KeyEvent.VK_P, fileMenu, KeyEvent.VK_P, this);
		fileMenu.addSeparator();
		createMenuItem(fileExit, KeyEvent.VK_ESCAPE, fileMenu, this);

		temp = createMenuItem(editUndo, KeyEvent.VK_U, editMenu, KeyEvent.VK_Z, this);
		temp.setEnabled(false);
		editMenu.addSeparator();
		cutItem = createMenuItem(editCut, KeyEvent.VK_T, editMenu, KeyEvent.VK_X, this);
		copyItem = createMenuItem(editCopy, KeyEvent.VK_C, editMenu, KeyEvent.VK_C, this);
		createMenuItem(editPaste, KeyEvent.VK_P, editMenu, KeyEvent.VK_V, this);
		deleteItem = createMenuItem(editDelete, KeyEvent.VK_L, editMenu, this);
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		editMenu.addSeparator();
		findItem = createMenuItem(editFind, KeyEvent.VK_F, editMenu, KeyEvent.VK_F, this);
		findNextItem = createMenuItem(editFindNext, KeyEvent.VK_N, editMenu, this);
		findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		replaceItem = createMenuItem(editReplace, KeyEvent.VK_R, editMenu, KeyEvent.VK_H, this);
		gotoItem = createMenuItem(editGoTo, KeyEvent.VK_G, editMenu, KeyEvent.VK_G, this);
		editMenu.addSeparator();
		selectAllItem = createMenuItem(editSelectAll, KeyEvent.VK_A, editMenu, KeyEvent.VK_A, this);
		createMenuItem(editTimeDate, KeyEvent.VK_D, editMenu, this)
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

		createCheckBoxMenuItem(formatWordWrap, KeyEvent.VK_W, formatMenu, this);
		createMenuItem(formatFont, KeyEvent.VK_F, formatMenu, this);
		formatMenu.addSeparator();
		createMenuItem(formatForeground, KeyEvent.VK_T, formatMenu, this);
		createMenuItem(formatBackground, KeyEvent.VK_P, formatMenu, this);

		createCheckBoxMenuItem(viewStatusBar, KeyEvent.VK_S, viewMenu, this).setSelected(true);

		LookAndFeelMenu.createLookAndFeelMenuIem(viewMenu, this.frame);

		temp = createMenuItem(helpHelpTopic, KeyEvent.VK_H, helpMenu, this);
		temp.setEnabled(false);
		helpMenu.addSeparator();
		createMenuItem(helpAbout, KeyEvent.VK_A, helpMenu, this);

		MenuListener editMenuListener = new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {

				if (Notepad.this.textArea.getText().length() == 0) {
					findItem.setEnabled(false);
					findNextItem.setEnabled(false);
					replaceItem.setEnabled(false);
					selectAllItem.setEnabled(false);
					gotoItem.setEnabled(false);
				} else {
					findItem.setEnabled(true);
					findNextItem.setEnabled(true);
					replaceItem.setEnabled(true);
					selectAllItem.setEnabled(true);
					gotoItem.setEnabled(true);
				}

				if (Notepad.this.textArea.getSelectionStart() == textArea.getSelectionEnd()) {
					cutItem.setEnabled(false);
					copyItem.setEnabled(false);
					deleteItem.setEnabled(false);
				} else {
					cutItem.setEnabled(true);
					copyItem.setEnabled(true);
					deleteItem.setEnabled(true);
				}
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuCanceled(MenuEvent e) {
			}
		};
		editMenu.addMenuListener(editMenuListener);
		frame.setJMenuBar(mb);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String cmdText = event.getActionCommand();

		if (cmdText.equals(fileNew))
			fileHandler.newFile();

		else if (cmdText.equals(fileOpen))
			fileHandler.openFile();

		else if (cmdText.equals(fileSave))
			fileHandler.saveThisFile();

		else if (cmdText.equals(fileSaveAs))
			fileHandler.saveAsFile();

		else if (cmdText.equals(fileExit)) {
			if (fileHandler.confirmSave())
				System.exit(0);

		} else if (cmdText.equals(filePrint))
			JOptionPane.showMessageDialog(Notepad.this.frame, "Check Your Printer", "Error Printing",
					JOptionPane.INFORMATION_MESSAGE);

		else if (cmdText.equals(editCut))
			textArea.cut();
		else if (cmdText.equals(editCopy))
			textArea.copy();
		else if (cmdText.equals(editPaste))
			textArea.paste();
		else if (cmdText.equals(editDelete))
			textArea.replaceSelection("");
		else if (cmdText.equals(editFind)) {
			if (Notepad.this.textArea.getText().length() == 0)
				return;

			if (findReplaceDialog == null)
				findReplaceDialog = new FindDialog(Notepad.this.textArea);
			findReplaceDialog.showDialog(Notepad.this.frame, true);

		} else if (cmdText.equals(editFindNext)) {
			if (Notepad.this.textArea.getText().length() == 0)
				return;

			if (findReplaceDialog == null)
				statusBar.setText("Nothing to search for, use Find Option of Edit Menu First !!!!");
			else
				findReplaceDialog.findNextWithSelection();

		} else if (cmdText.equals(editReplace)) {
			if (Notepad.this.textArea.getText().length() == 0)
				return;
			if (findReplaceDialog == null)
				findReplaceDialog = new FindDialog(Notepad.this.textArea);
			findReplaceDialog.showDialog(Notepad.this.frame, false);

		} else if (cmdText.equals(editGoTo)) {
			if (Notepad.this.textArea.getText().length() == 0)
				return;
			goTo();

		} else if (cmdText.equals(editSelectAll))
			textArea.selectAll();

		else if (cmdText.equals(editTimeDate))
			textArea.insert(new Date().toString(), textArea.getSelectionStart());

		else if (cmdText.equals(formatWordWrap)) {
			JCheckBoxMenuItem temp = (JCheckBoxMenuItem) event.getSource();
			textArea.setLineWrap(temp.isSelected());

		} else if (cmdText.equals(formatFont)) {
			if (fontDialog == null)
				fontDialog = new FontChooser(textArea.getFont());

			if (fontDialog.showDialog(Notepad.this.frame, "Choose a font"))
				Notepad.this.textArea.setFont(fontDialog.createFont());

		} else if (cmdText.equals(formatForeground))
			showForegroundColorDialog();

		else if (cmdText.equals(formatBackground))
			showBackgroungColorDialog();

		else if (cmdText.equals(viewStatusBar)) {
			JCheckBoxMenuItem temp = (JCheckBoxMenuItem) event.getSource();
			statusBar.setVisible(temp.isSelected());

		} else if (cmdText.equals(helpAbout)) {
			JOptionPane.showMessageDialog(Notepad.this.frame, aboutText, "about", JOptionPane.INFORMATION_MESSAGE);

		} else {
			statusBar.setText("this" + cmdText + "command is yet to be implemented");
		}
	}

	void showBackgroungColorDialog() {
		if (bColorChooser == null) {
			bColorChooser = new JColorChooser();
		}
		if (backgroundDialog == null) {
			backgroundDialog = JColorChooser.createDialog(Notepad.this.frame, formatBackground, false, bColorChooser,
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Notepad.this.textArea.setBackground(bColorChooser.getColor());
						}
					}, null);

		}
		backgroundDialog.setVisible(true);
	}

	void showForegroundColorDialog() {
		if (fColorChooser == null)
			fColorChooser = new JColorChooser();

		if (foregroundDialog == null) {
			foregroundDialog = JColorChooser.createDialog(Notepad.this.frame, formatForeground, false, fColorChooser,
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							Notepad.this.textArea.setForeground(fColorChooser.getColor());
						}
					}, null);
		}
		foregroundDialog.setVisible(true);
	}

	void goTo() {
		int lineNumber = 0;
		try {
			lineNumber = textArea.getLineOfOffset(textArea.getCaretPosition()) + 1;
			String tempStr = JOptionPane.showInputDialog(frame, "Enter Line Number : ", "" + lineNumber);
			if (tempStr == null) {
				return;
			}
			lineNumber = Integer.parseInt(tempStr);
			textArea.setCaretPosition(textArea.getLineStartOffset(lineNumber - 1));
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		new Notepad();
	}
}
