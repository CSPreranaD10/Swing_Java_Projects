package com;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

public class FindDialog extends JPanel implements ActionListener {

	JTextArea ta;
	public int lastIndex;
	private boolean ok;

	private TextField findWhat;
	private JTextField replaceWith;
	private JCheckBox matchCase;
	private JDialog dialog;

	JLabel replaceLabel;
	JRadioButton up, down;
	JButton findNextButton, replaceButton, replaceAllButton, cancelButton;
	JPanel direction, replaceButtonPanel;

	// parameterised constructor
	public FindDialog(JTextArea ta) {

		this.ta = ta;
		findWhat = new TextField(20);
		replaceWith = new JTextField(20);

		matchCase = new JCheckBox("Match case");

		up = new JRadioButton("Up");
		down = new JRadioButton("Down");
		down.setSelected(true);

		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(up);
		bGroup.add(down);

		direction = new JPanel();
		Border etched = BorderFactory.createEtchedBorder();
		Border titled = BorderFactory.createTitledBorder(etched, "Direction");
		direction.setBorder(titled);
		direction.setLayout(new GridLayout(1, 2));
		direction.add(up);
		direction.add(down);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 2));
		southPanel.add(matchCase);
		southPanel.add(direction);

		findNextButton = new JButton("Find Next");
		replaceButton = new JButton("Replace");
		replaceAllButton = new JButton("Replace All");
		cancelButton = new JButton("Cancel");

		replaceButtonPanel = new JPanel();
		replaceButtonPanel.setLayout(new GridLayout(4, 1));
		replaceButtonPanel.add(findNextButton);
		replaceButtonPanel.add(replaceButton);
		replaceButtonPanel.add(replaceAllButton);
		replaceButtonPanel.add(cancelButton);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(3, 2));
		textPanel.add(new JLabel("Find What "));
		textPanel.add(findWhat);
		textPanel.add(replaceLabel = new JLabel("Replace With"));
		textPanel.add(replaceWith);
		textPanel.add(new JLabel(" "));
		textPanel.add(new JLabel(" "));

		setLayout(new BorderLayout());

		add(new JLabel("              "), BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
		add(replaceButtonPanel, BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);

		setSize(200, 200);

		findNextButton.addActionListener(this);
		replaceButton.addActionListener(this);
		replaceAllButton.addActionListener(this);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		findWhat.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent event) {
				enableDisableButtons();
			}
		});

		findWhat.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				enableDisableButtons();
			}
		});

	}

	// enableDisableButtons() method disables the find, replace, replaceAll button
	// when there is no text in the textbox otherwise it enables the buttons.
	void enableDisableButtons() {
		if (findWhat.getText().length() == 0) {
			findNextButton.setEnabled(false);
			replaceAllButton.setEnabled(false);
			replaceButton.setEnabled(false);
		} else {
			findNextButton.setEnabled(true);
			replaceAllButton.setEnabled(true);
			replaceButton.setEnabled(true);
		}
	}
	// End of enableDisableButtons() method

	int findNext() {

		String stg1 = ta.getText();
		String stg2 = findWhat.getText();

		lastIndex = ta.getCaretPosition();

		int selectStart = ta.getSelectionStart();
		int selectEnd = ta.getSelectionEnd();

		if (up.isSelected()) {
			if (selectStart != selectEnd)
				lastIndex = selectEnd - stg2.length() - 1;
			if (!matchCase.isSelected())
				lastIndex = stg1.toUpperCase().lastIndexOf(stg2.toUpperCase(), lastIndex);
			else {
				lastIndex = stg1.lastIndexOf(stg2, lastIndex);
			}
		} else {
			if (selectStart != selectEnd) {
				lastIndex = selectStart + 1;
			}
			if (!matchCase.isSelected())
				lastIndex = stg1.toUpperCase().indexOf(stg2.toUpperCase(), lastIndex);
			else {
				lastIndex = stg1.indexOf(stg2, lastIndex);
			}
		}
		return lastIndex;
	}

	public void findNextWithSelection() {
		int index = findNext();
		if (index != -1) {
			ta.setSelectionStart(index);
			ta.setSelectionEnd(index + findWhat.getText().length());
		} else {
			JOptionPane.showMessageDialog(this, "Cannot find " + " \" " + findWhat.getText() + " \"", "Find",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// replaceNext() method replaces the selected text with the replaceWith
	// jtextfield text
	void replaceNext() {
		if (ta.getSelectionStart() == ta.getSelectionEnd()) {
			findNextWithSelection();
			return;
		}

		String searchText = findWhat.getText();
		String temp = ta.getSelectedText();
		if ((matchCase.isSelected() && temp.equals(searchText))
				|| (!matchCase.isSelected() && temp.equalsIgnoreCase(searchText)))

			ta.replaceSelection(replaceWith.getText());
		findNextWithSelection();
	}
	// End of replaceNext() method

	// replaceAllNext() method replaces all the occurence of the selected text with
	// the text user wants to replace
	int replaceAllNext() {
		if (up.isSelected())
			ta.setCaretPosition(ta.getText().length() - 1);
		else {
			ta.setCaretPosition(0);
		}
		int index = 0;
		int counter = 0;
		do {
			index = findNext();
			if (index == -1) {
				break;
			}
			counter++;
			ta.replaceRange(replaceWith.getText(), index, index + findWhat.getText().length());
		} while (index != -1);

		return counter;
	}
	// End of replaceAllNext() method

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == findNextButton)
			findNextWithSelection();
		else if (e.getSource() == replaceButton)
			replaceNext();
		else if (e.getSource() == replaceAllButton) {
			JOptionPane.showMessageDialog(null, "Total replacements made= " + replaceAllNext());
		}
	}

	public boolean showDialog(Component parent, boolean isFind) {

		Frame owner = null;
		if (parent instanceof Frame) {
			owner = (Frame) parent;
		} else {
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
		}
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, false);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(findNextButton);
		}

		if (findWhat.getText().length() == 0)
			findNextButton.setEnabled(false);
		else {
			findNextButton.setEnabled(true);
		}

		replaceButton.setVisible(false);
		replaceAllButton.setVisible(false);
		replaceWith.setVisible(false);
		replaceLabel.setVisible(false);

		if (isFind) {
			dialog.setSize(460, 180);
			dialog.setTitle("Find");
		} else {
			replaceButton.setVisible(true);
			replaceAllButton.setVisible(true);
			replaceWith.setVisible(true);
			replaceLabel.setVisible(true);

			dialog.setSize(450, 200);
			dialog.setTitle("Replace");
		}
		dialog.setVisible(true);
		return ok;
	}

}
