package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ZAX2 {
	private List<IZX2> IZX2 = new ArrayList<>();
	
	public static ZAX2 read(SmartByteBuffer data) throws Exception {
		ZAX2 instance = new ZAX2();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		while (length + lengthStart > data.position()) {
			String izx2 = data.getString(-1, 4);
			
			if (!izx2.equals("IZX2")) {
				throw new Exception("IZX2 mismatch.");
			}

			instance.IZX2.add(net.eiveo.original.sections.todo.not_converted_yet.IZX2.read(data));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ZAX2");
		outputStream.sectionStart();

		for (IZX2 izx2 : this.IZX2) {
			izx2.write(outputStream);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
