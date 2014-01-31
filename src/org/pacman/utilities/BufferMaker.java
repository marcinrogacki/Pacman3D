package org.pacman.utilities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 
 * @author Marcin Rogacki
 *
 */
public class BufferMaker {	
	public static FloatBuffer createFloat(final float vec[]) {
		int bytesPerFloat = 4;
		ByteBuffer bb = ByteBuffer.allocateDirect(vec.length * bytesPerFloat);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = bb.asFloatBuffer();
		buffer.put(vec);
		buffer.position(0);
		return buffer;
	}
	
	public static ByteBuffer createByte(final byte vec[]) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(vec.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(vec);
		buffer.position(0);
		return buffer;
	}
}
