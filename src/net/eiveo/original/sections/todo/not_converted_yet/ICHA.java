package net.eiveo.original.sections.todo.not_converted_yet;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ICHA {
	private String name;
	private short type;
	private short subtype;
	private short[] frames = new short[24];

	public static ICHA read(SmartByteBuffer data, boolean isYoda) throws Exception {
		ICHA instance = new ICHA();
		
		int length = data.getInt();
		int lengthStart = data.position();

		int nameStart = data.position();
		instance.name = data.getString();
		data.position(nameStart + 16); // filled with trash - bug

		instance.type = data.getShort();
		instance.subtype = data.getShort();
		
		if (isYoda) {
			data.getShort(); // -1
			data.getShort(); // 0
			data.getShort(); // 0
		}
		
		for (int i = 0; i < 24; i++) {
			instance.frames[i] = data.getShort();
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) throws Exception {
		outputStream.write("ICHA");
		outputStream.sectionStart();

		outputStream.write(this.name);
		
		for (int i = this.name.length(); i < 16; i++) {
			outputStream.write((byte) 0);
		}
		
		outputStream.write(this.type);
		outputStream.write(this.subtype);
		
		if (isYoda) {
			outputStream.write((short) -1);
			outputStream.write((short) 0);
			outputStream.write((short) 0);
		}
		
		for (short frame : this.frames) {
			outputStream.write(frame);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
