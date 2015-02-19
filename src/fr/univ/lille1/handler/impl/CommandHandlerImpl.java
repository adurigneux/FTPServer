package fr.univ.lille1.handler.impl;

import fr.univ.lille1.exception.ConnectionException;
import fr.univ.lille1.handler.CommandHandler;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.request.impl.CommandRequestImpl;
import fr.univ.lille1.utils.ReturnCode;

import java.io.*;
import java.net.Socket;

/**
 * CommandHandlerImpl contains the reference of the input/output stream to receive/send
 * messages from/to the client.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class CommandHandlerImpl implements CommandHandler {
    private Socket socket;
    private BufferedReader inputStream;
    private BufferedWriter outputStream;


    public CommandHandlerImpl(Socket socketConnection) {
        this.socket = socketConnection;
    }

    /**
     * Save the reference of the input and output stream.
     */
    @Override
    public void init() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new ConnectionException("CommandHandler - Impossible to initialize reader and writer.");
        }

    }

    /**
     * Close the socket and input/output stream.
     */
    @Override
    public void close() {

        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new ConnectionException("CommandHandler - Impossible to close reader, writer and socket.");
        }


    }

    /**
     * Send a message to the client.
     *
     * @param message Message sent to the client.
     */
    @Override
    public void sendMessage(String message) {

        try {
            outputStream.write(message);
            outputStream.flush();
        } catch (IOException e) {
            throw new ConnectionException("CommandHandler - Impossible to send message.");
        }
    }

    /**
     * Used to receive messages from a client.
     */
    @Override
    public CommandRequest receiveMessage() {
        try {
            return new CommandRequestImpl(inputStream.readLine());
        } catch (IOException e) {
            throw new ConnectionException("CommandHandler - Impossible to receive message.");
        }
    }

    /**
     * Send a preformatted message to the client.
     *
     * @param message Preformatted message to send.
     */
    public void sendMessage(ReturnCode message) {

        sendMessage(message.toString());
    }
}
