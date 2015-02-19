package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.File;

/**
 * Implementation of the CWD command.
 * This command is used to Change the current directory of the client
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class CwdCommand implements Command {

    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        String path = commandRequest.getParam()[0];
        if (!path.contains(clientSession.getCurrentPath())) {
            path = clientSession.getCurrentPath() + "/" + path;
        }

        File result = new File(path);
        if (result.exists()) {

            clientSession.setCurrentPath(path);
            clientSession.getCommandHandler().sendMessage(ReturnCode.OK);
        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.CWDNOFILE);
        }

    }

}
