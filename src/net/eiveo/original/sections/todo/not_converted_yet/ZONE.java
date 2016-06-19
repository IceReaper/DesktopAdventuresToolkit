package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.original.sections.IZON;
import net.eiveo.original.sections.todo.determine_values.IACT;
import net.eiveo.original.sections.todo.determine_values.IZAX;
import net.eiveo.original.sections.todo.determine_values.IZX4;
import net.eiveo.original.sections.todo.determine_values.HTSP.Hotspot;
import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ZONE {
	public List<Zone> zones = new ArrayList<>();
	
	public static ZONE read(SmartByteBuffer data, boolean isYoda) throws Exception {
		ZONE instance = new ZONE();
		
		int length = 0;
		int lengthStart = 0;

		if (!isYoda) {
			length = data.getInt();
			lengthStart = data.position();
		}
		
		short zoneCount = data.getShort();
		
		for (short i = 0; i < zoneCount; i++) {
			instance.zones.add(Zone.read(data, isYoda, i));
		}
		
		if (!isYoda && lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) {
		outputStream.write("ZONE");
		outputStream.sectionStart();
		
		outputStream.write((short) this.zones.size());
		
		for (short i = 0; i < this.zones.size(); i++) {
			this.zones.get(i).write(outputStream, isYoda, i);
		}

		byte[] section = outputStream.sectionEnd();
		
		if (!isYoda) {
			outputStream.write(section.length);
		}
		
		outputStream.write(section);
	}
	
	public static class Zone {
		public short world;
		public IZON IZON;
		private List<Hotspot> hotspots = new ArrayList<>();
		public IZAX IZAX;
		public IZX2 IZX2;
		public IZX3 IZX3;
		public IZX4 IZX4;
		private List<IACT> IACT = new ArrayList<>();

		public static Zone read(SmartByteBuffer data, boolean isYoda, short zoneId) throws Exception {
			Zone instance = new Zone();

			int length = 0;
			int lengthStart = 0;
			
			if (isYoda) {
				instance.world = data.getShort();
				length = data.getInt();
				lengthStart = data.position();
				
				if (zoneId != data.getShort()) {
					throw new Exception("ZoneId Mismatch.");
				}
			}
			
			String izon = data.getString(-1, 4);
			
			if (!izon.equals("IZON")) {
				throw new Exception("IZON mismatch.");
			}
			
			instance.IZON = net.eiveo.original.sections.IZON.read(data, isYoda, instance.world);
			
			if (isYoda) {
				short amount = data.getShort();

				for (int j = 0; j < amount; j++) {
					instance.hotspots.add(Hotspot.read(data));
				}
				
				String izax = data.getString(-1, 4);
				
				if (!izax.equals("IZAX")) {
					throw new Exception("IZAX mismatch.");
				}
				
				instance.IZAX = net.eiveo.original.sections.todo.determine_values.IZAX.read(data, isYoda);
				
				String izx2 = data.getString(-1, 4);
				
				if (!izx2.equals("IZX2")) {
					throw new Exception("IZX2 mismatch.");
				}
				
				instance.IZX2 = net.eiveo.original.sections.todo.not_converted_yet.IZX2.read(data);
				
				String izx3 = data.getString(-1, 4);
				
				if (!izx3.equals("IZX3")) {
					throw new Exception("IZX3 mismatch.");
				}

				instance.IZX3 = net.eiveo.original.sections.todo.not_converted_yet.IZX3.read(data);
				
				String izx4 = data.getString(-1, 4);
				
				if (!izx4.equals("IZX4")) {
					throw new Exception("IZX4 mismatch.");
				}

				instance.IZX4 = net.eiveo.original.sections.todo.determine_values.IZX4.read(data);
				
				amount = data.getShort();
				
				for (int j = 0; j < amount; j++) {
					String iact = data.getString(-1, 4);
					
					if (!iact.equals("IACT")) {
						throw new Exception("IACT mismatch.");
					}
					
					instance.IACT.add(net.eiveo.original.sections.todo.determine_values.IACT.read(data, isYoda));
				}
			}
			
			if (isYoda && lengthStart + length != data.position()) {
				throw new Exception("Length mismatch.");
			}
			
			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream, boolean isYoda, short zoneId) {
			outputStream.sectionStart();

			if (isYoda) {
				outputStream.write(zoneId);
			}
			
			this.IZON.write(outputStream, isYoda);
			
			if (isYoda) {
				outputStream.write((short) this.hotspots.size());

				for (Hotspot hotspot : this.hotspots) {
					hotspot.write(outputStream);
				}
				
				this.IZAX.write(outputStream, isYoda);
				this.IZX2.write(outputStream);
				this.IZX3.write(outputStream);
				this.IZX4.write(outputStream);

				outputStream.write((short) this.IACT.size());
				
				for (IACT iact : this.IACT) {
					iact.write(outputStream, isYoda);
				}
			}

			byte[] section = outputStream.sectionEnd();
			
			if (isYoda) {
				outputStream.write(this.world);
				outputStream.write(section.length);
			}
			
			outputStream.write(section);
		}
	}
}
