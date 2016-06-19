package net.eiveo.utils;

import java.awt.image.BufferedImage;

import net.eiveo.structured.Palette;

public class Image {
	public static boolean lastImageHadAlpha;

	public static BufferedImage indexedToImage(byte[] indexed, Palette palette, int width, int height, boolean hasAlpha) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int index = indexed[x + y * image.getWidth()] & 0xff;

				// First an last 10 colors are reserved.
				if (!hasAlpha && (index < 10 || index > 255 - 10)) {
					image.setRGB(x, y, 0x000000ff);
				} else {
					image.setRGB(x, y, palette.colors[index]);
				}
			}
		}
		
		return image;
	}

	public static byte[] imageToIndexed(BufferedImage image, Palette palette) {
		byte[] indexed = new byte[image.getWidth() * image.getHeight()];
		lastImageHadAlpha = false;
		
		outer:
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (image.getRGB(x, y) == 0) {
					lastImageHadAlpha = true;
					break outer;
				}
			}
		}

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				// First an last 10 colors are reserved.
				for (int colorIndex = 10; colorIndex < 256 - 10; colorIndex++) {
					if (palette.colors[colorIndex] == image.getRGB(x, y)) {
						indexed[x + y * image.getWidth()] = (byte) colorIndex;
						break;
					}
				}
			}
		}
		
		return indexed;
	}
}
