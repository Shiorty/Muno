package at.htlkaindorf.ahif18.network;

import at.htlkaindorf.ahif18.data.Card;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Server Thread handling new Clients
 * When a new client connects a new ClientConnection Thread is opened
 *
 * Last changed: 2022-05-16
 * @author Andreas Kurz; Jan Mandl
 */
public class Server extends Thread{

    public static final int PORT = 60000;

    private ServerSocket socket;

    //Game Information
    private Queue<Card> stack;
    private int currentPlayer;

    //Player Connections
    private List<ClientConnection> connections;

    public Server()
    {
        connections = new ArrayList<>();
    }

    @Override
    public void run()
    {
        try {
            runServer();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void interrupt() {
        System.out.println("Server Interrupted");

        connections.forEach(Thread::interrupt);
        socket.close();
        super.interrupt();
    }

    public void runServer() throws Exception
    {
        socket = new ServerSocket(PORT);

        while(!isInterrupted())
        {
            Socket clientSocket = socket.accept();

            ClientConnection connection = new ClientConnection(clientSocket);
            connections.add(connection);
            connection.start();
        }
    }
}
