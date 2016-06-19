package net.eiveo.original.sections.todo.not_converted_yet;

import java.util.ArrayList;
import java.util.List;

import net.eiveo.original.sections.todo.determine_values.IZX4;
import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ZAX4 {
	private List<IZX4> IZX4 = new ArrayList<>();
	
	public static ZAX4 read(SmartByteBuffer data) throws Exception {
		ZAX4 instance = new ZAX4();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		while (length + lengthStart > data.position()) {
			String izx4 = data.getString(-1, 4);
			
			if (!izx4.equals("IZX4")) {
				throw new Exception("IZX4 mismatch.");
			}

			instance.IZX4.add(net.eiveo.original.sections.todo.determine_values.IZX4.read(data));
		}

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("ZAX4");
		outputStream.sectionStart();

		for (IZX4 izx4 : this.IZX4) {
			izx4.write(outputStream);
		}
		
		byte[] section = outputStream.sectionEnd();
		outputStream.write(section.length);
		outputStream.write(section);
	}
}
