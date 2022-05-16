package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;

/**
 * Connection from the Server to a single Client
 *
 * Last changed: 2022-05-16
 * @author Andreas Kurz
 */
public class ClientConnection extends Thread {

    //Main Server
    private Server server;

    //Client information
    private Socket socket;
    private Card[] cards;
    private String name;

    public ClientConnection(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            startConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        System.out.println("Connection interrupted");

        socket.close();
        super.interrupt();
    }

    public void startConnection() throws Exception
    {
        RequestParser.sendInit(socket.getOutputStream(), Card.values());

        while(!isInterrupted())
        {
            String message = ByteDealer.receiveString(socket.getInputStream());
            System.out.println(message);

            ByteDealer.sendString(socket.getOutputStream(), "You said + " + message);
        }
    }
}
