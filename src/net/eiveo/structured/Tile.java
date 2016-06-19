package net.eiveo.structured;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.w3c.dom.Node;

public class Tile {
	public String id;
	public String name;
	public String type;
	public String subtype;
	public BufferedImage image;

	public static Tile read(File file, Node node) throws Exception {
		Tile instance = new Tile();

		instance.id = file.getName().substring(0, file.getName().length() - 4);
		instance.name = node.getAttributes().getNamedItem("name").getNodeValue();
		instance.type = node.getAttributes().getNamedItem("type").getNodeValue();
		instance.subtype = node.getAttributes().getNamedItem("subtype").getNodeValue();
		
		instance.image = ImageIO.read(file);
		
		return instance;
	}
	
	public void write(File root, StringBuilder xml) throws Exception {
		File tilesDir = new File(root, "tiles");
		tilesDir.mkdirs();
		
		ImageIO.write(this.image, "png", new File(tilesDir, this.id + ".png"));
		xml.append("\n\t<tile file=\"" + this.id + "\" name=\"" + (this.name == null ? "" : this.name) + "\" type=\"" + this.type + "\" subtype=\"" + this.subtype + "\"/>");
	}
}
