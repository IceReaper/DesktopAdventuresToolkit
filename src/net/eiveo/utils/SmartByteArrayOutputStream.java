package net.eiveo.utils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SmartByteArrayOutputStream {
	private List<ByteArrayOutputStream> sections = new ArrayList<>();
	private ByteArrayOutputStream section;
	
	public SmartByteArrayOutputStream() {
		this.section = new ByteArrayOutputStream();
		this.sections.add(this.section);
	}

	public void sectionStart() {
		this.section = new ByteArrayOutputStream();
		this.sections.add(this.section);
	}
	
	public byte[] sectionEnd() {
		byte[] result = this.section.toByteArray();
		sections.remove(sections.size() - 1);
		this.section = sections.get(sections.size() - 1);
		return result;
	}

	public byte[] toByteArray() {
		return this.section.toByteArray();
	}

	public void write(String string) {
		for (byte byteData : string.getBytes()) {
			this.section.write(byteData);
		}
	}

	public void write(byte value) {
		this.section.write(value);
	}

	public void write(byte[] value) {
		for (byte byteData : value) {
			this.section.write(byteData);
		}
	}

	public void write(short value) {
		for (byte byteData : new byte[] {
	        (byte) value,
	        (byte)(value >> 8)
        }) {
			this.section.write(byteData);
		}
	}

	public void write(int value) {
		for (byte byteData : new byte[] {
	        (byte) value,
	        (byte)(value >> 8),
	        (byte)(value >> 16),
	        (byte)(value >> 24)
        }) {
			this.section.write(byteData);
		}
	}
}
