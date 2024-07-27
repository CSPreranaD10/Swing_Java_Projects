package com;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter {

	private String extension;
	private String description;

	public MyFileFilter() {
		setExtension(null);
		setDescription(null);
	}

	public MyFileFilter(final String ext, final String desc) {
		setExtension(ext);
		setDescription(desc);
	}

	public void setExtension(String ext) {
		if (ext == null) {
			extension = null;
			return;
		}

		extension = new String(ext).toLowerCase();
		if (!ext.startsWith(".")) {
			extension = "." + extension;
		}
	}

	public void setDescription(String description) {
		if (description == null) {
			this.description = new String("All Files(*.*)");
		} else {
			this.description = new String(description);
		}
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean accept(File f) {
		final String filename = f.getName();
		if (f.isDirectory() || extension == null || filename.toUpperCase().endsWith(extension.toUpperCase())) {
			return true;
		}
		return false;
	}

}
