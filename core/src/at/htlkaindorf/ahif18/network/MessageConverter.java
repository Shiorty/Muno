package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.PlayerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides Static methods to create and receive different Message Types
 *
 * Last Changed: 2022-06-16
 * @author Andreas Kurz, Jan Mandl
 */
public class MessageConverter {

    public enum MessageType {
        INIT,
        END,

        //Server informs Clients of other client disconnect
        PLAYER_JOINED,
        PLAYER_LEAVE,

        //Send a string message
        TALK,

        //Send when a player plays a Card
        CARD_PLAYED,

        //Client wants to draw Card
        DRAW_CARD,

        //Server sends all information about Player (name, cardCount)
        PLAYER_UPDATE,

        CURRENT_PLAYER_UPDATE //contains information about who is currently in possession of the ability to lay down cards on the table or otherwise interact with various gameplay elements to progress the game
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


    public interface ClientMessageListener{
        void receiveInit(String playerName);
        void receiveEnd();
        void receiveTalk(String message);
        void receiveCardPlayed(Card cardPlayed);
        void receiveDrawCard();
    }

    public static void receiveClientRequest(ClientMessageListener listener, InputStream inputStream) throws IOException
    {
        MessageType type = getRequestType(inputStream);

        switch(type)
        {
            case INIT:
                String playerName = ByteDealer.receiveString(inputStream);
                listener.receiveInit(playerName);
                break;

            case END:
                listener.receiveEnd();
                break;

            case TALK:
                String message = ByteDealer.receiveString(inputStream);
                listener.receiveTalk(message);
                break;

            case CARD_PLAYED:
                Card cardPlayed = ByteDealer.receiveCard(inputStream);
                listener.receiveCardPlayed(cardPlayed);
                break;

            case DRAW_CARD:
                listener.receiveDrawCard();
                break;
        }
    }

    public interface ServerMessageListener{
        void receiveInit(int playerID, Card lastPlayedCard, List<Card> cards, List<PlayerInfo> playerInfos);
        void receivePlayerJoined(PlayerInfo newPlayer);
        void receivePlayerLeft(PlayerInfo deadPlayer);
        void receiveTalk(String message);
        void receiveCardPlayed(PlayerInfo player, Card card);
        void receiveCardDrawn(Card card);
        void receivePlayerUpdate(PlayerInfo playerInfo);
        void receiveCurrentPlayerUpdate(int currentPlayer);
    }

    public static void receiveServerRequest(ServerMessageListener listener, InputStream inputStream) throws IOException
    {
        MessageType type = getRequestType(inputStream);

        switch(type)
        {
            case INIT:
                int playerID = ByteDealer.receiveInt(inputStream);
                Card lastPlayedCard = ByteDealer.receiveCard(inputStream);
                List<Card> cards = ByteDealer.receiveCardList(inputStream);
                List<PlayerInfo> playerInfos = ByteDealer.receivePlayerInfoList(inputStream);
                listener.receiveInit(playerID, lastPlayedCard, cards, playerInfos);
                break;

            case PLAYER_JOINED:
                PlayerInfo newPlayer = ByteDealer.receivePlayerInfo(inputStream);
                listener.receivePlayerJoined(newPlayer);
                break;

            case PLAYER_LEAVE:
                PlayerInfo deadPlayer = ByteDealer.receivePlayerInfo(inputStream);
                listener.receivePlayerLeft(deadPlayer);
                break;

            case TALK:
                String message = ByteDealer.receiveString(inputStream);
                listener.receiveTalk(message);
                break;

            case CARD_PLAYED:
                PlayerInfo player = ByteDealer.receivePlayerInfo(inputStream);
                Card card = ByteDealer.receiveCard(inputStream);
                listener.receiveCardPlayed(player, card);
                break;

            case DRAW_CARD:
                Card drawnCard = ByteDealer.receiveCard(inputStream);
                listener.receiveCardDrawn(drawnCard);
                break;

            case PLAYER_UPDATE:
                PlayerInfo playerInfo = ByteDealer.receivePlayerInfo(inputStream);
                listener.receivePlayerUpdate(playerInfo);
                break;

            case CURRENT_PLAYER_UPDATE:
                int currentPlayer = ByteDealer.receiveInt(inputStream);
                listener.receiveCurrentPlayerUpdate(currentPlayer);
                break;
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

    public static void sendClientEnd(OutputStream stream) throws IOException
    {
        sendRequestType(stream, MessageType.END);
    }

    public static void sendClientCardPlayed(OutputStream stream, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.CARD_PLAYED);
        ByteDealer.sendCard(stream, card);
    }

    public static void sendClientDrawCard(OutputStream stream) throws IOException
    {
        sendRequestType(stream, MessageType.DRAW_CARD);
    }



    //--- Server ----//
    public static void sendServerCardPlayer(OutputStream stream, PlayerInfo currentPlayer, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.CARD_PLAYED);
        ByteDealer.sendPlayerInfo(stream, currentPlayer);
        ByteDealer.sendCard(stream, card);
    }

    public static void sendServerInit(int playerID, OutputStream stream, Card lastPlayedCard, List<PlayerInfo> otherPlayers, ArrayList<Card> cards) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);
        ByteDealer.sendInt(stream, playerID);
        ByteDealer.sendCard(stream, lastPlayedCard);
        ByteDealer.sendCardList(stream, cards);
        ByteDealer.sendPlayerInfoList(stream, otherPlayers);
    }

    public static void sendServerPlayerJoined(OutputStream stream, PlayerInfo otherPlayers) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_JOINED);
        ByteDealer.sendPlayerInfo(stream, otherPlayers);
    }

    public static void sendServerPlayerLeft(OutputStream stream, PlayerInfo player) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_LEAVE);
        ByteDealer.sendPlayerInfo(stream, player);
    }

    public static void sendServerDrawCard(OutputStream stream, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.DRAW_CARD);
        ByteDealer.sendCard(stream, card);
    }

    public static void senderServerPlayerUpdate(OutputStream stream, PlayerInfo playerInfo) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_UPDATE);
        ByteDealer.sendPlayerInfo(stream, playerInfo);
    }

    public static void sendServerCurrentPlayerUpdate(OutputStream stream, int currentPlayerID) throws IOException
    {
        sendRequestType(stream, MessageType.CURRENT_PLAYER_UPDATE);
        ByteDealer.sendInt(stream, currentPlayerID);
    }
}
