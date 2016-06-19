package net.eiveo.original.sections.todo.determine_values;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class HTSP {
	private Map<Short, List<Hotspot>> hotspots = new LinkedHashMap<>();
	
	public static HTSP read(SmartByteBuffer data) throws Exception {
		HTSP instance = new HTSP();

		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short zoneId = data.getShort();
			
			if (zoneId == -1) {
				break;
			}
			
			short amount = data.getShort();
			
			List<Hotspot> hotspots = new ArrayList<>();
			instance.hotspots.put(zoneId, hotspots);
			
			for (int i = 0; i < amount; i++) {
				hotspots.add(Hotspot.read(data));
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("HTSP");
		outputStream.sectionStart();

		for (Entry<Short, List<Hotspot>> entry : this.hotspots.entrySet()) {
			outputStream.write(entry.getKey());
			outputStream.write((short) entry.getValue().size());
			
			for (Hotspot hotspot : entry.getValue()) {
				hotspot.write(outputStream);
			}
		}
		
		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
	
	public static class Hotspot {
		private short type;
		private short x;
		private short y;
		private short param;

		public static Hotspot read(SmartByteBuffer data) {
			Hotspot instance = new Hotspot();
			
			instance.type = data.getShort();
			data.getShort(); // 0
			instance.x = data.getShort();
			instance.y = data.getShort();
			data.getShort(); // 1
			instance.param = data.getShort();
			
			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream) {
			outputStream.write(this.type);
			outputStream.write((short) 0);
			outputStream.write(this.x);
			outputStream.write(this.y);
			outputStream.write((short) 1);
			outputStream.write(this.param);
		}
	}
}
