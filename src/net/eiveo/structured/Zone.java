package net.eiveo.structured;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Zone {
	public String id;
	public int width;
	public int height;
	public String type;
	public String world;
	public Tile[][] tiles;

	public static Zone read(File zone, Map<String, Tile> tiles) throws Exception {
		Zone instance = new Zone();
		
		instance.id = zone.getName().substring(0, zone.getName().length() - 4);
		
		DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();
		InputSource input = new InputSource(new InputStreamReader(new FileInputStream(zone), "UTF-8"));
		Document xml = builder.newDocumentBuilder().parse(input);
		
		Node mapNode = xml.getElementsByTagName("map").item(0);
		instance.width = Integer.parseInt(mapNode.getAttributes().getNamedItem("width").getNodeValue());
		instance.height = Integer.parseInt(mapNode.getAttributes().getNamedItem("height").getNodeValue());

		NodeList propertiesNodes = xml.getElementsByTagName("properties").item(0).getChildNodes();
		
		for (int i = 0; i < propertiesNodes.getLength(); i++) {
			Node node = propertiesNodes.item(i);
			
			switch (node.getAttributes().getNamedItem("name").getNodeValue()) {
				case "type":
					instance.type = node.getAttributes().getNamedItem("value").getNodeValue();
					break;
				case "world":
					instance.world = node.getAttributes().getNamedItem("value").getNodeValue();
					break;
			}
		}
		
		Map<Integer, String> tileMapping = new HashMap<>();
		NodeList tilesetNodes = xml.getElementsByTagName("tileset");

		for (int i = 0; i < tilesetNodes.getLength(); i++) {
			Node node = tilesetNodes.item(i);
			int tmxTileId = Integer.parseInt(node.getAttributes().getNamedItem("firstgid").getNodeValue());
			String tileId = node.getChildNodes().item(0).getAttributes().getNamedItem("source").getNodeValue();
			tileId = tileId.substring(9, tileId.length() - 4);
			tileMapping.put(tmxTileId, tileId);
		}

		NodeList layerNodes = xml.getElementsByTagName("layer");
		instance.tiles = new Tile[3][];
		
		for (int layer = 0; layer < 3; layer++) {
			instance.tiles[layer] = new Tile[instance.width * instance.height];
			String[] tmxTiles = layerNodes.item(layer).getChildNodes().item(0).getTextContent().split(",");
			
			for (int i = 0; i < instance.width * instance.height; i++) {
				int tmxTile = Integer.parseInt(tmxTiles[i]);
				
				if (tmxTile > 0) {
					instance.tiles[layer][i] = tiles.get(tileMapping.get(tmxTile));
				}
			}
		}
		
		return instance;
	}
	
	public void write(File root) throws Exception {
		File zonesDir = new File(root, "zones");
		zonesDir.mkdirs();
		
		StringBuilder xml = new StringBuilder();
		xml.append("<map version=\"1.0\" orientation=\"orthogonal\" width=\"" + this.width + "\" height=\"" + this.height + "\" tilewidth=\"32\" tileheight=\"32\">");

		xml.append("<properties>");
		xml.append("<property name=\"type\" value=\"" + this.type + "\"/>");
		xml.append("<property name=\"world\" value=\"" + this.world + "\"/>");
		xml.append("</properties>");

		List<Tile> usedTiles = new ArrayList<>();

		String layers = "";
		
		for (short layer = 0; layer < this.tiles.length; layer++) {
			layers += "<layer width=\"" + this.width + "\" height=\"" + this.height + "\"><data encoding=\"csv\">";
			
			for (int y = 0; y < this.height; y++) {
				for (int x = 0; x < this.width; x++) {
					Tile tile = this.tiles[layer][x + y * this.width];
					int tileId = -1;
					
					if (tile != null) {
						if (!usedTiles.contains(tile)) {
							usedTiles.add(tile);
						}
						
						tileId = usedTiles.indexOf(tile);
					}
					
					layers += (x == 0 && y == 0 ? "" : ",") + (tileId + 1);
				}
			}
			
			layers += "</data></layer>";
		}
		
		for (short j = 0; j < usedTiles.size(); j++) {
			xml.append("<tileset firstgid=\"" + (j + 1) + "\" tilewidth=\"32\" tileheight=\"32\"><image source=\"../tiles/" + usedTiles.get(j).id + ".png\"/></tileset>");
		}
		
		xml.append(layers);
		//tmx += "<objectgroup name=\"hotspots\">" + hotspots + "</objectgroup>";
		//tmx += "<objectgroup name=\"characters\">" + characters + "</objectgroup>";
		xml.append("</map>");
		
		Files.write(new File(zonesDir, this.id + ".tmx").toPath(), xml.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}
}
