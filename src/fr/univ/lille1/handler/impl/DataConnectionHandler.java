package fr.univ.lille1.handler.impl;

import fr.univ.lille1.exception.ConnectionException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class provides methods to open and close a connection between
 * the client and the server to transfer data
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class DataConnectionHandler {

    private Socket dataSocket;
    private ServerSocket pasvSocket;

    private boolean activeMode;

    /**
     * Initialize the connection in active mode with the hostname and port sent by the client
     *
     * @param host the client's hostname
     * @param port the client's port
     */
    public void initializeActive(String host, int port) {
        close();
        this.activeMode = true;
        try {
            this.dataSocket = new Socket(host, port);
        } catch (UnknownHostException e) {
            throw new ConnectionException("DataConnectionHandler - Unknow host " + host + ":" + port);
        } catch (IOException e) {
            throw new ConnectionException("DataConnectionHandler - Error while opening a socket on " + host + ":" + port);
        }
    }

    /**
     * Initialize the connection in passive mode. The server wait the connection on the port
     * sent to the client.
     *
     * @param port the port sent to the client
     */
    public void initializePassive(int port) {
        close();
        this.activeMode = false;
        try {
            this.pasvSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new ConnectionException("DataConnectionHandler - Error while opening a socket on port : " + port);
        }
    }

    public Socket open() {
        if (!this.activeMode) {
            try {
                this.dataSocket = this.pasvSocket.accept();
            } catch (IOException e) {
                throw new ConnectionException("DataConnectionHandler - Error while opening a socket in passive mode");
            }
        }
        return this.dataSocket;
    }

    public void close() {
        if (this.dataSocket != null) {
            try {
                this.dataSocket.close();
            } catch (IOException ignore) {
            }
        }
        if (this.pasvSocket != null) {
            try {
                this.pasvSocket.close();
            } catch (IOException ignore) {
            }
        }
    }
}