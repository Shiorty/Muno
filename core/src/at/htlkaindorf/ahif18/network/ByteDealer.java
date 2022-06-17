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
 * <br><br>
 * Last changed: 2022-06-03
 * @author Andreas Kurz, Jan Mandl
 */
public class ByteDealer {

    /**
     * Reads n bytes from the InputStream
     * Blocks the thread until all bytes have been read
     * @param stream stream to be read from
     * @param n amount of bytes to be read
     * @return a byte[] containing all the bytes
     * @throws IOException when the InputStream is closed
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
     * Sends a single integer over the network
     * @param stream Destination of the int
     * @param i integer to be sent
     * @throws IOException when a network error occurs
     */
    public static void sendInt(OutputStream stream, int i) throws IOException
    {
        stream.write(ByteConverter.intToBytes(i));
    }

    /**
     * Reds a single integer from the given InputStream<br>
     * Blocks until the int has been read
     * @param stream data source
     * @return the integer read
     * @throws IOException when a network error occurs
     */
    public static int receiveInt(InputStream stream) throws IOException
    {
        return ByteConverter.bytesToInt(readNBytes(stream, 4));
    }

    /**
     * Send a string over the network
     * @param stream destination of the string
     * @param s string to be sent
     * @throws IOException when a network error occurs
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
     * @throws IOException when a network error occurs
     */
    public static String receiveString(InputStream stream) throws IOException
    {
        int length = receiveInt(stream);
        return ByteConverter.byteToString(readNBytes(stream, length));
    }

    /**
     * Sends a Card over the network
     * @param stream destination of the string
     * @param card Card to be sent
     * @throws IOException when a network error occurs
     */
    public static void sendCard(OutputStream stream, Card card) throws IOException
    {
        sendString(stream, card.name());
    }

    /**
     * Reads a Card from the InputStream<br>
     * Blocks until a Card has been read
     * @param stream data source
     * @return the Card received
     * @throws IOException when a network error occurs
     */
    public static Card receiveCard(InputStream stream) throws IOException
    {
        return Card.valueOf(receiveString(stream));
    }

    /**
     * Sends a PlayerInfo over the network
     * @param stream destination of the playerInfo
     * @param playerInfo PlayerInfo to be sent
     * @throws IOException when a network error occurs
     */
    public static void sendPlayerInfo(OutputStream stream, PlayerInfo playerInfo) throws IOException
    {
        sendInt(stream, playerInfo.getPlayerID());
        sendString(stream, playerInfo.getPlayerName());
        sendInt(stream, playerInfo.getCardAmount());
    }

    /**
     * Reads a PlayerInfo from the InputStream<br>
     * Blocks until a PlayerInfo has been read
     * @param stream data source
     * @return the PlayerInfo received
     * @throws IOException when a network error occurs
     */
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

    /**
     * Interface containing a Method to receive an Object of Type T
     * @param <T> The type that can be received
     */
    public interface Receiver<T>{
        T receive(InputStream stream) throws IOException;
    }

    /**
     * Interface containing a Method to send an Object of Type T
     * @param <T> The type that can be sent
     */
    public interface Sender<T>{
        void send(OutputStream stream, T element) throws IOException;
    }

    /**
     * Generic method to receive a list of any type
     * @param stream data source
     * @param receiver Receiver interface that implements how the items are received
     * @return the received list
     * @param <T> the type of the list
     * @throws IOException when a network error occurs
     */
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

    /**
     * Generic method to send a list of any type
     * @param stream destination
     * @param sender specifies how the items are sent over the network
     * @param list the list to be sent
     * @param <T> the datatype of the list
     * @throws IOException when a network error occurs
     */
    public static <T> void sendList(OutputStream stream, Sender<T> sender, List<T> list) throws IOException {
        ByteDealer.sendInt(stream, list.size());

        for(T element : list){
            sender.send(stream, element);
        }
    }

    /**
     * Method used to receive a String List
     * @param stream data source
     * @return the list
     * @throws IOException when a network error occurs
     */
    public static List<String> receiveStringList(InputStream stream) throws IOException {
        return receiveList(stream, ByteDealer::receiveString);
    }

    /**
     * Method used to Send a String List
     * @param stream destination of the list
     * @param list the list to be sent
     * @throws IOException when a network error occurs
     */
    public static void sendStringList(OutputStream stream, List<String> list) throws IOException {
        sendList(stream, ByteDealer::sendString, list);
    }

    /**
     * Method used to receive a PlayerInfo List
     * @param stream data source
     * @return the list
     * @throws IOException when a network error occurs
     */
    public static List<PlayerInfo> receivePlayerInfoList(InputStream stream) throws IOException {
        return receiveList(stream, ByteDealer::receivePlayerInfo);
    }

    /**
     * Method used to Send a PlayerInfo List
     * @param stream destination of the list
     * @param list the list to be sent
     * @throws IOException when a network error occurs
     */
    public static void sendPlayerInfoList(OutputStream stream, List<PlayerInfo> list) throws IOException {
        sendList(stream, ByteDealer::sendPlayerInfo, list);
    }

    /**
     * Method used to Send a Card List
     * @param stream destination of the list
     * @param list the list to be sent
     * @throws IOException when a network error occurs
     */
    public static void sendCardList(OutputStream stream, List<Card> list) throws IOException
    {
        sendStringList(stream, list.stream().map(Card::name).collect(Collectors.toList()));
    }

    /**
     * Method used to receive a Card List
     * @param stream data source
     * @return the list
     * @throws IOException when a network error occurs
     */
    public static List<Card> receiveCardList(InputStream stream) throws IOException
    {
        return receiveStringList(stream)
                .stream()
                .map(Card::valueOf)
                .collect(Collectors.toList());
    }
}
