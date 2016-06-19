package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ANAM {
	private Map<Short, Map<Short, String>> names = new LinkedHashMap<>();
	
	public static ANAM read(SmartByteBuffer data) throws Exception {
		ANAM instance = new ANAM();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short zoneId = data.getShort();

			if (zoneId == -1) {
				break;
			} else {
				Map<Short, String> names = new LinkedHashMap<>();
				instance.names.put(zoneId, names);

				while (true) {
					short actionId = data.getShort();

					if (actionId == -1) {
						break;
					} else {
						names.put(actionId, data.getString(-1, 16).trim());
					}
				}
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ANAM");
		outputStream.sectionStart();
		
		for (Entry<Short, Map<Short, String>> entry : this.names.entrySet()) {
			outputStream.write(entry.getKey());
			
			for (Entry<Short, String> subEntry : entry.getValue().entrySet()) {
				outputStream.write(subEntry.getKey());
				outputStream.write(subEntry.getValue());
				
				for (int i = subEntry.getValue().length(); i < 16; i++) {
					outputStream.write((byte) 0);
				}
			}
			
			outputStream.write((short) -1);
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
