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

    public void receiveInit(String playerName)
    {
        server.playerJoined(playerID, playerName);
    }

    public void sendInitResponse(List<PlayerInfo> playerInfos, ArrayList<Card> cards) {
        sendTasks.push(() -> {
            try {
                MessageConverter.sendServerInit(socket.getOutputStream(), playerInfos, cards);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void playerJoined(PlayerInfo playerInfo) {
        sendTasks.push(() -> {
            try {
                MessageConverter.sendServerPlayerJoined(socket.getOutputStream(), playerInfo);
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
