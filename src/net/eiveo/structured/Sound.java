package net.eiveo.structured;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Sound {
	public String name;
	public byte[] data;

	public static Sound read(File sound) throws Exception {
		Sound instance = new Sound();
		
		instance.name = sound.getName();
		instance.data = Files.readAllBytes(sound.toPath());
		
		return instance;
	}
	
	public void write(File root) throws Exception {
		File soundsDir = new File(root, "sounds");
		soundsDir.mkdirs();
		
		Files.write(new File(soundsDir, this.name).toPath(), this.data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}
}
