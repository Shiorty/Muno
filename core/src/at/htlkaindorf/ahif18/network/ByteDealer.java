package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains static methods to send and receive data from the network
 *
 * Last changed: 2022-06-03
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

    public static void sendInt(OutputStream stream, int i) throws IOException
    {
        stream.write(ByteConverter.intToBytes(i));
    }

    public static int receiveInt(InputStream stream) throws IOException
    {
        return ByteConverter.bytesToInt(readNBytes(stream, 4));
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
        int length = receiveInt(stream);
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

    public static void sendCard(OutputStream stream, Card card) throws IOException
    {
        sendString(stream, card.name());
    }

    public static Card receiveCard(InputStream stream) throws IOException
    {
        return Card.valueOf(receiveString(stream));
    }

    public static void sendPlayerInfo(OutputStream stream, PlayerInfo playerInfo) throws IOException
    {
        sendInt(stream, playerInfo.getPlayerID());
        sendString(stream, playerInfo.getPlayerName());
        sendInt(stream, playerInfo.getCardAmount());
    }

    public static PlayerInfo receivePlayerInfo(InputStream stream) throws IOException
    {
        return new PlayerInfo(
                receiveInt(stream),
                receiveString(stream),
                receiveInt(stream)
        );
    }

//    /**
//     * Receive a String[]
//     * @param stream Data Source
//     * @return the String[] which has been read
//     * @throws IOException
//     */

    public interface Receiver<T>{
        public T receive(InputStream stream) throws IOException;
    }

    public interface Sender<T>{
        public void send(OutputStream stream, T element) throws IOException;
    }

    public static <T> List<T> receiveList(InputStream stream, Receiver<T> receiver) throws IOException
    {
        int length = receiveInt(stream);

        List<T> values = new ArrayList(length);
        for(int i = 0; i < length; i++)
        {
            values.add(receiver.receive(stream));
        }

        return values;
    }

    public static <T> void sendList(OutputStream stream, Sender<T> sender, List<T> list) throws IOException {
        ByteDealer.sendInt(stream, list.size());

        for(T element : list){
            sender.send(stream, element);
        }
    }

    public static List<String> receiveStringList(InputStream stream) throws IOException {
        return receiveList(stream, ByteDealer::receiveString);
    }

    public static void sendStringList(OutputStream stream, List<String> list) throws IOException {
        sendList(stream, ByteDealer::sendString, list);
    }

    public static List<PlayerInfo> receivePlayerInfoList(InputStream stream) throws IOException {
        return receiveList(stream, ByteDealer::receivePlayerInfo);
    }

    public static void sendPlayerInfoList(OutputStream stream, List<PlayerInfo> list) throws IOException {
        sendList(stream, ByteDealer::sendPlayerInfo, list);
    }

    public static void sendCardList(OutputStream stream, List<Card> list) throws IOException
    {
        sendStringList(stream, list.stream().map(Card::name).collect(Collectors.toList()));
    }

    public static List<Card> receiveCardList(InputStream stream) throws IOException
    {
        return receiveStringList(stream)
                .stream()
                .map(Card::valueOf)
                .collect(Collectors.toList());
    }
}
