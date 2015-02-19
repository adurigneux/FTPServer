package fr.univ.lille1.client.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.handler.CommandHandler;
import fr.univ.lille1.handler.impl.CommandHandlerImpl;
import fr.univ.lille1.handler.impl.DataConnectionHandler;

import java.net.Socket;

/**
 * Implementation of the ClientSession. This class contains every client's informations.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class ClientSessionImpl implements ClientSession {
    private int clientId;
    private String username;
    private String currentPath;
    private boolean connected = false;
    private boolean usernameCorrect = false;
    private boolean passwordCorrect = false;
    private Socket connectionSocket;
    private CommandHandler commandHandler;
    private DataConnectionHandler dataConnectionHandler;
    private boolean binaryFlag = false;
    private boolean isReadOnly = false;

    public ClientSessionImpl(Socket socket, String path, int clientId) {
        this.connectionSocket = socket;
        this.clientId = clientId;
        this.currentPath = path;
        this.connected = true;

        this.commandHandler = new CommandHandlerImpl(this.connectionSocket);
        this.dataConnectionHandler = new DataConnectionHandler();
    }

    public void init() {
        this.commandHandler.init();
    }


    public int getClientId() {
        return clientId;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void quitSession() {
        this.close();
    }

    public Socket getConnectionSocket() {
        return connectionSocket;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public DataConnectionHandler getDataConnectionHandler() {
        return this.dataConnectionHandler;
    }

    public void close() {
        this.commandHandler.close();
        this.connected = false;
    }

    public boolean isUsernameCorrect() {
        return usernameCorrect;
    }

    public void setUsernameCorrect(boolean usernameCorrect) {
        this.usernameCorrect = usernameCorrect;
    }

    public boolean isPasswordCorrect() {
        return passwordCorrect;
    }

    public void setPasswordCorrect(boolean passwordCorrect) {
        this.passwordCorrect = passwordCorrect;
        setConnected(passwordCorrect);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public boolean isBinaryFlag() {
        return binaryFlag;
    }

    public void setBinaryFlag(boolean binaryFlag) {
        this.binaryFlag = binaryFlag;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }
}
