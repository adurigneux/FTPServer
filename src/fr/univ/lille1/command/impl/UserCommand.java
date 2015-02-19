package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.dal.UserAccess;
import fr.univ.lille1.dal.impl.UserAccessImpl;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of the USER command.
 * Check the username of the client and ask for password if needed.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class UserCommand implements Command {


    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        UserAccess userAccessHandler = UserAccessImpl.getInstance();
        String username = "";
        if (commandRequest.hasParam()) {
            username = commandRequest.getParam()[0];
        } else {
            username = "anonymous";
        }


        boolean isExist = userAccessHandler.isUserExist(username);
        if (isExist) {
            clientSession.setUsernameCorrect(true);
            clientSession.setUsername(username);
            clientSession.getCommandHandler().sendMessage(ReturnCode.NEEDPSD);
            if (username.compareToIgnoreCase("anonymous") == 0) {
                clientSession.setReadOnly(true);
            }
            return;
        }

        clientSession.getCommandHandler().sendMessage(ReturnCode.USERNAMENOTOK);

    }
}
