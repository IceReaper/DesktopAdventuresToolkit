package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ZNAM {
	private Map<Short, String> names = new LinkedHashMap<>();
	
	public static ZNAM read(SmartByteBuffer data) throws Exception {
		ZNAM instance = new ZNAM();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short zoneId = data.getShort();

			if (zoneId == -1) {
				break;
			} else {
				int nameStart = data.position();
				instance.names.put(zoneId, data.getString()); // Filled with trash - bug
				data.position(nameStart + 16);
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ZNAM");
		outputStream.sectionStart();
		
		for (Entry<Short, String> entry : this.names.entrySet()) {
			outputStream.write(entry.getKey());
			outputStream.write(entry.getValue());
			
			for (int i = entry.getValue().length(); i < 16; i++) {
				outputStream.write((byte) 0);
			}
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
