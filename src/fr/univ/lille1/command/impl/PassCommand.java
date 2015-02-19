package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.dal.UserAccess;
import fr.univ.lille1.dal.impl.UserAccessImpl;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of the PASS command.
 * This command is used to check the password of the client.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class PassCommand implements Command {

    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {
        UserAccess userAccessHandler = UserAccessImpl.getInstance();

        if (clientSession.isUsernameCorrect()) {
            boolean isCorrect = false;

            //anonymous mode
            if (clientSession.getUsername().compareToIgnoreCase("anonymous") == 0) {
                isCorrect = true;
                //just in case every password is accepted
            } else {
                if (commandRequest.hasParam()) {
                    isCorrect = userAccessHandler.isPasswordOK(clientSession.getUsername(), commandRequest.getParam()[0]);
                } else {
                    clientSession.getCommandHandler().sendMessage(ReturnCode.PASSNOTOK);
                    clientSession.close();
                }
            }


            if (isCorrect) {
                clientSession.setPasswordCorrect(true);
                clientSession.getCommandHandler().sendMessage(ReturnCode.PASSOK);
                return;
            }

        }

        clientSession.getCommandHandler().sendMessage(ReturnCode.PASSNOTOK);
        clientSession.close();

    }

}
