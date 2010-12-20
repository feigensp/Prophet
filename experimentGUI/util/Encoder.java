package experimentGUI.util;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Encoder {

	private static byte[] xor(byte[] input, byte key) {
		byte[] result = new byte[input.length];
		for (int i = 0; i < input.length; i++) {
			result[i] = (byte) (input[i] ^ key);
		}	
		return result;
	}
	
	public static String encode(String s, byte key) {
		return new BASE64Encoder().encode(xor(s.getBytes(), key));
	}

	public static String decode(String s, byte key) {
    try {
		return new String(xor(new BASE64Decoder().decodeBuffer(s), key));
	} catch (IOException e) {
		return null;
	}
  }
}
