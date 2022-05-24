package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                String playerName = ByteDealer.receiveString(inputStream);
                connection.receiveInit(playerName);
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
                List<Card> cards = ByteDealer.receiveCardList(inputStream);
                List<PlayerInfo> playerInfos = ByteDealer.receivePlayerInfoList(inputStream);
                client.receivedInit(cards, playerInfos);
                break;

            case PLAYER_JOINED:
                PlayerInfo newPlayer = ByteDealer.receivePlayerInfo(inputStream);
                client.playerJoined(newPlayer);
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

    public static void sendClientInit(OutputStream stream, String name) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);
        ByteDealer.sendString(stream, name);
    }

    public static void sendServerInit(OutputStream stream, List<PlayerInfo> otherPlayers, ArrayList<Card> cards) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);
        ByteDealer.sendCardList(stream, cards);
        ByteDealer.sendPlayerInfoList(stream, otherPlayers);
    }

    public static void sendServerPlayerJoined(OutputStream stream, PlayerInfo otherPlayers) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_JOINED);
        ByteDealer.sendPlayerInfo(stream, otherPlayers);
    }
}
