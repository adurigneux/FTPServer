package fr.univ.lille1.client;

import fr.univ.lille1.handler.CommandHandler;
import fr.univ.lille1.handler.impl.DataConnectionHandler;

import java.net.Socket;

/**
 * ClientSession interface
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public interface ClientSession {


    public void init();

    public void close();

    public int getClientId();

    public String getCurrentPath();

    public void setCurrentPath(String currentPath);

    public boolean isConnected();

    public void quitSession();

    public Socket getConnectionSocket();

    public CommandHandler getCommandHandler();

    public DataConnectionHandler getDataConnectionHandler();

    public boolean isUsernameCorrect();

    public void setUsernameCorrect(boolean usernameSet);

    public boolean isPasswordCorrect();

    public void setPasswordCorrect(boolean passwordSet);

    public String getUsername();

    public void setUsername(String username);

    public boolean isBinaryFlag();

    public void setBinaryFlag(boolean binaryFlag);


    public boolean isReadOnly();

    public void setReadOnly(boolean isReadOnly);
}
