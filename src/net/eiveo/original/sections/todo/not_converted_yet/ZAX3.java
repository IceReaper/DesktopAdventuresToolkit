package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ZAX3 {
	private List<IZX3> IZX3 = new ArrayList<>();
	
	public static ZAX3 read(SmartByteBuffer data) throws Exception {
		ZAX3 instance = new ZAX3();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		while (length + lengthStart > data.position()) {
			String izx3 = data.getString(-1, 4);
			
			if (!izx3.equals("IZX3")) {
				throw new Exception("IZX3 mismatch.");
			}

			instance.IZX3.add(net.eiveo.original.sections.todo.not_converted_yet.IZX3.read(data));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ZAX3");
		outputStream.sectionStart();

		for (IZX3 izx3 : this.IZX3) {
			izx3.write(outputStream);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
