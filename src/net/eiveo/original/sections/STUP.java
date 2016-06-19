package net.eiveo.original.sections;

import net.eiveo.utils.SmartByteArrayOutputStream;
import net.eiveo.utils.SmartByteBuffer;

public class STUP {
	public byte[] image = new byte[9 * 32 * 9 * 32];
	
	public static STUP read(SmartByteBuffer data) throws Exception {
		STUP instance = new STUP();
		
		int length = data.getInt();
		int lengthStart = data.position();
		
		data.get(instance.image);

		if (lengthStart + length != data.position()) {
			throw new Exception("Length mismatch.");
		}
		
		return instance;
	}

	public void write(SmartByteArrayOutputStream outputStream) {
		outputStream.write("STUP");
		outputStream.write(this.image.length);
		outputStream.write(this.image);
	}
}
