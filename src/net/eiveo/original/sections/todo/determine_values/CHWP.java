package net.eiveo.original.sections.todo.determine_values;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class CHWP {
	private Map<Short, Weapon> weapons = new LinkedHashMap<>();
	
	public static CHWP read(SmartByteBuffer data) throws Exception {
		CHWP instance = new CHWP();
		
		int length = data.getInt();
		int lengthStart = data.position();

		while (true) {
			short characterId = data.getShort();

			if (characterId == -1) {
				break;
			} else {
				instance.weapons.put(characterId, Weapon.read(data));
			}
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) throws Exception {
		outputStream.write("CHWP");
		outputStream.sectionStart();
		
		for (Entry<Short, Weapon> entry : this.weapons.entrySet()) {
			outputStream.write(entry.getKey());
			entry.getValue().write(outputStream);
		}

		outputStream.write((short) -1);
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
	
	public static class Weapon {
		private short weaponOrSoundId;
		private short healthOrUnk;
		
		public static Weapon read(SmartByteBuffer data) {
			Weapon instance = new Weapon();
			
			instance.weaponOrSoundId = data.getShort();
			instance.healthOrUnk = data.getShort(); // TODO what is unk? 1? (if indy values change to 1 makes no difference, assume its 1 and 0 is buggy)

			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream) {
			outputStream.write(this.weaponOrSoundId);
			outputStream.write(this.healthOrUnk);
		}
	}
}
