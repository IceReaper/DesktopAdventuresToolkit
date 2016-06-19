package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.original.sections.todo.determine_values.IACT;
import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ACTN {
	private Map<Short, List<IACT>> IACT = new LinkedHashMap<>();
	
	public static ACTN read(SmartByteBuffer data) throws Exception {
		ACTN instance = new ACTN();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short zoneId = data.getShort();
			
			if (zoneId == -1) {
				break;
			}
			
			short amount = data.getShort();
			
			List<IACT> iacts = new ArrayList<>();
			instance.IACT.put(zoneId, iacts);
			
			for (int i = 0; i < amount; i++) {
				String iact = data.getString(-1, 4);
				
				if (!iact.equals("IACT")) {
					throw new Exception("IACT mismatch.");
				}
				
				iacts.add(net.eiveo.original.sections.todo.determine_values.IACT.read(data, false));
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ACTN");
		outputStream.sectionStart();

		for (Entry<Short, List<IACT>> entry : this.IACT.entrySet()) {
			outputStream.write(entry.getKey());
			outputStream.write((short) entry.getValue().size());
			
			for (IACT iact : entry.getValue()) {
				iact.write(outputStream, false);
			}
		}
		
		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
