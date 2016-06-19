package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.original.sections.todo.determine_values.IZAX;
import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ZAUX {
	private List<IZAX> IZAX = new ArrayList<>();
	
	public static ZAUX read(SmartByteBuffer data) throws Exception {
		ZAUX instance = new ZAUX();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		while (length + lengthStart > data.position()) {
			String izax = data.getString(-1, 4);
			
			if (!izax.equals("IZAX")) {
				throw new Exception("IZAX mismatch.");
			}
			
			instance.IZAX.add(net.eiveo.original.sections.todo.determine_values.IZAX.read(data, false));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ZAUX");
		outputStream.sectionStart();

		for (IZAX izax : this.IZAX) {
			izax.write(outputStream, false);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
