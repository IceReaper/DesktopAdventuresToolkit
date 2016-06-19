package net.eiveo.original.sections.todo.determine_values;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class IZAX {
	private short unk1;
	private List<Character> characters = new ArrayList<>();
	private List<Short> tileId1 = new ArrayList<>();
	private List<Short> tileId2 = new ArrayList<>();

	public static IZAX read(SmartByteBuffer data, boolean isYoda) throws Exception {
		IZAX instance = new IZAX();
		
		// Somehow IZAX includes identifier and the length int into the length.
		int length = data.getInt() - 8;
		int lengthStart = data.position();
		
		instance.unk1 = data.getShort(); // TODO 0 or 1
		
		short amount = data.getShort();
		
		for (int j = 0; j < amount; j++) {
			instance.characters.add(Character.read(data, isYoda));
		}
		
		amount = data.getShort();
		
		for (int j = 0; j < amount; j++) {
			instance.tileId1.add(data.getShort());
		}
		
		if (isYoda) {
			amount = data.getShort();
			
			for (int j = 0; j < amount; j++) {
				instance.tileId2.add(data.getShort());
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) {
		outputStream.write("IZAX");
		outputStream.sectionStart();

		outputStream.write(this.unk1);
		outputStream.write((short) this.characters.size());
		
		for (Character character : this.characters) {
			character.write(outputStream, isYoda);
		}

		outputStream.write((short) this.tileId1.size());
		
		for (short tileId1 : this.tileId1) {
			outputStream.write(tileId1);
		}
		
		if (isYoda) {

			outputStream.write((short) this.tileId2.size());
			
			for (short tileId2 : this.tileId2) {
				outputStream.write(tileId2);
			}
		}
		
		byte[] section = outputStream.sectionEnd();
		// Somehow IZAX includes identifier and the length int into the length.
		outputStream.write(section.length + 8);
		outputStream.write(section);
	}
	
	public static class Character {
		private short id;
		private short x;
		private short y;
		private short unk3;
		private short unk4;
		private short[] unk5 = new short[16];

		public static Character read(SmartByteBuffer data, boolean isYoda) {
			Character instance = new Character();

			instance.id = data.getShort();
			instance.x = data.getShort();
			instance.y = data.getShort();
			
			if (isYoda) {
				instance.unk3 = data.getShort(); // TODO itemId only?
				instance.unk4 = data.getShort(); // TODO 0 or 1 - num items?
				data.getShort(); // 0
			
				for (int i = 0; i < 16; i++) {
					instance.unk5[i] = data.getShort();
				}
			}
			
			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) {
			outputStream.write(this.id);
			outputStream.write(this.x);
			outputStream.write(this.y);
			
			if (isYoda) {
				outputStream.write(this.unk3);
				outputStream.write(this.unk4);
				outputStream.write((short) 0);
				
				for (short unk5 : this.unk5) {
					outputStream.write(unk5);
				}
			}
		}
	}
}
