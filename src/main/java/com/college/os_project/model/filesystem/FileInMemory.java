package com.college.os_project.model.filesystem;

import com.college.os_project.model.memory.Block;

public class FileInMemory {
	private String name;
	private int size;
	private int indexBlock;
	private int length;
	private static byte[] contentFile = new byte[0];

	public FileInMemory(String name, byte[] content) {
		this.name = name;
		contentFile = content;
		size = contentFile.length;
	}

	public static byte[] part(int index) {
		byte[] part = new byte[Block.getSize()];
		int counter = 0;
		for (int i = index * Block.getSize(); i < contentFile.length; i++) {
			part[counter] = contentFile[i];
			if (counter == Block.getSize() - 1)
				break;
			counter++;
		}
		while (counter < Block.getSize() - 1) {
			byte[] b = " ".getBytes();
			part[counter] = b[0];
			counter++;
		}
		return part;
	}

	public int getIndexBlock() {
		return indexBlock;
	}

	public void setIndexBlock(int index) {
		this.indexBlock = index;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public byte[] getContentFile() {
		return contentFile;
	}

	public void setContentFile(byte[] contentFile) {
		FileInMemory.contentFile = contentFile;
	}
}
