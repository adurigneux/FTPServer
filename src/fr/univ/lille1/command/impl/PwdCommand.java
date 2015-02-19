package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of the PWD command.
 * This command is used to print the current location of the client on the ftp server.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class PwdCommand implements Command {

    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        if (clientSession.isConnected()) {
            ReturnCode.PWDSUCCESS.setText("/" + clientSession.getCurrentPath());
            clientSession.getCommandHandler().sendMessage(ReturnCode.PWDSUCCESS);
        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.NOTLOGGED);
        }
    }
}
