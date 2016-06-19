package net.eiveo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.eiveo.original.Container;
import net.eiveo.original.sections.ENDF;
import net.eiveo.original.sections.IZON;
import net.eiveo.original.sections.SNDS;
import net.eiveo.original.sections.STUP;
import net.eiveo.original.sections.TILE;
import net.eiveo.original.sections.TNAM;
import net.eiveo.original.sections.VERS;
import net.eiveo.original.sections.todo.determine_values.IZAX;
import net.eiveo.original.sections.todo.determine_values.IZX4;
import net.eiveo.original.sections.todo.not_converted_yet.IZX2;
import net.eiveo.original.sections.todo.not_converted_yet.IZX3;
import net.eiveo.original.sections.todo.not_converted_yet.ZONE;
import net.eiveo.structured.Sound;
import net.eiveo.structured.Tile;
import net.eiveo.structured.Zone;
import net.eiveo.utils.Image;

public class Packer {
	public static void pack(File container, File executable, boolean isYoda) throws Exception {
		// Step 1: Read
		net.eiveo.structured.Game structuredGame = net.eiveo.structured.Game.read(container);
		
		// Step 2: Convert
		net.eiveo.original.Game originalGame = new net.eiveo.original.Game();
		originalGame.isYoda = isYoda;
		originalGame.container = new Container();
		
		// Palette
		for (int i = 0; i < 256; i++) {
			int color = structuredGame.palette.colors[i];
			System.arraycopy(new byte[] {
				// TODO verify order!
		        (byte) color,
		        (byte)(color >> 8),
		        (byte)(color >> 16),
		        (byte)(color >> 24)
	        }, 0, originalGame.palette, i * 4, 4);
		}
		
		originalGame.container.VERS = new VERS();
		
		// Splash Image
		originalGame.container.STUP = new STUP();
		originalGame.container.STUP.image = Image.imageToIndexed(structuredGame.splashImage, structuredGame.palette);

		// Sounds
		originalGame.container.SNDS = new SNDS();
		
		for (Sound sound : structuredGame.sounds.values()) {
			SNDS.Sound originalSound = new SNDS.Sound();
			
			originalSound.name = sound.name;
			originalSound.data = sound.data;
			
			originalGame.container.SNDS.sounds.add(originalSound);
		}
		
		// Tiles
		originalGame.container.TILE = new TILE();
		originalGame.container.TNAM = new TNAM();
		
		Map<String, Short> tileMapping = new HashMap<>();
		
		for (Tile tile : structuredGame.tiles.values()) {
			TILE.Tile originalTile = new TILE.Tile();
			
			originalTile.image = Image.imageToIndexed(tile.image, structuredGame.palette);
			originalTile.type = (short) (Mapping.getType("tile.type", tile.type).intValue() << 1);
			originalTile.subtype = (short) Mapping.getType("tile.subtype-" + tile.type, tile.subtype).intValue();
			
			if (Image.lastImageHadAlpha) {
				originalTile.type++;
			}
			
			if (!tile.name.equals("")) {
				originalGame.container.TNAM.names.put((short) originalGame.container.TILE.tiles.size(), tile.name);
			}
			
			tileMapping.put(tile.id, (short) originalGame.container.TILE.tiles.size());
			
			originalGame.container.TILE.tiles.add(originalTile);
		}
		
		// Zones
		originalGame.container.ZONE = new ZONE();

		for (Zone zone : structuredGame.zones.values()) {
			ZONE.Zone originalZone = new ZONE.Zone();
			originalZone.world = Mapping.getType("zone.world", zone.world);
			
			originalZone.IZON = new IZON();
			originalZone.IZON.height = (short) zone.height;
			originalZone.IZON.width = (short) zone.width;
			originalZone.IZON.type = Mapping.getType("zone.type", zone.type);
			originalZone.IZON.world = originalZone.world;
			originalZone.IZON.tiles = new short[zone.width * zone.height * 3];
			
			for (int layer = 0; layer < 3; layer++) {
				for (int i = 0; i < zone.width * zone.height; i++) {
					Tile tile = zone.tiles[layer][i];
					originalZone.IZON.tiles[i * 3 + layer] = tile != null ? tileMapping.get(tile.id) : -1; 
				}
			}
			
			// TODO
			originalZone.IZAX = new IZAX();
			originalZone.IZX2 = new IZX2();
			originalZone.IZX3 = new IZX3();
			originalZone.IZX4 = new IZX4();

			originalGame.container.ZONE.zones.add(originalZone);
		}
		
		// TODO continue here...
		
		originalGame.container.ENDF = new ENDF();
		
		// Step 3: Save
		originalGame.write(new File("output/" + (originalGame.isYoda ? "yoda-packed" : "indy-packed")));
	}
}
