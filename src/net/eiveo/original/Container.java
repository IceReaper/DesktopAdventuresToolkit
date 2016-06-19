package net.eiveo.original;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import net.eiveo.original.sections.ENDF;
import net.eiveo.original.sections.SNDS;
import net.eiveo.original.sections.STUP;
import net.eiveo.original.sections.TILE;
import net.eiveo.original.sections.TNAM;
import net.eiveo.original.sections.VERS;
import net.eiveo.original.sections.todo.determine_values.CHWP;
import net.eiveo.original.sections.todo.determine_values.HTSP;
import net.eiveo.original.sections.todo.not_converted_yet.ACTN;
import net.eiveo.original.sections.todo.not_converted_yet.ANAM;
import net.eiveo.original.sections.todo.not_converted_yet.CAUX;
import net.eiveo.original.sections.todo.not_converted_yet.CHAR;
import net.eiveo.original.sections.todo.not_converted_yet.PNAM;
import net.eiveo.original.sections.todo.not_converted_yet.PUZ2;
import net.eiveo.original.sections.todo.not_converted_yet.ZAUX;
import net.eiveo.original.sections.todo.not_converted_yet.ZAX2;
import net.eiveo.original.sections.todo.not_converted_yet.ZAX3;
import net.eiveo.original.sections.todo.not_converted_yet.ZAX4;
import net.eiveo.original.sections.todo.not_converted_yet.ZNAM;
import net.eiveo.original.sections.todo.not_converted_yet.ZONE;
import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class Container {
	public VERS VERS; // VERSion

	public STUP STUP; // STartUP
	public SNDS SNDS; // SouNDS
	public TILE TILE; // TILEs
	
	public ZONE ZONE; // ZONEs
		// => IZON; // Items of ZONe
		// => IZAX; // Items of Zone AuXiliary 1 - Only in Yoda Stories
		// => IZX2; // Items of Zone AuXiliary 2 - Only in Yoda Stories
		// => IZX3; // Items of Zone AuXiliary 3 - Only in Yoda Stories
		// => IZX4; // Items of Zone AuXiliary 4 - Only in Yoda Stories
		// => IACT; // Items of ACTion - Only in Yoda Stories
	public ZAUX ZAUX; // Zone AUXiliary 1 - Not in Yoda Stories - has been merged into ZONE
		// => IZAX; // Items of Zone AuXiliary 1
	public ZAX2 ZAX2; // Zone AUXiliary 2 - Not in Yoda Stories - has been merged into ZONE
		// => IZX2; // Items of Zone AuXiliary 2
	public ZAX3 ZAX3; // Zone AUXiliary 3 - Not in Yoda Stories - has been merged into ZONE
		// => IZX3; // Items of Zone AuXiliary 3
	public ZAX4 ZAX4; // Zone AUXiliary 4 - Not in Yoda Stories - has been merged into ZONE
		// => IZX4; // Items of Zone AuXiliary 4
	public HTSP HTSP; // HoTSPots - Not in Yoda Stories - has been merged into ZONE
	public ACTN ACTN; // ACTioN - Not in Yoda Stories - has been merged into ZONE
		// => IACT; // Items of ACTion
	
	public PUZ2 PUZ2; // PUZzles
		// => IPUZ; // Items of PUZzle
	
	public CHAR CHAR; // CHARacters
		// => ICHA; // Items of CHAracters
	public CHWP CHWP; //CHaracter WeaPons
	public CAUX CAUX; // Character-AUXiliary
	
	public TNAM TNAM; // Tile NAMEs
	public ZNAM ZNAM; // Zone NAMEs - Not in Yoda Stories
	public PNAM PNAM; // Puzzle NAMEs - Not in Yoda Stories
	public ANAM ANAM; // Action NAMEs - Not in Yoda Stories
	
	public ENDF ENDF; // ENDFile

	public static Container read(SmartByteBuffer data, File root, boolean isYoda) throws Exception {
		Container instance = new Container();
		
		while (data.position() < data.capacity()) {
			String type = data.getString(-1, 4);

			switch (type) {
				case "VERS":
					instance.VERS = net.eiveo.original.sections.VERS.read(data);
					break;
				case "STUP":
					instance.STUP = net.eiveo.original.sections.STUP.read(data);
					break;
				case "SNDS":
					instance.SNDS = net.eiveo.original.sections.SNDS.read(data, isYoda, root);
					break;
				case "TILE":
					instance.TILE = net.eiveo.original.sections.TILE.read(data);
					break;
				case "ZONE":
					instance.ZONE = net.eiveo.original.sections.todo.not_converted_yet.ZONE.read(data, isYoda);
					break;
				case "ZAUX":
					instance.ZAUX = net.eiveo.original.sections.todo.not_converted_yet.ZAUX.read(data);
					break;
				case "ZAX2":
					instance.ZAX2 = net.eiveo.original.sections.todo.not_converted_yet.ZAX2.read(data);
					break;
				case "ZAX3":
					instance.ZAX3 = net.eiveo.original.sections.todo.not_converted_yet.ZAX3.read(data);
					break;
				case "ZAX4":
					instance.ZAX4 = net.eiveo.original.sections.todo.not_converted_yet.ZAX4.read(data);
					break;
				case "HTSP":
					instance.HTSP = net.eiveo.original.sections.todo.determine_values.HTSP.read(data);
					break;
				case "ACTN":
					instance.ACTN = net.eiveo.original.sections.todo.not_converted_yet.ACTN.read(data);
					break;
				case "PUZ2":
					instance.PUZ2 = net.eiveo.original.sections.todo.not_converted_yet.PUZ2.read(data, isYoda);
					break;
				case "CHAR":
					instance.CHAR = net.eiveo.original.sections.todo.not_converted_yet.CHAR.read(data, isYoda);
					break;
				case "CHWP":
					instance.CHWP = net.eiveo.original.sections.todo.determine_values.CHWP.read(data);
					break;
				case "CAUX":
					instance.CAUX = net.eiveo.original.sections.todo.not_converted_yet.CAUX.read(data);
					break;
				case "TNAM":
					instance.TNAM = net.eiveo.original.sections.TNAM.read(data, isYoda);
					break;
				case "ZNAM":
					instance.ZNAM = net.eiveo.original.sections.todo.not_converted_yet.ZNAM.read(data);
					break;
				case "PNAM":
					instance.PNAM = net.eiveo.original.sections.todo.not_converted_yet.PNAM.read(data);
					break;
				case "ANAM":
					instance.ANAM = net.eiveo.original.sections.todo.not_converted_yet.ANAM.read(data);
					break;
				case "ENDF":
					instance.ENDF = net.eiveo.original.sections.ENDF.read(data);
					break;
				default:
					throw new Exception("Unknown block: " + type);
			}
		}
		
		return instance;
	}

	public void write(File root, boolean isYoda) throws Exception {
		SmartByteArrayOutputStream outputStream = new SmartByteArrayOutputStream();

		this.VERS.write(outputStream);
		this.STUP.write(outputStream);
		this.SNDS.write(outputStream, isYoda, root);
		this.TILE.write(outputStream);
		if(this.ZONE != null)this.ZONE.write(outputStream, isYoda);
		
		if (!isYoda) {
			if(this.ZAUX != null)this.ZAUX.write(outputStream);
			if(this.ZAX2 != null)this.ZAX2.write(outputStream);
			if(this.ZAX3 != null)this.ZAX3.write(outputStream);
			if(this.ZAX4 != null)this.ZAX4.write(outputStream);
			if(this.HTSP != null)this.HTSP.write(outputStream);
			if(this.ACTN != null)this.ACTN.write(outputStream);
		}
		
		if(this.PUZ2 != null)this.PUZ2.write(outputStream, isYoda);
		if(this.CHAR != null)this.CHAR.write(outputStream, isYoda);
		if(this.CHWP != null)this.CHWP.write(outputStream);
		if(this.CAUX != null)this.CAUX.write(outputStream);
		this.TNAM.write(outputStream, isYoda);
		
		if (!isYoda) {
			if(this.ZNAM != null)this.ZNAM.write(outputStream);
			if(this.PNAM != null)this.PNAM.write(outputStream);
			if(this.ANAM != null)this.ANAM.write(outputStream);
		}
		
		this.ENDF.write(outputStream);
		
		Files.write(new File(root, isYoda ? "yodesk.dta" : "DESKTOP.DAW").toPath(), outputStream.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}
}
