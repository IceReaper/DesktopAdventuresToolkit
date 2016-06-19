package net.eiveo;

import java.awt.Color;
import java.io.File;

import net.eiveo.original.sections.SNDS;
import net.eiveo.original.sections.TILE;
import net.eiveo.original.sections.todo.not_converted_yet.ZONE;
import net.eiveo.structured.Palette;
import net.eiveo.structured.Sound;
import net.eiveo.structured.Tile;
import net.eiveo.structured.Zone;
import net.eiveo.utils.Image;

public class Unpacker {
	public static void unpack(File container, File executable) throws Exception {
		// Step 1: Read
		net.eiveo.original.Game originalGame = net.eiveo.original.Game.read(container, executable);
		
		// Step 2: Convert
		net.eiveo.structured.Game structuredGame = new net.eiveo.structured.Game();
		
		// Palette
		structuredGame.palette = new Palette();
		
		for (int i = 10; i < 256 - 10; i++) {
			structuredGame.palette.colors[i] = new Color(
				originalGame.palette[i * 4 + 2] & 0xff,
				originalGame.palette[i * 4 + 1] & 0xff,
				originalGame.palette[i * 4 + 0] & 0xff,
				255
			).getRGB();
		}
		
		// Splash Image
		structuredGame.splashImage = Image.indexedToImage(originalGame.container.STUP.image, structuredGame.palette, 9 * 32, 9 * 32, false);
		
		// Sounds
		for (SNDS.Sound oldSound : originalGame.container.SNDS.sounds) {
			if (oldSound.data != null) {
				Sound sound = new Sound();
				
				sound.name = oldSound.name.toLowerCase();
				sound.data = oldSound.data;
				
				structuredGame.sounds.put(sound.name, sound);
			}
		}
		
		// Tiles and items.
		for (short tileId = 0; tileId < originalGame.container.TILE.tiles.size(); tileId++) {
			TILE.Tile originalTile = originalGame.container.TILE.tiles.get(tileId);
			
			Tile tile = new Tile();
			tile.id = "" + tileId;
			
			if (originalGame.container.TNAM.names.containsKey(tileId)) {
				tile.name = originalGame.container.TNAM.names.get(tileId);
			}
			
			tile.type = Mapping.getType("tile.type", originalTile.type >> 1);
			// TODO split non-tile related stuff like weapon, item, npc.
			tile.subtype = Mapping.getType("tile.subtype-" + tile.type, originalTile.subtype);
			tile.image = Image.indexedToImage(originalTile.image, structuredGame.palette, 32, 32, (originalTile.type & 0b1) == 1);
			
			structuredGame.tiles.put(tile.id, tile);
		}
		
		// TODO how is linked which item is which weapon? Possibly using the tileId...
		
		// Characters and Weapons.
		/*for (short characterId : originalGame.container.CHAR.ICHA.keySet()) {
			// TODO frames.
			ICHA icha = originalGame.container.CHAR.ICHA.get(characterId);
			short weaponOrSoundId = originalGame.container.CHWP.weaponOrSoundId.get(characterId);
			short healthOrUnk = originalGame.container.CHWP.healthOrUnk.get(characterId);
			short damage = originalGame.container.CAUX.damage.get(characterId);

			if (icha.type.equals("weapon")) {
				String sound = originalGame.container.SNDS.sounds.get(weaponOrSoundId);
				sound = new File(sound.substring(0, sound.lastIndexOf("."))).getName().toLowerCase();
				
				Weapon weapon = new Weapon();
				weapon.id = "" + characterId;
				weapon.name = icha.name;
				weapon.sound = sound;
				weapon.damage = damage < 1 ? null : damage;
				weapon.type = icha.subtype;
				// TODO healthOrUnk?
				// TODO where is ammo?
				
				structuredGame.weapons.add(weapon);
			} else {
				Character character = new Character();
				character.id = "" + characterId;
				character.name = icha.name;
				character.type = icha.subtype;
				
				if (icha.type.equals("npc")) {
					character.damage = damage < 1 ? null : damage;
					character.health = healthOrUnk < 1 ? null : healthOrUnk;
					
					if (weaponOrSoundId != -1) {
						character.weapon = originalGame.container.CHAR.ICHA.get(weaponOrSoundId).name;
					}
				}
				
				structuredGame.characters.add(character);
			}
		}
		
		// Puzzles
		for (short puzzleId : originalGame.container.PUZ2.IPUZ.keySet()) {
			IPUZ ipuz = originalGame.container.PUZ2.IPUZ.get(puzzleId);
			String name = originalGame.isYoda ? "" : originalGame.container.PNAM.names.get(puzzleId);
			
			// TODO write puzzle
			
			//System.out.println(puzzleId + "\t" + ipuz.type + " : " + ipuz.unk1 + "\t" + ipuz.texts[0] + "\n\t\t" + ipuz.texts[1] + "\n\t\t" + ipuz.texts[2] + "\n\t\t" + ipuz.texts[3]);
		}*/
		
		// TODO here goes the other stuff, as zone has to go last!

		for (short zoneId = 0; zoneId < originalGame.container.ZONE.zones.size(); zoneId++) {
			ZONE.Zone originalZone = originalGame.container.ZONE.zones.get(zoneId);
			
			Zone zone = new Zone();
			zone.id = "" + zoneId;
			zone.width = originalZone.IZON.width;
			zone.height = originalZone.IZON.height;
			zone.type = Mapping.getType("zone.type", originalZone.IZON.type);
			
			if (originalGame.isYoda) {
				zone.world = Mapping.getType("zone.world", originalZone.IZON.world);
			}
			
			zone.tiles = new Tile[3][zone.width * zone.height];

			for (int y = 0; y < zone.height; y++) {
				for (int x = 0; x < zone.width; x++) {
					for (int layer = 0; layer < zone.tiles.length; layer++) {
						short tileId = originalZone.IZON.tiles[layer + x * zone.tiles.length + y * zone.width * zone.tiles.length];
						zone.tiles[layer][x + y * zone.width] = tileId == -1 ? null : structuredGame.tiles.get("" + tileId);
					}	
				}
			}
			
			structuredGame.zones.put(zone.id, zone);
		}

		// TODO: ICHA: frames
		// TODO: CHWP: param2 for weapons
		
		// Step 3: Save
		structuredGame.write(new File("output/" + (originalGame.isYoda ? "yoda-unpacked" : "indy-unpacked")));
		
		/*
		// Zones
		new File(path, "zones").mkdirs();
		for (short zoneId = 0; zoneId < gameContainer.ZONE.IZON.size(); zoneId++) {
			IZON izon = gameContainer.ZONE.IZON.get(zoneId);
			
			// Characters
			String characters = "";
			IZAX izax = isYoda ? gameContainer.ZONE.IZAX.get(zoneId) : gameContainer.ZAUX.IZAX.get(zoneId);
			// TODO implement this.
			short unk1 = izax.unk1;
			
			for (int i = 0; i < izax.characterId.size(); i++) {
				short characterId = izax.characterId.get(i);
				String name = gameContainer.CHAR.ICHA.get(characterId).name;
				
				// TODO this block
				if (isYoda) {
					short unk3 = izax.unk3.get(i); // Looks like a tile-id (item-drop?)
					short unk4 = izax.unk4.get(i);
					List<Short> unk6 = izax.unk5.get(i);

					//System.out.println(name + " " + gameContainer.TNAM.names.get(unk3) + " " + unk4 + " " + unk6);
				}
				
				characters += "<object name=\"" + name + "\" x=\"" + (izax.x.get(i) * 32) + "\" y=\"" + (izax.y.get(i) * 32) + "\" width=\"32\" height=\"32\"><properties>";
				characters += "<property name=\"characterId\" value=\"" + characterId + "\"/>";
				characters += "</properties></object>";
			}
			
			// TODO
			IZX4 izx4 = isYoda ? gameContainer.ZONE.IZX4.get(zoneId) : gameContainer.ZAX4.IZX4.get(zoneId);
			
			// Actions
			List<IACT> iacts = isYoda ? gameContainer.ZONE.IACT.get(zoneId) : gameContainer.ACTN.IACT.get(zoneId);
			if (iacts != null) {
				for (short actionId = 0; actionId < iacts.size(); actionId++) {
					IACT iact = iacts.get(actionId);
					String name = isYoda ? "" : gameContainer.ANAM.names.get(zoneId).get(actionId);
					//System.out.println(zoneId + " " + name);
				}
			}
			
			// Hotspots
			List<String> type = isYoda ? gameContainer.ZONE.type.get(zoneId) : gameContainer.HTSP.type.get(zoneId);
			List<Short> x = isYoda ? gameContainer.ZONE.x.get(zoneId) : gameContainer.HTSP.x.get(zoneId);
			List<Short> y = isYoda ? gameContainer.ZONE.y.get(zoneId) : gameContainer.HTSP.y.get(zoneId);
			List<Short> param = isYoda ? gameContainer.ZONE.param.get(zoneId) : gameContainer.HTSP.param.get(zoneId);			
			
			String hotspots = "";
			
			if (type != null) {
				for (int j = 0; j < type.size(); j++) {
					hotspots += "<object name=\"" + type.get(j) + "\" x=\"" + (x.get(j) * 32) + "\" y=\"" + (y.get(j) * 32) + "\" width=\"32\" height=\"32\"><properties>";
					
					if (param.get(j) >= 0) {
						characters += "<property name=\"param\" value=\"" + param.get(j) + "\"/>";
					}
					
					hotspots += "</properties></object>";
				}
			}
			
			// Properties
			String properties = "";
			
			if (gameContainer.ZNAM != null && gameContainer.ZNAM.names.containsKey(zoneId)) {
				properties += "<property name=\"name\" value=\"" + gameContainer.ZNAM.names.get(zoneId) + "\"/>";
			}
			
			// Entity Pools
			IZX2 izx2 = isYoda ? gameContainer.ZONE.IZX2.get(zoneId) : gameContainer.ZAX2.IZX2.get(zoneId);
			IZX3 izx3 = isYoda ? gameContainer.ZONE.IZX3.get(zoneId) : gameContainer.ZAX3.IZX3.get(zoneId);
			
			properties += "<property name=\"possibleQuestItems1\" value=\"";
			for (int i = 0; i < izax.tileId1.size(); i++) {
				properties += (i > 0 ? "," : "") + izax.tileId1.get(i);
			}
			properties += "\"/>";

			properties += "<property name=\"possibleQuestItems2\" value=\"";
			if (izax.tileId2.size() > 0) {
				for (int i = 0; i < izax.tileId2.size(); i++) {
					properties += (i > 0 ? "," : "") + izax.tileId2.get(i);
				}
			}
			properties += "\"/>";
			
			properties += "<property name=\"possibleQuestItems3\" value=\"";
			for (int i = 0; i < izx2.tileId.size(); i++) {
				properties += (i > 0 ? "," : "") + izx2.tileId.get(i);
			}
			properties += "\"/>";
		
			properties += "<property name=\"possibleQuestNpcs\" value=\"";
			for (int i = 0; i < izx3.tileId.size(); i++) {
				properties += (i > 0 ? "," : "") + izx3.tileId.get(i);
			}
			properties += "\"/>";
			
			// Output

		}
		*/
	}
}
