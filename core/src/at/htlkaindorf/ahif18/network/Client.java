package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;

/**
 * Connection from Client to Server
 * Sending and Receiving are handled in seperate threads
 *
 * Last changed: 2022-05-16
 * @author Andreas Kurz; Jan Mandl
 */
public class Client extends Thread{

    public static final String SERVER_IP = "127.0.0.1";

    private Thread receiveThread;

    private Socket serverConnection;
    private String message;

    public Client(String message){
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

        MessageConverter.sendClientInit(serverConnection.getOutputStream());

        while(!isInterrupted())
        {
            MessageConverter.sendTalk(serverConnection.getOutputStream(), message);
            Thread.sleep(1000);
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

    public void receivedInit(Card[] cards) {
        for(Card card : cards){
            System.out.println(card.name());
        }
    }

    public void receivedTalk(String message) {
        System.out.println(message);
    }
}
