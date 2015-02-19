package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.File;

/**
 * Implementation of the MKD command.
 * This command is used to create a directory in the current directory of the client
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class MkdCommand implements Command {
    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        //client in read only can not execute this function
        if (!clientSession.isReadOnly()) {
            File result = new File(clientSession.getCurrentPath() + "/" + commandRequest.getParam()[0]);

            //make dirs /a/b/c ..
            if (result.mkdirs()) {
                clientSession.getCommandHandler().sendMessage(ReturnCode.DIRSUCCESS);
            } else {
                clientSession.getCommandHandler().sendMessage(ReturnCode.DIRERROR);
            }
        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.READONLY);
        }

    }
}
