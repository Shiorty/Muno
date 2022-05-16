package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends Thread{

    public static final String SERVER_IP = "127.0.0.1";

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

        Card[] cards = RequestParser.receiveInit(serverConnection.getInputStream());

        System.out.println("Client received: ");
        for(Card card : cards){
            System.out.println(card.name());
        }

        while(!isInterrupted())
        {
            ByteDealer.sendString(serverConnection.getOutputStream(), message);

            String reply = ByteDealer.receiveString(serverConnection.getInputStream());
            System.out.println(reply);

            Thread.sleep(1000);
        }
    }
}
