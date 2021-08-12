package com.toone.v3.platform.function;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Base64编码
 * @author tugx
 * 请使用com.toone.util.CryptoUtils封装后的方法
 */
class Base64
{

	private static final char S_BASE64CHAR[] = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
		'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
		'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
		'8', '9', '+', '/'
	};
//	private static final char S_BASE64PAD = 61;
	private static final byte S_DECODETABLE[];

	private Base64()
	{
	}

	private static int decode0(char ibuf[], byte obuf[], int wp)
	{
		int outlen = 3;
		if (ibuf[3] == '=')
			outlen = 2;
		if (ibuf[2] == '=')
			outlen = 1;
		int b0 = S_DECODETABLE[ibuf[0]];
		int b1 = S_DECODETABLE[ibuf[1]];
		int b2 = S_DECODETABLE[ibuf[2]];
		int b3 = S_DECODETABLE[ibuf[3]];
		switch (outlen)
		{
		case 1: // '\001'
			obuf[wp] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 3);
			return 1;

		case 2: // '\002'
			obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 3);
			obuf[wp] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
			return 2;

		case 3: // '\003'
			obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 3);
			obuf[wp++] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
			obuf[wp] = (byte)(b2 << 6 & 0xc0 | b3 & 0x3f);
			return 3;
		}
		throw new RuntimeException("Base64 Internal Error");
	}

	/**
	 * 字符数组数据解码
	 * @param data char[] 
	 * @param off 偏移量
	 * @param len 解码长度
	 * @return
	 */
	static byte[] decode(char data[], int off, int len)
	{
		char ibuf[] = new char[4];
		int ibufcount = 0;
		byte obuf[] = new byte[(len / 4) * 3 + 3];
		int obufcount = 0;
		for (int i = off; i < off + len; i++)
		{
			char ch = data[i];
			if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
				continue;
			ibuf[ibufcount++] = ch;
			if (ibufcount == ibuf.length)
			{
				ibufcount = 0;
				obufcount += decode0(ibuf, obuf, obufcount);
			}
		}

		if (obufcount == obuf.length)
		{
			return obuf;
		} else
		{
			byte ret[] = new byte[obufcount];
			System.arraycopy(obuf, 0, ret, 0, obufcount);
			return ret;
		}
	}

	/**
	 * base64字符串解码
	 * @param data @see String
	 * @return byte[]
	 */
	static byte[] decode(String data)
	{
		char ibuf[] = new char[4];
		int ibufcount = 0;
		byte obuf[] = new byte[(data.length() / 4) * 3 + 3];
		int obufcount = 0;
		for (int i = 0; i < data.length(); i++)
		{
			char ch = data.charAt(i);
			if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
				continue;
			ibuf[ibufcount++] = ch;
			if (ibufcount == ibuf.length)
			{
				ibufcount = 0;
				obufcount += decode0(ibuf, obuf, obufcount);
			}
		}

		if (obufcount == obuf.length)
		{
			return obuf;
		} else
		{
			byte ret[] = new byte[obufcount];
			System.arraycopy(obuf, 0, ret, 0, obufcount);
			return ret;
		}
	}

	/**
	 * 字符数组数据编码
	 * @param data char[]
	 * @param off 偏移量
	 * @param len 解码长度
	 * @param ostream @see OutputStream
	 * @throws IOException
	 */
	static void decode(char data[], int off, int len, OutputStream ostream)
		throws IOException
	{
		char ibuf[] = new char[4];
		int ibufcount = 0;
		byte obuf[] = new byte[3];
		for (int i = off; i < off + len; i++)
		{
			char ch = data[i];
			if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
				continue;
			ibuf[ibufcount++] = ch;
			if (ibufcount == ibuf.length)
			{
				ibufcount = 0;
				int obufcount = decode0(ibuf, obuf, 0);
				ostream.write(obuf, 0, obufcount);
			}
		}

	}

	/**
	 * 字符串解码
	 * @param data String
	 * @param ostream @see OutputStream
	 * @throws IOException
	 */
	static void decode(String data, OutputStream ostream)
		throws IOException
	{
		char ibuf[] = new char[4];
		int ibufcount = 0;
		byte obuf[] = new byte[3];
		for (int i = 0; i < data.length(); i++)
		{
			char ch = data.charAt(i);
			if (ch != '=' && (ch >= S_DECODETABLE.length || S_DECODETABLE[ch] == 127))
				continue;
			ibuf[ibufcount++] = ch;
			if (ibufcount == ibuf.length)
			{
				ibufcount = 0;
				int obufcount = decode0(ibuf, obuf, 0);
				ostream.write(obuf, 0, obufcount);
			}
		}

	}

	/**
	 * byte数组编码
	 * @param data
	 * @return
	 */
	static String encode(byte data[])
	{
		return encode(data, 0, data.length);
	}

	/**
	 * byte数组编码
	 * @param data byte[]
	 * @param off 偏移量
	 * @param len 解码长度
	 * @return
	 */
	static String encode(byte data[], int off, int len)
	{
		if (len <= 0)
			return "";
		char out[] = new char[(len / 3) * 4 + 4];
		int rindex = off;
		int windex = 0;
		int rest;
		for (rest = len - off; rest >= 3; rest -= 3)
		{
			int i = ((data[rindex] & 0xff) << 16) + ((data[rindex + 1] & 0xff) << 8) + (data[rindex + 2] & 0xff);
			out[windex++] = S_BASE64CHAR[i >> 18];
			out[windex++] = S_BASE64CHAR[i >> 12 & 0x3f];
			out[windex++] = S_BASE64CHAR[i >> 6 & 0x3f];
			out[windex++] = S_BASE64CHAR[i & 0x3f];
			rindex += 3;
		}

		if (rest == 1)
		{
			int i = data[rindex] & 0xff;
			out[windex++] = S_BASE64CHAR[i >> 2];
			out[windex++] = S_BASE64CHAR[i << 4 & 0x3f];
			out[windex++] = '=';
			out[windex++] = '=';
		} else
		if (rest == 2)
		{
			int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
			out[windex++] = S_BASE64CHAR[i >> 10];
			out[windex++] = S_BASE64CHAR[i >> 4 & 0x3f];
			out[windex++] = S_BASE64CHAR[i << 2 & 0x3f];
			out[windex++] = '=';
		}
		return new String(out, 0, windex);
	}

	/**
	 * byte数组数据编码
	 * @param data byte[]
	 * @param off 偏移量
	 * @param len 解码长度
	 * @param ostream @see OutputStream
	 * @throws IOException
	 */
	static void encode(byte data[], int off, int len, OutputStream ostream)
		throws IOException
	{
		if (len <= 0)
			return;
		byte out[] = new byte[4];
		int rindex = off;
		int rest;
		for (rest = len - off; rest >= 3; rest -= 3)
		{
			int i = ((data[rindex] & 0xff) << 16) + ((data[rindex + 1] & 0xff) << 8) + (data[rindex + 2] & 0xff);
			out[0] = (byte)S_BASE64CHAR[i >> 18];
			out[1] = (byte)S_BASE64CHAR[i >> 12 & 0x3f];
			out[2] = (byte)S_BASE64CHAR[i >> 6 & 0x3f];
			out[3] = (byte)S_BASE64CHAR[i & 0x3f];
			ostream.write(out, 0, 4);
			rindex += 3;
		}

		if (rest == 1)
		{
			int i = data[rindex] & 0xff;
			out[0] = (byte)S_BASE64CHAR[i >> 2];
			out[1] = (byte)S_BASE64CHAR[i << 4 & 0x3f];
			out[2] = 61;
			out[3] = 61;
			ostream.write(out, 0, 4);
		} else
		if (rest == 2)
		{
			int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
			out[0] = (byte)S_BASE64CHAR[i >> 10];
			out[1] = (byte)S_BASE64CHAR[i >> 4 & 0x3f];
			out[2] = (byte)S_BASE64CHAR[i << 2 & 0x3f];
			out[3] = 61;
			ostream.write(out, 0, 4);
		}
	}

	/**
	 * byte数组编码
	 * @param data byte[]
	 * @param off 偏移量
	 * @param len 解码长度
	 * @param writer @see Writer
	 * @throws IOException
	 */
	static void encode(byte data[], int off, int len, Writer writer)
		throws IOException
	{
		if (len <= 0)
			return;
		char out[] = new char[4];
		int rindex = off;
		int rest = len - off;
		int output = 0;
		do
		{
			if (rest < 3)
				break;
			int i = ((data[rindex] & 0xff) << 16) + ((data[rindex + 1] & 0xff) << 8) + (data[rindex + 2] & 0xff);
			out[0] = S_BASE64CHAR[i >> 18];
			out[1] = S_BASE64CHAR[i >> 12 & 0x3f];
			out[2] = S_BASE64CHAR[i >> 6 & 0x3f];
			out[3] = S_BASE64CHAR[i & 0x3f];
			writer.write(out, 0, 4);
			rindex += 3;
			rest -= 3;
			if ((output += 4) % 76 == 0)
				writer.write("\n");
		} while (true);
		if (rest == 1)
		{
			int i = data[rindex] & 0xff;
			out[0] = S_BASE64CHAR[i >> 2];
			out[1] = S_BASE64CHAR[i << 4 & 0x3f];
			out[2] = '=';
			out[3] = '=';
			writer.write(out, 0, 4);
		} else
		if (rest == 2)
		{
			int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
			out[0] = S_BASE64CHAR[i >> 10];
			out[1] = S_BASE64CHAR[i >> 4 & 0x3f];
			out[2] = S_BASE64CHAR[i << 2 & 0x3f];
			out[3] = '=';
			writer.write(out, 0, 4);
		}
	}

	static 
	{
		S_DECODETABLE = new byte[128];
		for (int i = 0; i < S_DECODETABLE.length; i++)
			S_DECODETABLE[i] = 127;

		for (int i = 0; i < S_BASE64CHAR.length; i++)
			S_DECODETABLE[S_BASE64CHAR[i]] = (byte)i;

	}
}
