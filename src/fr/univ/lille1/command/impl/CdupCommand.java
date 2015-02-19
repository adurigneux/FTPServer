package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.File;

/**
 * Implementation of the CDUP command.
 * This command is used to get acces to the parent of the current directory
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class CdupCommand implements Command {
    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        File r = new File(clientSession.getCurrentPath());

        if (r.exists()) {
            String parent = r.getParent();
            if (parent != null && !parent.isEmpty()) {
                //get the parent of the file
                clientSession.setCurrentPath(r.getParent());
                clientSession.getCommandHandler().sendMessage(ReturnCode.OK);
            } else {
                clientSession.getCommandHandler().sendMessage(ReturnCode.CDUPERROR);
            }

        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.CDUPERROR);

        }
    }
}
