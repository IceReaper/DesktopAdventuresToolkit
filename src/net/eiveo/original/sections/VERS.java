package net.eiveo.original.sections;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class VERS {
	public static VERS read(SmartByteBuffer data) throws Exception {
		VERS instance = new VERS();
		
		if (data.getInt() != 512) {
			throw new Exception("Version mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("VERS");
		outputStream.write(512);
	}
}
