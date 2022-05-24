package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import at.htlkaindorf.ahif18.data.ConcurrentQueue;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 * Connection from the Server to a single Client
 *
 * Last changed: 2022-05-16
 * @author Andreas Kurz
 */
public class ClientConnection extends Thread {

    private Thread receiveThread;

    //Main Server
    private Server server;

    private int playerID;

    private ConcurrentQueue<Runnable> sendTasks = new ConcurrentQueue<>();

    //Client information
    private Socket socket;

    public ClientConnection(Socket socket, int playerID)
    {
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
        System.out.println("Connection interrupted");

        System.out.println("Receive Thread interrupted");
        receiveThread.interrupt();

        socket.close();
        super.interrupt();
    }

    public void startConnection() throws Exception
    {
        receiveThread = new Thread(this::receiveTask);
        receiveThread.start();

        MessageConverter.sendServerInit(socket.getOutputStream(), Card.values());

        while(!isInterrupted())
        {
            Runnable r = sendTasks.pop();
            r.run();
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

    public Card[] generateNewCards(int amount)
    {
        Random r = new Random();

        Card[] cards = new Card[amount];
        for(int i = 0; i < amount; i++){
            cards[i] = Card.values()[r.nextInt(Card.values().length)];
        }

        return cards;
    }

    public void receiveInit()
    {
        sendTasks.push(() -> {
            try {
                MessageConverter.sendServerInit(socket.getOutputStream(), generateNewCards(10));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void receivedTalk(String message){
        sendTasks.push(() -> {
            try {
                MessageConverter.sendTalk(socket.getOutputStream(), "yu said: " + message);
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
