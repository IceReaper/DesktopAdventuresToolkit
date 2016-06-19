package net.eiveo;

import java.io.File;

public class Toolkit {
	public static void main(String[] args) {
		try {
			System.out.println("Yoda Stories");
			Unpacker.unpack(new File("data/yoda/yodesk.dta"), new File("data/yoda/yodesk.exe"));
//			Packer.pack(new File("output/yoda-unpacked"), new File("data/yoda/yodesk.exe"), true);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		System.out.println("");
		
		try {
			System.out.println("Indiana Jones Desktop Adventures");
			Unpacker.unpack(new File("data/indy/desktop.daw"), new File("data/indy/deskadv.exe"));
//			Packer.pack(new File("output/indy-unpacked"), new File("data/indy/deskadv.exe"), false);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		// TODO implement Extractor.patch - Should patch bugs still left in unaltered archive:
		// - TILE: YODA(1746-1752) && INDY(384) => subtypeId = 1
		// - TILE: see if the subtype for weapons can be changed, makes no sense how they are!
		// - ICHA: See below!
		/* idle, action 1-4, extra 1-3, icon
		if (isYoda && instance.name.equals("Generator")) {
			instance.tileIdleEast = instance.tileIdleNorth;
		} else if (isYoda && instance.name.equals("CeilingFan")) {
			instance.tileActionNorth1 = -1;
		} else if (isYoda && instance.name.equals("SandWorm")) {
			instance.tileIdleEast = instance.tileExtra3;
		} else if (isYoda && instance.name.equals("DesertBeetle")) {
			instance.tileIdleEast = instance.tileExtra3;
		} else if (isYoda && instance.name.equals("Sarlacc")) {
			instance.tileIdleWest = instance.tileIdleEast;
			instance.tileIdleEast = instance.tileExtra3;
		} else if (isYoda && instance.name.equals("BatPatrol")) {
			instance.tileIdleNorth = 1742;
		} else if (isYoda && instance.name.equals("Imperial Blaste")) {
			instance.tileActionNorth1 = 1036;
			instance.tileActionNorth2 = 1036;
			instance.tileActionEast1 = 1038;
			instance.tileActionEast2 = 1038;
			instance.tileActionSouth1 = 1035;
			instance.tileActionSouth2 = 1035;
			instance.tileActionWest1 = 1037;
			instance.tileActionWest2 = 1037;
		} else if (isYoda && instance.name.equals("EvilForce")) {
			instance.tileActionNorth1 = 1780;
			instance.tileActionNorth2 = 1780;
			instance.tileActionEast1 = 1779;
			instance.tileActionEast2 = 1779;
			instance.tileActionSouth1 = 1781;
			instance.tileActionSouth2 = 1781;
			instance.tileActionWest1 = 1778;
			instance.tileActionWest2 = 1778;
			// TODO change color to red! (this is required to use the evil force :D)
			instance.tileItemIcon = 511;
		} else if (!isYoda && instance.name.equals("WhipWeapon")) {
			instance.tileActionNorth4 = instance.tileActionNorth3;
			instance.tileActionNorth3 = 480;
		}

		// All Characters have bugged entries - removing them.
		if (instance.type.equals("player") || instance.type.equals("npc")) {
			instance.tileItemIcon = -1;
			instance.tileExtra1 = -1;
			instance.tileExtra2 = -1;
			instance.tileExtra3 = -1;
		}
		
		// Player has more bugged entries - removing them.
		if (instance.type.equals("player")) {
			instance.tileActionNorth3 = -1;
			instance.tileActionNorth4 = -1;
			instance.tileActionEast3 = -1;
			instance.tileActionEast4 = -1;
			instance.tileActionSouth3 = -1;
			instance.tileActionSouth4 = -1;
			instance.tileActionWest3 = -1;
			instance.tileActionWest4 = -1;
		}*/
	}
}
