package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of TYPE command.
 * This command is used to set the binary flag on or off
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class TypeCommand implements Command {
    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        if (commandRequest.hasParam()) {
            String param1 = commandRequest.getParam()[0];


            if (param1.compareToIgnoreCase("A") == 0 || param1.compareToIgnoreCase("N") == 0) {
                //flag on
                clientSession.setBinaryFlag(true);
                clientSession.getCommandHandler().sendMessage(ReturnCode.OK);
            } else if (param1.compareToIgnoreCase("8") == 0 || param1.compareToIgnoreCase("I") == 0 || param1.compareToIgnoreCase("L") == 0) {
                //flag off
                clientSession.setBinaryFlag(false);
                clientSession.getCommandHandler().sendMessage(ReturnCode.OK);
            } else {
                clientSession.getCommandHandler().sendMessage(ReturnCode.TYPEERROR);
            }
        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.TYPEERROR);
        }


    }
}
