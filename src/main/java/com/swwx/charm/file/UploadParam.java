package com.swwx.charm.file;

import java.io.Serializable;

/**
 * 上传下载文件传输对象
 * @author whl
 *
 */
public class UploadParam implements Serializable{

	private static final long serialVersionUID = 3980076904499581368L;

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 文件描述
	 */
	private String fileDesc;

	/**
	 * 文件流
	 */
	private byte[] input;
	
	/**
	 * 文件大小
	 */
	private long fileLength;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public byte[] getInput() {
		return input;
	}

	public void setInput(byte[] input) {
		this.input = input;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

}
