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
 * Last Changed: 2022-06-03
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

        //Client wants to draw Card
        DRAW_CARD,

        //Server sends all information about Player (name, cardCount) + who is currently in posession of the ability to lay down cards on the table or otherwise interact with various gameplay elements to progress the game
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
                break;

            case CARD_PLAYED:
                Card cardPlayed = ByteDealer.receiveCard(inputStream);
                connection.receiveCardPlayed(cardPlayed);
                break;

            case DRAW_CARD:
                connection.drawCard();
                break;


        }
    }

    public static void receiveServerRequest(Client client, InputStream inputStream) throws IOException
    {
        MessageType type = getRequestType(inputStream);

        switch(type)
        {
            case INIT:
                int playerID = ByteDealer.receiveInt(inputStream);
                Card lastPlayedCard = ByteDealer.receiveCard(inputStream);
                List<Card> cards = ByteDealer.receiveCardList(inputStream);
                List<PlayerInfo> playerInfos = ByteDealer.receivePlayerInfoList(inputStream);
                client.receivedInit(playerID, lastPlayedCard, cards, playerInfos);
                break;

            case PLAYER_JOINED:
                PlayerInfo newPlayer = ByteDealer.receivePlayerInfo(inputStream);
                client.playerJoined(newPlayer);
                break;

            case TALK:
                String message = ByteDealer.receiveString(inputStream);
                client.receivedTalk(message);
                break;

            case CARD_PLAYED:
                PlayerInfo player = ByteDealer.receivePlayerInfo(inputStream);
                Card card = ByteDealer.receiveCard(inputStream);
                client.cardPlayed(player, card);

                break;

            case DRAW_CARD:
                Card drawnCard = ByteDealer.receiveCard(inputStream);
                client.cardDrawn(drawnCard);
                break;

            case PLAYER_UPDATE:
                PlayerInfo playerInfo = ByteDealer.receivePlayerInfo(inputStream);
                int currentPlayerID = ByteDealer.receiveInt(inputStream);
                client.playerUpdate(playerInfo, currentPlayerID);
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

    public static void sendServerDrawCard(OutputStream stream, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.DRAW_CARD);
        ByteDealer.sendCard(stream, card);
    }

    public static void senderServerPlayerUpdate(OutputStream stream, PlayerInfo playerInfo, int playerIDOfCurrentPlayer) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_UPDATE);
        ByteDealer.sendPlayerInfo(stream, playerInfo);
        ByteDealer.sendInt(stream, playerIDOfCurrentPlayer);
    }
}
