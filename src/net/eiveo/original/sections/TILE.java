package net.eiveo.original.sections;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class TILE {
	public List<Tile> tiles = new ArrayList<>();
	
	public static TILE read(SmartByteBuffer data) throws Exception {
		TILE instance = new TILE();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		while (data.position() < lengthStart + length) {
			instance.tiles.add(Tile.read(data));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("TILE");
		outputStream.sectionStart();
		
		for (Tile tile : this.tiles) {
			tile.write(outputStream);
		}

		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
	
	public static class Tile {
		public short type;
		public short subtype;
		public byte[] image = new byte[32 * 32];
		
		public static Tile read(SmartByteBuffer data) {
			Tile instance = new Tile();
			
			instance.type = data.getShort();
			instance.subtype = data.getShort();
			data.get(instance.image);
			
			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream) {
			outputStream.write(this.type);
			outputStream.write(this.subtype);
			outputStream.write(this.image);
		}
	}
}
