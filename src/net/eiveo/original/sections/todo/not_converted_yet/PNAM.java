package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class PNAM {
	private List<String> names = new ArrayList<>();
	
	public static PNAM read(SmartByteBuffer data) throws Exception {
		PNAM instance = new PNAM();
		
		int length = data.getInt();
		int lengthStart = data.position();

		short numEntries = data.getShort();

		for (int i = 0; i < numEntries; i++) {
			instance.names.add(data.getString(-1, 16).trim());
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("PNAM");
		outputStream.sectionStart();
		
		outputStream.write((short) this.names.size());
		
		for (String name: this.names) {
			outputStream.write(name);
			
			for (int i = name.length(); i < 16; i++) {
				outputStream.write((byte) 0);
			}
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
