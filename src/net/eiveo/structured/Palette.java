package net.eiveo.structured;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Palette {
	public int[] colors = new int[256];

	public static Palette read(File root) throws Exception {
		Palette instance = new Palette();
		
		BufferedImage palette = ImageIO.read(new File(root, "palette.png"));

		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				instance.colors[x + y * 16] = palette.getRGB(x, y);
			}	
		}
		
		return instance;
	}
	
	public void write(File root) throws Exception {
		BufferedImage paletteImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				paletteImage.setRGB(x, y, colors[x + y * 16]);
			}	
		}
		
		ImageIO.write(paletteImage, "png", new File(root, "palette.png"));
	}
}
