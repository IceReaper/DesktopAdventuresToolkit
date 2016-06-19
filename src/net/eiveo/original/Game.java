package net.eiveo.original;

import java.io.File;
import java.nio.ByteOrder;
import java.nio.file.Files;

import net.eiveo.utils.SmartByteBuffer;

public class Game {
	private static final int YODA_PALETTE_OFFSET = 0x550F0;
	private static final int INDY_PALETTE_OFFSET = 0x36656;
	
	public boolean isYoda;
	
	public byte[] palette = new byte[256 * 4];
	public Container container;
	
	public static Game read(File container, File executable) throws Exception {
		Game instance = new Game();

		SmartByteBuffer executableBuffer = SmartByteBuffer.wrap(Files.readAllBytes(executable.toPath()));
		SmartByteBuffer containerBuffer = SmartByteBuffer.wrap(Files.readAllBytes(container.toPath()));

		executableBuffer.order(ByteOrder.LITTLE_ENDIAN);
		containerBuffer.order(ByteOrder.LITTLE_ENDIAN);
		
		instance.isYoda = executableBuffer.getInt() == 0x00905A4D;

		executableBuffer.position(instance.isYoda ? Game.YODA_PALETTE_OFFSET : Game.INDY_PALETTE_OFFSET);
		executableBuffer.get(instance.palette);
		
		instance.container = Container.read(containerBuffer, container.getParentFile(), instance.isYoda);
		
		return instance;
	}

	public void write(File root) throws Exception {
		root.mkdirs();
		
		// TODO update palette in exe
		
		this.container.write(root, this.isYoda);
	}
}
