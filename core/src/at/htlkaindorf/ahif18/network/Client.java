package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.ConcurrentQueue;
import at.htlkaindorf.ahif18.data.PlayerInfo;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Connection from Client to Server
 * Sending and Receiving are handled in seperate threads
 *
 * Last changed: 2022-06-03
 * @author Andreas Kurz; Jan Mandl
 */
public class Client extends Thread{

    public static final String SERVER_IP = "127.0.0.1";

    private ConcurrentQueue<SendTask> sendTasks = new ConcurrentQueue<>();

    private Thread receiveThread;
    private Socket serverConnection;
    private String message;
    private NetworkBuffer networkBuffer;

    private int playerID;


    public Client(NetworkBuffer nwb){
        this(nwb, "I <3 cats");
    }

    public Client(NetworkBuffer nwb, String message){
        networkBuffer = nwb;
        Thread.currentThread().setName(message);
        this.message = message;
    }

    @Override
    public void run() {
        try {
            startClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        System.out.println("Client interrupted");

        serverConnection.close();
        super.interrupt();
    }

    public void startClient() throws Exception
    {
        serverConnection = new Socket(SERVER_IP, Server.PORT);
        receiveThread = new Thread(this::receiveTask);
        receiveThread.start();

        MessageConverter.sendClientInit(serverConnection.getOutputStream(), message);

        while(!isInterrupted())
        {
            sendTasks.pop().execute();
        }
    }

    public void receiveTask()
    {
        try
        {
            while(!Thread.currentThread().isInterrupted())
            {
                MessageConverter.receiveServerRequest(this, serverConnection.getInputStream());
            }
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public void receivedInit(int playerID, Card lastPlayedCard, List<Card> cards, List<PlayerInfo> otherPlayers) {
        this.playerID = playerID;

        networkBuffer.setCards(cards);
        networkBuffer.setLastPlayedCard(lastPlayedCard);
        networkBuffer.setPlayers(otherPlayers);

        System.out.println("Client: " + Thread.currentThread().getName());

        System.out.println(message + " " + lastPlayedCard);
        System.out.println(message + " " + cards);
        System.out.println(message + " " + otherPlayers);
    }

    public void receivedTalk(String message) {
        System.out.println(message);
    }

    public void playerJoined(PlayerInfo newPlayer) {
        System.out.println(message + " New Player Joined: " + newPlayer);
    }

    public void playCard(Card card) {
        sendTasks.push(() -> {
            MessageConverter.sendClientCardPlayed(serverConnection.getOutputStream(), card);
        });
    }

    public void drawCard(){
        sendTasks.push(() -> {
            MessageConverter.sendClientDrawCard(serverConnection.getOutputStream());
        });
    }

    public void cardPlayed(PlayerInfo player, Card card) {
        networkBuffer.setLastPlayedCard(card);

        if(this.playerID == player.getPlayerID())
        {
            //THATS ME!
            networkBuffer.removeCard(card);
        }
        else
        {
            //Thats not me :(
            System.out.println("Thats not me :(");

            networkBuffer.playerUpdate(player);
        }
    }

    public void cardDrawn(Card drawnCard) {
        networkBuffer.addCard(drawnCard);
    }
}