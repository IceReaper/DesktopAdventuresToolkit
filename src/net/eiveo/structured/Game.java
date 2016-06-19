package net.eiveo.structured;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Game {
	public Palette palette;
	public BufferedImage splashImage;
	public Map<String, Sound> sounds = new LinkedHashMap<>();
	public Map<String, Tile> tiles = new LinkedHashMap<>();
	public Map<String, Zone> zones = new LinkedHashMap<>();

	public static Game read(File root) throws Exception {
		Game instance = new Game();
		
		instance.palette = Palette.read(root);
		
		instance.splashImage = ImageIO.read(new File(root, "splash.png"));
		
		for (File file : new File(root, "sounds").listFiles()) {
			Sound sound = Sound.read(file);
			instance.sounds.put(sound.name, sound);
		}

		DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
		InputSource input = new InputSource(new InputStreamReader(new FileInputStream(new File(root, "tiles.xml")), "UTF-8"));
		Document xml = builder.newDocumentBuilder().parse(input);
		NodeList xmlNodes = xml.getElementsByTagName("tile");
		
		for (int i = 0; i < xmlNodes.getLength(); i++) {
			Node node = xmlNodes.item(i);

			Tile tile = Tile.read(new File(root, "tiles/" + node.getAttributes().getNamedItem("file").getNodeValue() + ".png"), node);
			instance.tiles.put(tile.id, tile);

			instance.tiles.put(tile.id, tile);
		}
		
		File[] zones = new File(root, "zones").listFiles();
		
		// TODO remove this in the end - order does not care anymore then...
		//      We use this to be able to compare the re-packed container with the original one.
		Arrays.sort(zones, new Comparator<File>() {
			public int compare(File f1, File f2) {
				int i1 = Integer.parseInt(f1.getName().substring(0, f1.getName().length() - 4));
				int i2 = Integer.parseInt(f2.getName().substring(0, f2.getName().length() - 4));
				return i1 - i2;
			}
		});
		
		for (File file : zones) {
			Zone zone = Zone.read(file, instance.tiles);
			instance.zones.put(zone.id, zone);
		}
		
		return instance;
	}
	
	public void write(File root) throws Exception {
		root.mkdirs();
		
		this.palette.write(root);
		
		ImageIO.write(this.splashImage, "png", new File(root, "splash.png"));
		
		for (Sound sound : this.sounds.values()) {
			sound.write(root);
		}
		
		StringBuilder xml = new StringBuilder();
		
		for (Tile tile : this.tiles.values()) {
			tile.write(root, xml);
		}
		
		Files.write(new File(root, "tiles.xml").toPath(), ("<tiles>" + xml + "\n</tiles>").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		for (Zone zone : this.zones.values()) {
			zone.write(root);
		}
	}
}
