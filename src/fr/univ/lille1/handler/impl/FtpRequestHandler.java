package fr.univ.lille1.handler.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.client.impl.ClientSessionImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server thread which accept connection on a specific port, it initialize each
 * ClientSession and start each ClientHandler thread.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class FtpRequestHandler implements Runnable {
    public static int port = 0;
    private String directoryPath;
    private int clientCounter;

    public FtpRequestHandler(int port, String directoryPath) {
        this.port = port;
        this.directoryPath = directoryPath;
        this.clientCounter = 1;
    }

    @Override
    public void run() {

        ServerSocket s = null;
        try {
            s = new ServerSocket(port);

            while (true) {

                Socket clientSocket = s.accept();
                ClientSession clientSession = new ClientSessionImpl(
                        clientSocket, directoryPath, clientCounter);
                new ClientHandler(clientSession).start();

                clientCounter++;
            }
        } catch (IOException e) {
            System.out.println("FtpRequestHandler - " + e.getMessage());
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                // Nothing to display, the server is closed
            }
        }

    }

}
