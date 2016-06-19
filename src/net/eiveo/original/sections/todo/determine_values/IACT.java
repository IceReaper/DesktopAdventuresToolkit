package net.eiveo.original.sections.todo.determine_values;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class IACT {
	private List<Condition> conditions = new ArrayList<>();
	private List<Action> actions = new ArrayList<>();

	public static IACT read(SmartByteBuffer data, boolean isYoda) throws Exception {
		IACT instance = new IACT();
		// TODO: https://github.com/cyco/DeskFun/blob/master/dfengine/include/Action.hpp
		// https://github.com/a-kr/jsyoda/blob/master/yodesk_decompiler/GenView/Script.cs

		int length = data.getInt();
		int lengthStart = data.position();

		short amount = data.getShort();
		
		for (int i = 0; i < amount; i++) {
			instance.conditions.add(Condition.read(data));
		}

		amount = data.getShort();
		
		for (int i = 0; i < amount; i++) {
			instance.actions.add(Action.read(data));
		}

		if (isYoda && lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream, boolean isYoda) {
		outputStream.write("IACT");
		outputStream.sectionStart();

		outputStream.write((short) this.conditions.size());
		
		for (Condition condition : this.conditions) {
			condition.write(outputStream);
		}

		outputStream.write((short) this.actions.size());
		
		for (Action action : this.actions) {
			action.write(outputStream);
		}

		byte[] section = outputStream.sectionEnd();
		outputStream.write(isYoda ? section.length : 0);
		outputStream.write(section);
	}
	
	public static class Condition {
		private short unk1;
		private short unk2;
		private short unk3;
		private short unk4;
		private short unk5;
		private short unk6;
		private String string;
		
		public static Condition read(SmartByteBuffer data) {
			Condition instance = new Condition();

			instance.unk1 = data.getShort(); // TODO type
			instance.unk2 = data.getShort(); // TODO x
			instance.unk3 = data.getShort(); // TODO y
			instance.unk4 = data.getShort(); // TODO data
			instance.unk5 = data.getShort(); // TODO data
			instance.unk6 = data.getShort(); // TODO data
			
			short stringLength = data.getShort();
			instance.string = data.getString(-1, stringLength);

			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream) {
			outputStream.write(this.unk1);
			outputStream.write(this.unk2);
			outputStream.write(this.unk3);
			outputStream.write(this.unk4);
			outputStream.write(this.unk5);
			outputStream.write(this.unk6);
			outputStream.write((short) this.string.length());
			outputStream.write(this.string);
		}
	}
	
	public static class Action {
		private short unk1;
		private short unk2;
		private short unk3;
		private short unk4;
		private short unk5;
		private short unk6;
		private String string;
		
		public static Action read(SmartByteBuffer data) {
			Action instance = new Action();
			
			instance.unk1 = data.getShort(); // TODO type
			instance.unk2 = data.getShort(); // TODO x
			instance.unk3 = data.getShort(); // TODO y
			instance.unk4 = data.getShort(); // TODO data
			instance.unk5 = data.getShort(); // TODO data
			instance.unk6 = data.getShort(); // TODO data
			
			short stringLength = data.getShort();
			instance.string = data.getString(-1, stringLength);

			return instance;
		}

		public void write(SmartByteArrayOutputStream outputStream) {
			outputStream.write(this.unk1);
			outputStream.write(this.unk2);
			outputStream.write(this.unk3);
			outputStream.write(this.unk4);
			outputStream.write(this.unk5);
			outputStream.write(this.unk6);
			outputStream.write((short) this.string.length());
			outputStream.write(this.string);
		}
	}
}
