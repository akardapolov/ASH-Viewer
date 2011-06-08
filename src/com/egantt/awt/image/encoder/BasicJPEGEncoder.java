package com.egantt.awt.image.encoder;

import com.egantt.awt.image.ImageEncoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;

/**
 *  This class uses the com.sun jpeg encoder which is shipped with the Sun JRE
 *  it is uncertain as with all com.sun whether this can be considererd 100%
 *  pure java for this reason.
 */
public class BasicJPEGEncoder implements ImageEncoder
{
	private static final String MIME_TYPE = "image/jpeg";

	private static final byte [] EMPTY_SET = new byte[0];

	protected static BasicJPEGEncoder instance;

	// __________________________________________________________________________

	public static ImageEncoder getInstance()
	{
		if (instance == null)
			instance = new BasicJPEGEncoder();
		return instance;
	}

	// __________________________________________________________________________

	public String getType()
	{
		return MIME_TYPE;
	}

	// __________________________________________________________________________

	public byte[] encode(BufferedImage image)
	{
		ByteOutputStream out = new ByteOutputStream(0); // needs to calculate based on the data banks
		try
		{
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
			encoder.encode(image, param);
		}
		catch (IOException io)
		{
			return EMPTY_SET;
		}
		return null;
	}

	// __________________________________________________________________________

	protected final class ByteOutputStream extends OutputStream
	{
		/**
		 * The buffer where data is stored.
		 */
		protected byte buffer[];

		/**
		 * The number of valid bytes in the buffer.
		 */
		protected int count;

		/**
		 * Creates a new byte array output stream. The buffer capacity is
		 * initially the value you specify though its size increases if necessary.
		 * it is quite inefficient in doing so.
		 */
		public ByteOutputStream(int size)
		{
			this.buffer = new byte[size];
		}

		// _______________________________________________________________________

		/**
		 * Writes the specified byte to this byte array output stream.
		 * @param value the byte to be written.
		 */
		public synchronized void write(int value)
		{
			int size = count + 1;
			if (size > buffer.length)
			{
				byte newBuffer[] = new byte[size];
				System.arraycopy(buffer, 0, newBuffer, 0, count);
				buffer = newBuffer;
			}
			buffer[count] = (byte) value;
			count = size;
		}
	}
}
