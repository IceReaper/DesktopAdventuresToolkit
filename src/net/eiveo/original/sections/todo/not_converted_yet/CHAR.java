package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class CHAR {
	private List<ICHA> ICHA = new ArrayList<>();
	
	public static CHAR read(SmartByteBuffer data, boolean isYoda) throws Exception {
		CHAR instance = new CHAR();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		while (true) {
			short characterId = data.getShort();
			
			if (characterId == -1) {
				break;
			}

			String icha = data.getString(-1, 4);
			
			if (!icha.equals("ICHA")) {
				throw new Exception("ICHA mismatch.");
			}
			
			instance.ICHA.add(net.eiveo.original.sections.todo.not_converted_yet.ICHA.read(data, isYoda));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) throws Exception {
		outputStream.write("CHAR");
		outputStream.sectionStart();

		for (short i = 0; i < this.ICHA.size(); i++) {
			outputStream.write(i);
			this.ICHA.get(i).write(outputStream, isYoda);
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
