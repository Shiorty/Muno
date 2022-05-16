package at.htlkaindorf.ahif18.network;

import java.util.Arrays;

public class ByteConverter {

    public static byte intToByte(int value){
        return (byte) (value - 128);
    }

    public static int byteToInt(byte value){
        return (int) (value + 128);
    }

    public static byte[] intToBytes(int value)
    {
        byte[] bytes = new byte[4];

        for(int k = 1; k <= bytes.length; k++)
        {
            bytes[bytes.length - k] = intToByte(value);
            value >>= 8;
        }

        return bytes;
    }

    public static int bytesToInt(byte[] bytes, int offset, int length)
    {
        int result = 0;

        for (int i = offset; i < offset + length; ++i) {
            result <<= 8;
            result += byteToInt(bytes[i]);
        }

        return result;
    }

    public static int bytesToInt(byte[] bytes)
    {
        return bytesToInt(bytes, 0, bytes.length);
    }

    public static byte[] stringToByte(String string)
    {
        byte[] length = intToBytes(string.length());
        byte[] data = string.getBytes();
        return merge(length, data);
    }

    public static String byteToString(byte[] bytes)
    {
        return new String(bytes);
    }

    public static byte[] merge(byte[]... arrays)
    {
        int outputLength = Arrays.stream(arrays)
                            .mapToInt((byte[] array) -> array.length)
                            .sum();

        byte[] output = Arrays.copyOf(arrays[0], outputLength);
        int outputPosition = arrays[0].length;

        for (int i = 1; i < arrays.length; i++)
        {
            System.arraycopy(arrays[i], 0, output, outputPosition, arrays[i].length);
            outputPosition += arrays[i].length;
        }

        return output;
    }
}
