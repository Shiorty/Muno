package at.htlkaindorf.ahif18.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Contains static methods to send and receive data from the network
 *
 * Last changed: 2022.05.10
 * @author Andreas Kurz, Jan Mandl
 */
public class ByteDealer {

    public static byte[] readNBytes(InputStream stream, int n) throws IOException
    {
        if(n < 0){
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

    public static void sendString(OutputStream stream, String s) throws IOException
    {
        stream.write(ByteConverter.stringToByte(s));
    }

    public static String receiveString(InputStream stream) throws IOException
    {
        int length = ByteConverter.bytesToInt(readNBytes(stream, 4));
        return ByteConverter.byteToString(readNBytes(stream, length));
    }
}
