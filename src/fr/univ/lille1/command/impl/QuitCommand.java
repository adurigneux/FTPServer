package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of QUIT command.
 * This command is used when the clients disconnect.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class QuitCommand implements Command {

    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        clientSession.getCommandHandler().sendMessage(ReturnCode.GOODBYE);
        clientSession.close();
    }

}
