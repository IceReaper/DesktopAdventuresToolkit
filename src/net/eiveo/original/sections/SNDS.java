package net.eiveo.original.sections;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class SNDS {
	public List<Sound> sounds = new ArrayList<>();
	
	public static SNDS read(SmartByteBuffer data, boolean isYoda, File root) throws Exception {
		SNDS instance = new SNDS();
		
		int length = data.getInt();
		int lengthStart = data.position();

		short numSounds = (short) (data.getShort() * -1);

		for (int i = 0; i < numSounds; i++) {
			instance.sounds.add(Sound.read(data, isYoda, root));
		}
		
		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda, File root) throws Exception {
		outputStream.write("SNDS");
		outputStream.sectionStart();
		
		outputStream.write((short) (this.sounds.size() * -1));

		root = new File(root, isYoda ? "sfx/" : "");
		root.mkdirs();
		
		for (Sound sound : this.sounds) {
			sound.write(outputStream, isYoda, root);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
	
	public static class Sound {
		public String name;
		public byte[] data;

		public static Sound read(SmartByteBuffer data, boolean isYoda, File root) throws Exception {
			Sound instance = new Sound();
			
			short nameLength = data.getShort();
			instance.name = new File(data.getString(-1, nameLength).trim()).getName();
			
			if (!instance.name.equals("")) {
				instance.data = Files.readAllBytes(new File(root, (isYoda ? "sfx/" : "") + instance.name).toPath());
			}
			
			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream, boolean isYoda, File root) throws Exception {
			outputStream.write((short) (this.name.length() + 1));
			outputStream.write(this.name);
			outputStream.write((byte) 0x00);
			
			if (!this.name.equals("")) {
				Files.write(new File(root, this.name).toPath(), this.data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			}
		}
	}
}
