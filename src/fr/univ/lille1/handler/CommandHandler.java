package fr.univ.lille1.handler;

import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Interface which is used to send messages to the client
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public interface CommandHandler {


    public void init();

    public void close();


    public void sendMessage(String message);

    public CommandRequest receiveMessage();

    public void sendMessage(ReturnCode message);


}
