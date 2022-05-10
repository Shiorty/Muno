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

        while(!isInterrupted())
        {
            ByteDealer.sendString(serverConnection.getOutputStream(), message);

            ByteDealer.sendString(serverConnection.getOutputStream(), "You said + " + message);

            Thread.sleep(1000);
        }
    }
}
