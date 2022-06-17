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
 * <br><br>
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

    /**
     * Method used to send the Request Type
     * @param outputStream destination of the Request
     * @param type Request Type to be sent
     * @throws IOException when a network error occurs
     */
    private static void sendRequestType(OutputStream outputStream, MessageType type) throws IOException
    {
        ByteDealer.sendString(outputStream, type.name());
    }

    /**
     * Method used to receive a Request Type<br>
     * Blocks the thread until a RequestType is received
     * @param stream data source
     * @return the request type
     * @throws IOException when a network error occurs
     */
    private static MessageType getRequestType(InputStream stream) throws IOException
    {
        String headerString = ByteDealer.receiveString(stream);
        return MessageType.valueOf(headerString);
    }

    /**
     * Gets notified of a Client Request
     */
    public interface ClientMessageListener{
        /**
         * Gets called when a client connects
         * @param playerName the name of the player
         */
        void receiveInit(String playerName);

        /**
         * Gets called when a client wants to disconnect
         */
        void receiveEnd();

        /**
         * Gets called when a client wants to play a card
         * @param cardPlayed card to be played
         */
        void receiveCardPlayed(Card cardPlayed);

        /**
         * Gets called when a client draws a card
         */
        void receiveDrawCard();
    }

    /**
     * Receives and handles client request
     * @param listener decides how to handle the request
     * @param inputStream data source
     * @throws IOException when a network error occurs
     */
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

            case CARD_PLAYED:
                Card cardPlayed = ByteDealer.receiveCard(inputStream);
                listener.receiveCardPlayed(cardPlayed);
                break;

            case DRAW_CARD:
                listener.receiveDrawCard();
                break;
        }
    }

    /**
     * Gets notified of a ServerMessage
     */
    public interface ServerMessageListener{
        /**
         * Handles the server init response
         * @param playerID the ID of the player
         * @param lastPlayedCard the last played card
         * @param cards the card of the player
         * @param playerInfos playerInfo of all players
         */
        void receiveInit(int playerID, Card lastPlayedCard, List<Card> cards, List<PlayerInfo> playerInfos);

        /**
         * Handles Server information that another player joined
         * @param newPlayer information about the new player
         */
        void receivePlayerJoined(PlayerInfo newPlayer);

        /**
         * Handles Server information that another player left the game
         * @param deadPlayer information about the effected player
         */
        void receivePlayerLeft(PlayerInfo deadPlayer);

        /**
         * Handles Server information that a player played a card
         * @param player the effected player
         * @param card card which has been played
         */
        void receiveCardPlayed(PlayerInfo player, Card card);

        /**
         * Handles Server information about the card that has been drawn
         * @param card the card that has been drawn
         */
        void receiveCardDrawn(Card card);

        /**
         * Handles Server Request about the update of player information
         * @param playerInfo the updated player information
         */
        void receivePlayerUpdate(PlayerInfo playerInfo);

        /**
         * Handles Sever information about the current player changing
         * @param currentPlayer the new current player
         */
        void receiveCurrentPlayerUpdate(int currentPlayer);
    }

    /**
     * Receives and handles Server request
     * @param listener decides how to handle the request
     * @param inputStream data source
     * @throws IOException when a network error occurs
     */
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


    //---- Client ----//

    /**
     * Sends an init message to the server
     * @param stream the destination
     * @param name the name of the player
     * @throws IOException when a network error occurs
     */
    public static void sendClientInit(OutputStream stream, String name) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);
        ByteDealer.sendString(stream, name);
    }

    /**
     * Sends an End message to the server
     * @param stream the destination
     * @throws IOException when a network error occurs
     */
    public static void sendClientEnd(OutputStream stream) throws IOException
    {
        sendRequestType(stream, MessageType.END);
    }

    /**
     * Sends a Card Played message to the server
     * @param stream the destination
     * @param card the card that has been played
     * @throws IOException when a network error occurs
     */
    public static void sendClientCardPlayed(OutputStream stream, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.CARD_PLAYED);
        ByteDealer.sendCard(stream, card);
    }

    /**
     * Requests to draw a card
     * @param stream the destination
     * @throws IOException when a network error occurs
     */
    public static void sendClientDrawCard(OutputStream stream) throws IOException
    {
        sendRequestType(stream, MessageType.DRAW_CARD);
    }



    //--- Server ----//
    /**
     * Sends information that a card has been played
     * @param stream the destination
     * @param currentPlayer the player who played the card
     * @param card the card that has been played
     * @throws IOException when a network error occurs
     */
    public static void sendServerCardPlayer(OutputStream stream, PlayerInfo currentPlayer, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.CARD_PLAYED);
        ByteDealer.sendPlayerInfo(stream, currentPlayer);
        ByteDealer.sendCard(stream, card);
    }

    /**
     * Sends a Init reply to the client
     * @param playerID the ID of the client
     * @param stream the destination of the message
     * @param lastPlayedCard the card that has last been played
     * @param otherPlayers information about all players
     * @param cards the cards of the player
     * @throws IOException when a network error occurs
     */
    public static void sendServerInit(int playerID, OutputStream stream, Card lastPlayedCard, List<PlayerInfo> otherPlayers, List<Card> cards) throws IOException
    {
        sendRequestType(stream, MessageType.INIT);
        ByteDealer.sendInt(stream, playerID);
        ByteDealer.sendCard(stream, lastPlayedCard);
        ByteDealer.sendCardList(stream, cards);
        ByteDealer.sendPlayerInfoList(stream, otherPlayers);
    }

    /**
     * Sends the information that a Client has joined the game
     * @param stream the destination
     * @param otherPlayers information about the new client
     * @throws IOException when a network error occurs
     */
    public static void sendServerPlayerJoined(OutputStream stream, PlayerInfo otherPlayers) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_JOINED);
        ByteDealer.sendPlayerInfo(stream, otherPlayers);
    }

    /**
     * Sends the information that a Client has left the game
     * @param stream the destination of the message
     * @param player information about the leaving player
     * @throws IOException when a network error occurs
     */
    public static void sendServerPlayerLeft(OutputStream stream, PlayerInfo player) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_LEAVE);
        ByteDealer.sendPlayerInfo(stream, player);
    }

    /**
     * Sends which card has been drawn
     * @param stream the destination of the message
     * @param card the card that has been drawn by the client
     * @throws IOException when a network error occurs
     */
    public static void sendServerDrawCard(OutputStream stream, Card card) throws IOException
    {
        sendRequestType(stream, MessageType.DRAW_CARD);
        ByteDealer.sendCard(stream, card);
    }

    /**
     * Sends updated information about a particular client
     * @param stream the destination of the message
     * @param playerInfo updated information about the client
     * @throws IOException when a network error occurs
     */
    public static void senderServerPlayerUpdate(OutputStream stream, PlayerInfo playerInfo) throws IOException
    {
        sendRequestType(stream, MessageType.PLAYER_UPDATE);
        ByteDealer.sendPlayerInfo(stream, playerInfo);
    }

    /**
     * Sends who is currently in possession of the ability to lay down cards on the table or otherwise interact with various gameplay elements to progress the game
     * @param stream the destination of the messsage
     * @param currentPlayerID the player id of the new current client
     * @throws IOException when a network error occurs
     */
    public static void sendServerCurrentPlayerUpdate(OutputStream stream, int currentPlayerID) throws IOException
    {
        sendRequestType(stream, MessageType.CURRENT_PLAYER_UPDATE);
        ByteDealer.sendInt(stream, currentPlayerID);
    }
}
