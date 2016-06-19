package net.eiveo.original.sections;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class TNAM {
	public Map<Short, String> names = new LinkedHashMap<>();
	
	public static TNAM read(SmartByteBuffer data, boolean isYoda) throws Exception {
		TNAM instance = new TNAM();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short tileId = data.getShort();

			if (tileId == -1) {
				break;
			} else {
				instance.names.put(tileId, data.getString(-1, isYoda ? 24 : 16).trim());
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) {
		outputStream.write("TNAM");
		outputStream.sectionStart();
		
		for (Entry<Short, String> entry : this.names.entrySet()) {
			outputStream.write(entry.getKey());
			outputStream.write(entry.getValue());
			
			for (int i = entry.getValue().length(); i < (isYoda ? 24 : 16); i++) {
				outputStream.write((byte) 0);
			}
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
