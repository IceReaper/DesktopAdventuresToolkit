package net.eiveo.original.sections;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class IZON {
	public short width;
	public short height;
	public short type;
	public short[] tiles;
	public Short world;
	
	public static IZON read(SmartByteBuffer data, boolean isYoda, short world) throws Exception {
		IZON instance = new IZON();

		// Somehow IZON includes identifier and the length int into the length.
		int length = data.getInt() - 8;
		int lengthStart = data.position();

		instance.width = data.getShort();
		instance.height = data.getShort();
		instance.type = data.getShort();
		data.getShort(); // 0
		
		if (isYoda) {
			data.getShort(); // -1
			instance.world = data.getShort();
			
			if (instance.world != world) {
				throw new Exception("mapGroup mismatch.");
			}
		}
		
		instance.tiles = new short[instance.width * instance.height * 3];
		
		for (int i = 0; i < instance.tiles.length; i++) {
			instance.tiles[i] = data.getShort();
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) {
		outputStream.write("IZON");
		outputStream.sectionStart();

		outputStream.write(this.width);
		outputStream.write(this.height);
		outputStream.write(this.type);
		outputStream.write((short) 0);
		
		if (isYoda) {
			outputStream.write((short) -1);
			outputStream.write(this.world);
		}
		
		for (short tile : this.tiles) {
			outputStream.write(tile);
		}
		
		byte[] section = outputStream.sectionEnd();
		// Somehow IZON includes identifier and the length int into the length.
		outputStream.write(section.length + 8);
		outputStream.write(section);
	}
}
