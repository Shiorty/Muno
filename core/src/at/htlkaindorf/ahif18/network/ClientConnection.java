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
    public void receiveTalk(String message) {
        sendTasks.push(() -> {
            MessageConverter.sendTalk(socket.getOutputStream(), "yu said: " + message);
        });
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

    public void sendPlayerJoined(PlayerInfo playerInfo) {
        sendTasks.push(() -> {
            MessageConverter.sendServerPlayerJoined(socket.getOutputStream(), playerInfo);
        });
    }

    public void sendPlayerLeft(PlayerInfo playerInfo){
        sendTasks.push(() -> {
            MessageConverter.sendServerPlayerLeft(socket.getOutputStream(), playerInfo);
        });
    }

    public void sendDrawnCard(Card card){
        sendTasks.push(() -> {
            MessageConverter.sendServerDrawCard(socket.getOutputStream(), card);
        });
    }

    public void sendPlayerUpdate(PlayerInfo playerInfo){
        sendTasks.push(() -> {
           MessageConverter.senderServerPlayerUpdate(socket.getOutputStream(), playerInfo);
        });
    }

    public void sendCurrentPlayerUpdate(int currentValue) {
        sendTasks.push(() -> {
            MessageConverter.sendServerCurrentPlayerUpdate(socket.getOutputStream(), currentValue);
        });
    }
}
