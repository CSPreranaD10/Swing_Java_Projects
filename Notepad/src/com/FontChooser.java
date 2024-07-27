package com;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class FontChooser extends JPanel {

	private Font thisFont;

	private JList jFace, jStyle, jSize;
	private JButton okButton;
	private JDialog dialog;

	private boolean ok;
	JTextArea tarea;

	public FontChooser(Font withFont) {

		thisFont = withFont;

		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		jFace = new JList(fontNames);
		jFace.setSelectedIndex(0);
		jFace.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				tarea.setFont(createFont());
			}
		});

		String[] fontStyles = { "Regular", "Italic", "Bold", "Bold Italic" };
		jStyle = new JList(fontStyles);
		jStyle.setSelectedIndex(0);
		jStyle.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				tarea.setFont(createFont());
			}
		});

		String[] fontSizes = new String[30];
		for (int j = 0; j < 30; j++)
			fontSizes[j] = new String(10 + j * 2 + "");
		jSize = new JList(fontSizes);
		jSize.setSelectedIndex(0);
		jSize.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				tarea.setFont(createFont());
			}
		});

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(1, 3));
		labelPanel.add(new JLabel("Font", JLabel.CENTER));
		labelPanel.add(new JLabel("Font Style", JLabel.CENTER));
		labelPanel.add(new JLabel("Size", JLabel.CENTER));

		JPanel listPanel = new JPanel();
		listPanel.add(new JScrollPane(jFace));
		listPanel.add(new JScrollPane(jStyle));
		listPanel.add(new JScrollPane(jSize));

		okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ok = true;
				FontChooser.this.thisFont = FontChooser.this.createFont();
				dialog.setVisible(false);
			}
		});
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(new JLabel("     "));
		buttonPanel.add(cancelButton);

		tarea = new JTextArea(5, 30);
		JPanel jpTextField = new JPanel();
		jpTextField.add(new JScrollPane(tarea));

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2, 1));
		centerPanel.add(listPanel);
		centerPanel.add(jpTextField);

		setLayout(new BorderLayout());
		add(labelPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		add(new JLabel(" "), BorderLayout.EAST);
		add(new JLabel(" "), BorderLayout.WEST);

		tarea.setFont(thisFont);
		tarea.append("\n A quick brown fox jumps over the lazy dog.");
		tarea.append("\n0123456789");
		tarea.append("\n~!@#$%^&*()_+|?><\n");

	}

	public Font createFont() {
		Font font = thisFont;
		int fontstyle = Font.PLAIN;
		int x = jStyle.getSelectedIndex();

		switch (x) {
		case 0:
			fontstyle = Font.PLAIN;
			break;
		case 1:
			fontstyle = Font.ITALIC;
			break;
		case 2:
			fontstyle = Font.BOLD;
			break;

		case 3:
			fontstyle = Font.BOLD + Font.ITALIC;
			break;
		}
		int fontsize = Integer.parseInt((String) jSize.getSelectedValue());
		String fontname = (String) jFace.getSelectedValue();
		font = new Font(fontname, fontstyle, fontsize);

		return font;
	}

	public boolean showDialog(Component parent, String title) {
		ok = false;

		Frame owner = null;
		if (parent instanceof Frame)
			owner = (Frame) parent;
		else
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.setSize(400, 325);
		}

		dialog.setTitle(title);
		dialog.setVisible(true);
		return ok;
	}

}
