package com;

import java.awt.event.KeyEvent;
import java.io.*;

import javax.swing.*;

public class FileOperation {

	Notepad ntpd;

	boolean saved;
	boolean newFileFlag;

	String fileName;
	String applicationTitle = "Notepad";

	File fileRef;
	JFileChooser chooser;

	// setter and getter methods
	public boolean isSave() {
		return saved;
	}

	public void setSave(boolean saved) {
		this.saved = saved;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// Parameterised Constructor

	FileOperation(Notepad ntpd) {
		this.ntpd = ntpd;

		saved = true;
		newFileFlag = true;
		fileName = new String("Untitled");
		fileRef = new File(fileName);
		this.ntpd.frame.setTitle(fileName + " - " + applicationTitle);

		chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new MyFileFilter(".java", "Java Source Files(*.java)"));
		chooser.addChoosableFileFilter(new MyFileFilter(".txt", "Text Files(*.txt)"));
		chooser.setCurrentDirectory(new File("."));

	}
	// End of Parameterised contructor

	// newFile() Method for creating a new file
	void newFile() {
		if (!confirmSave())
			return;

		this.ntpd.textArea.setText("");
		fileName = new String("Untitled");
		fileRef = new File(fileName);
		saved = true;
		newFileFlag = true;
		this.ntpd.frame.setTitle(fileName + " - " + applicationTitle);
	}
	// End of newFile() method.

	// openFile() method to open a file
	void openFile() {
		if (!confirmSave())
			return;
		chooser.setDialogTitle("Open File...");
		chooser.setApproveButtonText("Open this");
		chooser.setApproveButtonMnemonic(KeyEvent.VK_O);
		chooser.setApproveButtonToolTipText("Click me to open the selected file.!");

		File temp = null;
		do {
			if (chooser.showOpenDialog(this.ntpd.frame) != JFileChooser.APPROVE_OPTION)
				return;
			temp = chooser.getSelectedFile();

			if (temp.exists())
				break;

			JOptionPane.showMessageDialog(this.ntpd.frame,
					"<html>" + temp.getName() + "<br>file not found.<br>"
							+ "Please verify the correct file name was given.<html>",
					"Open", JOptionPane.INFORMATION_MESSAGE);

		} while (true);

		this.ntpd.textArea.setText("");

		if (!openFile(temp)) {
			fileName = "Untitled";
			saved = true;
			this.ntpd.frame.setTitle(fileName + " - " + applicationTitle);
		}
		if (!temp.canWrite())
			newFileFlag = true;

	}
	// End ofopenFile() method

	// openFile() parameterized Method for opening a existing file from system
	boolean openFile(File temp) {
		FileInputStream fis = null;
		BufferedReader bf = null;

		try {
			fis = new FileInputStream(temp);
			bf = new BufferedReader(new InputStreamReader(fis));
			String str = " ";
			while (str != null) {
				str = bf.readLine();
				if (str == null)
					break;
				this.ntpd.textArea.append(str + "\n");

			}
		} catch (IOException ioException) {
			updateStatus(temp, false);
			return false;
		} finally {
			try {
				bf.close();
				fis.close();
			} catch (IOException e) {
			}
		}
		updateStatus(temp, true);
		this.ntpd.textArea.setCaretPosition(0);
		return true;
	}
	// End of openFile() method.

	// saveThisFile() method for saving the changes made in the file.
	boolean saveThisFile() {
		if (!newFileFlag)
			return saveFile(fileRef);
		return saveAsFile();
	}
	// End of saveThisFile() method

	// saveAsFile() method for saving the file
	boolean saveAsFile() {
		File temp = null;
		chooser.setDialogTitle("Save As.....");
		chooser.setApproveButtonText("Save Now");
		chooser.setApproveButtonMnemonic(KeyEvent.VK_S);
		chooser.setApproveButtonToolTipText("Click me to save!");

		do {
			if (chooser.showSaveDialog(this.ntpd.frame) != JFileChooser.APPROVE_OPTION)
				return false;
			temp = chooser.getSelectedFile();
			if (!temp.exists())
				break;
			if (JOptionPane.showConfirmDialog(this.ntpd.frame,
					"<html>" + temp.getPath() + "already exists.<br> Do you want to replace it ? </html>", "Save As",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				break;
		} while (true);

		return saveFile(temp);
	}
	// End of saveAsFile() method

	// updateStatus() Returns the state of file if it is successfully saved/open on
	// the status bar
	void updateStatus(File temp, boolean saved) {
		if (saved) {
			this.saved = true;
			fileName = new String(temp.getName());
			if (!temp.canWrite()) {
				fileName += "(Read Only)";
				newFileFlag = true;
			}
			fileRef = temp;
			ntpd.frame.setTitle(fileName + "-" + applicationTitle);
			ntpd.statusBar.setText("File : " + temp.getPath() + " saved/opened successfully.");
			newFileFlag = false;
		} else {
			ntpd.statusBar.setText("Failed to save/open : " + temp.getPath());
		}
	}
	// End of updateStatus()

	// confirmSave Method confirms whether to save the changes or not before exiting
	// the application
	boolean confirmSave() {
		String strMsg = "<html> The text in the " + fileName + " file has been changed. <br>"
				+ "Do you want to save the changes?</html>";
		if (!saved) {
			int x = JOptionPane.showConfirmDialog(this.ntpd.frame, strMsg, applicationTitle,
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (x == JOptionPane.CANCEL_OPTION) {
				return false;
			}
			if (x == JOptionPane.YES_OPTION && !saveAsFile()) {
				return false;
			}
		}

		return true;
	}
	// End of confirmSave() method

	// saveFile() method returns true and writes the content of selected file int
	// the textarea. or else returns false
	boolean saveFile(File temp) {
		FileWriter fout = null;
		try {
			fout = new FileWriter(temp);
			fout.write(ntpd.textArea.getText());
		} catch (IOException ioe) {
			updateStatus(temp, false);
			return false;
		} finally {
			try {
				fout.close();
			} catch (IOException excp) {
			}
		}
		updateStatus(temp, true);
		return true;
	}
	// End of saveFile()

}
