package at.htlkaindorf.ahif18.network;

import java.util.Arrays;

/**
 * Converts various data types to and from byte[]
 *
 * Last changed: 2022-05-10
 * @author Andreas Kurz; Jan Mandl
 */
public class ByteConverter {

    /**
     * Converts a integer into a byte<br>
     * 128 gets subtracted from the value
     * @param value integer to be converted
     * @return byte representing the integer
     */
    public static byte intToByte(int value){
        return (byte) (value - 128);
    }

    /**
     * Converts a byte into a integer<br>
     * 128 gets added to the value
     * @param value byte to be converted
     * @return int representing the integer
     */
    public static int byteToInt(byte value){
        return (int) (value + 128);
    }

    /**
     * Converts a given integer into a byte[] with size of 4
     * @param value integer to be converted
     * @return byte[] representing the integer
     */
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

    /**
     * Converts a byte[] back into a integer
     * The size and the offset can be set
     * @param bytes byte[] to be converted
     * @param offset starting point of the byte[]
     * @param length amount of bytes to be converted
     * @return
     */
    public static int bytesToInt(byte[] bytes, int offset, int length)
    {
        int result = 0;

        for (int i = offset; i < offset + length; ++i) {
            result <<= 8;
            result += byteToInt(bytes[i]);
        }

        return result;
    }

    /**
     * Converts a byte[] into a integer<br>
     * Converts the byte[] from beginning to end<br>
     * Is the same as calling bytesToInt(bytes, 0, bytes.length)
     * @param bytes bytes array to be converted
     * @return int
     */
    public static int bytesToInt(byte[] bytes)
    {
        return bytesToInt(bytes, 0, bytes.length);
    }

    /**
     * Converts a string into a byte[]
     * The first 4 bytes are the length of the string
     * @param string string to be converted
     * @return byte[] containing the length and data of the string
     */
    public static byte[] stringToByte(String string)
    {
        byte[] length = intToBytes(string.length());
        byte[] data = string.getBytes();
        return merge(length, data);
    }

    /**
     * Converts a byte[] into a string
     * @param bytes bytes containing the data of the string
     * @return new String with bytes[] as its data
     */
    public static String byteToString(byte[] bytes)
    {
        return new String(bytes);
    }

    /**
     * Merges multiple byte[] together
     * @param arrays a array of byte[] which should be merged
     * @return a single byte[] containing the data of all byte[]s
     */
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
