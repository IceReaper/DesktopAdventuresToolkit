package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class CAUX {
	private Map<Short, Short> damage = new LinkedHashMap<>();
	
	public static CAUX read(SmartByteBuffer data) throws Exception {
		CAUX instance = new CAUX();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short characterId = data.getShort();

			if (characterId == -1) {
				break;
			} else {
				instance.damage.put(characterId, data.getShort());
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("CAUX");
		outputStream.sectionStart();
		
		for (Entry<Short, Short> entry : this.damage.entrySet()) {
			outputStream.write(entry.getKey());
			outputStream.write(entry.getValue());
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
