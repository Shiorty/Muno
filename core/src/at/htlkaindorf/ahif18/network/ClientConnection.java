package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.ConcurrentQueue;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Connection from the Server to a single Client
 *
 * <br><br>
 * Last changed: 2022-06-16
 * @author Andreas Kurz
 */
public class ClientConnection extends Thread implements MessageConverter.ClientMessageListener{

    private Thread receiveThread;

    //Main Server
    private Server server;

    private int playerID;

    private ConcurrentQueue<SendTask> sendTasks = new ConcurrentQueue<>();

    //Client information
    private Socket socket;

    public ClientConnection(Server server, Socket socket, int playerID)
    {
        this.server = server;
        this.socket = socket;
        this.playerID = playerID;
    }

    @Override
    public void run() {
        startConnection();
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        receiveThread.interrupt();

        socket.close();
        super.interrupt();
    }

    /**
     * Starts the connection with the client<br>
     * Will also start a Thread to handle all Client Requests
     */
    public void startConnection()
    {
        receiveThread = new Thread(this::receiveTask);
        receiveThread.start();

        while(!isInterrupted())
        {
            try
            {
                SendTask task = sendTasks.pop();
                task.execute();
            }
            catch (InterruptedException | IOException e) {
                clientLeft();
                return;
            }
        }
    }

    /**
     * Starts an infinite receive loop
     */
    public void receiveTask()
    {
        try
        {
            while(!Thread.currentThread().isInterrupted())
            {
                MessageConverter.receiveClientRequest(this, this.socket.getInputStream());
            }
        }
        catch(Exception ioException)
        {
            ioException.printStackTrace();
            clientLeft();
        }
    }

    /**
     * Tells the server to close the connection<br>
     * Gets called when the client leaves or otherwise disconnects
     */
    public void clientLeft() {
        System.out.println("Client dead");
        server.playerLeft(playerID);
    }

    @Override
    public void receiveInit(String playerName)
    {
        server.playerJoined(playerID, playerName);
    }

    @Override
    public void receiveEnd() {
        clientLeft();
    }

    @Override
    public void receiveCardPlayed(Card cardPlayed)
    {
        server.cardPlayed(playerID, cardPlayed);
    }

    @Override
    public void receiveDrawCard() {
        server.drawCard(this.playerID);
    }


    //---- Send Methods ----//
    /**
     * Sends the Response to the client init
     * @param lastPlayedCard the card that has last been played
     * @param playerInfos information about all players
     * @param cards the cards of the player
     */
    public void sendInitResponse(Card lastPlayedCard, List<PlayerInfo> playerInfos, List<Card> cards) {
        //System.out.println("ClientConnection:sendInitResponse -> ID: " + playerID + ": " + playerInfos);
        sendTasks.push(() -> {
            MessageConverter.sendServerInit(playerID, socket.getOutputStream(), lastPlayedCard, playerInfos, cards);
        });
    }

    /**
     * Sends information that a new player joined
     * @param playerInfo information about the new player
     */
    public void sendPlayerJoined(PlayerInfo playerInfo) {
        sendTasks.push(() -> {
            MessageConverter.sendServerPlayerJoined(socket.getOutputStream(), playerInfo);
        });
    }

    /**
     * Sends information about a client leaving the game
     * @param playerInfo the client who wants to leave
     */
    public void sendPlayerLeft(PlayerInfo playerInfo) {
        sendTasks.push(() -> {
            MessageConverter.sendServerPlayerLeft(socket.getOutputStream(), playerInfo);
        });
    }

    /**
     * Sends a Card Played Message to the client
     * @param player information about the player who played the card
     * @param card the card which has been played
     */
    public void sendCardPlayed(PlayerInfo player, Card card)
    {
        sendTasks.push(() -> {
            MessageConverter.sendServerCardPlayer(socket.getOutputStream(), player, card);
        });
    }

    /**
     * Sends the card drawn by a client
     * @param card the card that has been drawn
     */
    public void sendDrawnCard(Card card){
        sendTasks.push(() -> {
            MessageConverter.sendServerDrawCard(socket.getOutputStream(), card);
        });
    }

    /**
     * Sends updated information about a client
     * @param playerInfo the updated information
     */
    public void sendPlayerUpdate(PlayerInfo playerInfo){
        sendTasks.push(() -> {
           MessageConverter.senderServerPlayerUpdate(socket.getOutputStream(), playerInfo);
        });
    }

    /**
     * Sends a currentPlayer update
     * @param currentValue ID of the player who is currently in possession of the ability to lay down cards on the table or otherwise interact with various gameplay elements to progress the game
     */
    public void sendCurrentPlayerUpdate(int currentValue) {
        sendTasks.push(() -> {
            MessageConverter.sendServerCurrentPlayerUpdate(socket.getOutputStream(), currentValue);
        });
    }
}
