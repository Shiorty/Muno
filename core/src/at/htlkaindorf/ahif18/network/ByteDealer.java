package at.htlkaindorf.ahif18.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains static methods to send and receive data from the network
 *
 * Last changed: 2022-05-10
 * @author Andreas Kurz, Jan Mandl
 */
public class ByteDealer {

    /**
     * Reads n bytes from the InputStream
     * Blocks the thread until all bytes have been read
     * @param stream stream to be read from
     * @param n amount of bytes to be read
     * @return a byte[] containing all of the bytes
     * @throws IOException
     */
    public static byte[] readNBytes(InputStream stream, int n) throws IOException
    {
        if(n <= 0){
            return new byte[0];
        }

        byte[] bytes = new byte[n];

        int bytesRead = 0;
        while(bytesRead < n)
        {
            bytesRead += stream.read(bytes, bytesRead, n - bytesRead);
        }

        return bytes;
    }

    /**
     * Send a string over the network
     * @param stream destination of the string
     * @param s string to be send
     * @throws IOException
     */
    public static void sendString(OutputStream stream, String s) throws IOException
    {
        stream.write(ByteConverter.stringToByte(s));
    }

    /**
     * Reads a string from the InputStream<br>
     * Blocks until a string has been read
     * @param stream data source
     * @return the String
     * @throws IOException
     */
    public static String receiveString(InputStream stream) throws IOException
    {
        int length = ByteConverter.bytesToInt(readNBytes(stream, 4));
        return ByteConverter.byteToString(readNBytes(stream, length));
    }

    /**
     * Send a String[]
     * @param stream destination of the data
     * @param strings allStrings which should be sent
     * @throws IOException
     */
    public static void sendStringArray(OutputStream stream, String[] strings) throws IOException
    {
        byte[] length = ByteConverter.intToBytes(strings.length);
        stream.write(length);

        for(String s : strings)
        {
            sendString(stream, s);
        }
    }

    /**
     * Receive a String[]
     * @param stream Data Source
     * @return the String[] which has been read
     * @throws IOException
     */
    public static String[] receiveStringArray(InputStream stream) throws IOException
    {
        int length = ByteConverter.bytesToInt(readNBytes(stream, 4));

        String[] strings = new String[length];
        for(int i = 0; i < length; i++)
        {
            strings[i] = receiveString(stream);
        }

        return strings;
    }
}
