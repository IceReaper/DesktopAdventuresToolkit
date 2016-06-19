package net.eiveo.original.sections.todo.determine_values;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class IZX4 {
	private short unk;
	
	public static IZX4 read(SmartByteBuffer data) throws Exception {
		IZX4 instance = new IZX4();

		int length = data.getInt();
		int lengthStart = data.position();
		
		instance.unk = data.getShort(); // TODO mostly 1, sometimes 0

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("IZX4");
		outputStream.sectionStart();

		outputStream.write(this.unk);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
