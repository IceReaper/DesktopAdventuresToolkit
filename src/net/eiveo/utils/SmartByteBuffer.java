package net.eiveo.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SmartByteBuffer {
	private ByteBuffer byteBuffer;

	public int capacity() {
		return this.byteBuffer.capacity();
	}

	public byte get() {
		return this.byteBuffer.get();
	}

	public void get(byte[] dst) {
		this.byteBuffer.get(dst);
	}

	public int getInt() {
		return this.byteBuffer.getInt();
	}

	public short getShort() {
		return this.byteBuffer.getShort();
	}

	public String getString() {
		String string = "";
		
		while (true) {
			byte character = this.get();
			if ((character & 0xff) == 0) {
				break;
			} else {
				string += new String(new byte[] { character });
			}
		}
		
		return string;
	}

	public String getString(int index, int length) {
		byte[] output = new byte[length];
		if (index > -1) {
			this.position(index);
		}
		this.byteBuffer.get(output);
		return new String(output);
	}

	public void order(ByteOrder bo) {
		this.byteBuffer.order(bo);
	}

	public int position() {
		return this.byteBuffer.position();
	}
	
	public void position(int newPosition) {
		this.byteBuffer.position(newPosition);
	}

	public static SmartByteBuffer wrap(byte[] array) {
		SmartByteBuffer instance = new SmartByteBuffer();
		instance.byteBuffer = ByteBuffer.wrap(array);
		return instance;
	}
}
