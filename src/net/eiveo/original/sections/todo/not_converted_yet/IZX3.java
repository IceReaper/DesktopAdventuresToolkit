package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class IZX3 {
	private List<Short> tileId = new ArrayList<>();
	
	public static IZX3 read(SmartByteBuffer data) throws Exception {
		IZX3 instance = new IZX3();

		// Somehow IZX3 includes identifier and the length int into the length.
		int length = data.getInt() - 8;
		int lengthStart = data.position();
		
		short amount = data.getShort();
		
		for (int j = 0; j < amount; j++) {
			instance.tileId.add(data.getShort());
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("IZX3");
		outputStream.sectionStart();

		outputStream.write((short) this.tileId.size());
		
		for (short tileId : this.tileId) {
			outputStream.write(tileId);
		}
		
		byte[] section = outputStream.sectionEnd();
		// Somehow IZX3 includes identifier and the length int into the length.
		outputStream.write(section.length + 8);
		outputStream.write(section);
	}
}
