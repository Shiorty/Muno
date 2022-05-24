package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Provides Static methods to create and receive different Message Types
 *
 * Last Changed: 2022-05-16
 * @author Andreas Kurz
 */
public class MessageConverter {

    public enum MessageType {
        INIT,

        //Server informs Clients of other client disconnect
        PLAYER_JOINED,
        PLAYER_LEAVE,

        //Send a string message
        TALK,

        //Send when a player plays a Card
        CARD_PLAYED,

        //Server sends all information about Player (name, cardCount)
        PLAYER_UPDATE
    }

    private static void sendRequestType(OutputStream outputStream, MessageType type) throws IOException
    {
        ByteDealer.sendString(outputStream, type.name());
    }

    private static MessageType getRequestType(InputStream stream) throws IOException
    {
        String headerString = ByteDealer.receiveString(stream);
        return MessageType.valueOf(headerString);
    }




    public static void receiveClientRequest(ClientConnection connection, InputStream inputStream) throws IOException
    {
        MessageType type = getRequestType(inputStream);

        switch(type)
        {
            case INIT:
                String playerName = "Max";
                connection.receiveInit();
                break;

            case TALK:
                String message = ByteDealer.receiveString(inputStream);
                connection.receivedTalk(message);
        }
    }

    public static void receiveServerRequest(Client client, InputStream inputStream) throws IOException
    {
        MessageType type = getRequestType(inputStream);

        switch(type)
        {
            case INIT:
                Card[] cards = receiveCardArray(inputStream);
                client.receivedInit(cards);
                break;

            case TALK:
                String message = ByteDealer.receiveString(inputStream);
                client.receivedTalk(message);
        }
    }

    public static void sendTalk(OutputStream stream, String message) throws IOException
    {
        sendRequestType(stream, MessageType.TALK);
        ByteDealer.sendString(stream, message);
    }

    public static void sendClientInit(OutputStream stream) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);
    }

    public static void sendServerInit(OutputStream stream, Card[] cards) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);

        String[] cardNames = Arrays.stream(cards).map(Card::name).toArray(String[]::new);
        ByteDealer.sendStringArray(stream, cardNames);
    }

    public static Card[] receiveCardArray(InputStream stream) throws IOException
    {
        return Arrays.stream(ByteDealer.receiveStringArray(stream)).map(Card::valueOf).toArray(Card[]::new);
    }
}
