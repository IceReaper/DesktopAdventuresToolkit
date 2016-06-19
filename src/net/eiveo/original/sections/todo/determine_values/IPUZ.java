package net.eiveo.original.sections.todo.determine_values;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class IPUZ {
	private short type;
	private short unk1;
	private short unk2;
	private short unk3;
	private short unk4;
	private short unk5;
	private String[] texts = new String[4];
	private short tileId;
	private short unk6;

	public static IPUZ read(SmartByteBuffer data, boolean isYoda) throws Exception {
		IPUZ instance = new IPUZ();
		
		int length = data.getInt();
		int lengthStart = data.position();

		instance.type = data.getShort(); // indy used -1 instead of 0 for a single quest - bug?
		instance.unk1 = data.getShort(); // indy used -1 instead of 0 for a single quest - bug?
		instance.unk2 = data.getShort(); // TODO what is this? type?
		data.getShort(); // 0
		
		if (isYoda) {
			instance.unk3 = data.getShort(); // TODO what is this? low value like type, but can be -1 so its optional.
			instance.unk4 = data.getShort(); // 0 or -1
		}

		instance.unk5 = data.getShort(); // TODO what is this? looks like flags
		
		for (int i = 0; i < 4; i++) {
			short strlen = data.getShort();
			instance.texts[i] = data.getString(-1, strlen);
		}
		
		data.getShort(); // 0

		instance.tileId = data.getShort();
		
		if (isYoda) {
			instance.unk6 = data.getShort(); // TODO what is this? looks like binary data, maybe tileId2?
		}
		
		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) throws Exception {
		outputStream.write("IPUZ");
		outputStream.sectionStart();

		outputStream.write(this.type);
		outputStream.write(this.unk1);
		outputStream.write(this.unk2);
		outputStream.write((short) 0);

		if (isYoda) {
			outputStream.write(this.unk3);
			outputStream.write(this.unk4);
		}
		
		outputStream.write(this.unk5);
		
		for (int i = 0; i < 4; i++) {
			outputStream.write((short) this.texts[i].length());
			outputStream.write(this.texts[i]);
		}

		outputStream.write((short) 0);
		outputStream.write(this.tileId);
		
		if (isYoda) {
			outputStream.write(this.unk6);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
