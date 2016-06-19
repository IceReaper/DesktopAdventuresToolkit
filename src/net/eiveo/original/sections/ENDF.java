package net.eiveo.original.sections;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class ENDF {
	public static ENDF read(SmartByteBuffer data) throws Exception {
		ENDF instance = new ENDF();
		
		if (data.getInt() != 0 || data.position() != data.capacity()) {
			throw new Exception("Terminator mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) throws Exception {
		outputStream.write("ENDF");
		outputStream.write(0);
	}
}
