package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.ConcurrentQueue;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Connection from the Server to a single Client
 *
 * Last changed: 2022-06-03
 * @author Andreas Kurz
 */
public class ClientConnection extends Thread {

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
        try {
            startConnection();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        //System.out.println("Connection interrupted");

        //System.out.println("Receive Thread interrupted");
        receiveThread.interrupt();

        socket.close();
        super.interrupt();
    }

    public void startConnection() throws Exception
    {
        receiveThread = new Thread(this::receiveTask);
        receiveThread.start();

        while(!isInterrupted())
        {
            SendTask task = sendTasks.pop();
            task.execute();
        }
    }

    public void receiveTask()
    {
        try
        {
            while(!Thread.currentThread().isInterrupted())
            {
                MessageConverter.receiveClientRequest(this, this.socket.getInputStream());
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public void receiveInit(String playerName)
    {
        server.playerJoined(playerID, playerName);
    }

    public void receiveCardPlayed(Card cardPlayed)
    {
        server.cardPlayed(playerID, cardPlayed);
    }

    public void sendCardPlayed(PlayerInfo player, Card card)
    {
        sendTasks.push(() -> {
            MessageConverter.sendServerCardPlayer(socket.getOutputStream(), player, card);
        });
    }

    public void sendInitResponse(Card lastPlayedCard, List<PlayerInfo> playerInfos, ArrayList<Card> cards) {
        //System.out.println("ClientConnection:sendInitResponse -> ID: " + playerID + ": " + playerInfos);
        sendTasks.push(() -> {
            MessageConverter.sendServerInit(playerID, socket.getOutputStream(), lastPlayedCard, playerInfos, cards);
        });
    }

    public void playerJoined(PlayerInfo playerInfo) {
        sendTasks.push(() -> {
            MessageConverter.sendServerPlayerJoined(socket.getOutputStream(), playerInfo);
        });
    }

    public void receivedTalk(String message){
        sendTasks.push(() -> {
            MessageConverter.sendTalk(socket.getOutputStream(), "yu said: " + message);
        });
    }

    public void drawCard() {
        server.drawCard(this.playerID);
    }

    public void sendDrawnCard(Card card){
        sendTasks.push(() -> {
            MessageConverter.sendServerDrawCard(socket.getOutputStream(), card);
        });
    }

    public void sendPlayerUpdate(PlayerInfo playerInfo, int playerIDOfCurrentPlayer){
        sendTasks.push(() -> {
           MessageConverter.senderServerPlayerUpdate(socket.getOutputStream(), playerInfo, playerIDOfCurrentPlayer);
        });
    }
}
