package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.original.sections.todo.determine_values.IPUZ;
import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class PUZ2 {
	private List<IPUZ> IPUZ = new ArrayList<>();

	public static PUZ2 read(SmartByteBuffer data, boolean isYoda) throws Exception {
		PUZ2 instance = new PUZ2();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short puzzleId = data.getShort();
			
			if (puzzleId == -1) {
				break;
			}

			String ipuz = data.getString(-1, 4);
			
			if (!ipuz.equals("IPUZ")) {
				throw new Exception("IPUZ mismatch.");
			}
			
			instance.IPUZ.add(net.eiveo.original.sections.todo.determine_values.IPUZ.read(data, isYoda));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) throws Exception {
		outputStream.write("PUZ2");
		outputStream.sectionStart();

		for (int i = 0; i < this.IPUZ.size(); i++) {
			outputStream.write((short) i);
			this.IPUZ.get(i).write(outputStream, isYoda);
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
