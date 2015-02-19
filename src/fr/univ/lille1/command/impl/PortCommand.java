package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of the PORT command.
 * This command is sent when the user use a command in active mode.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class PortCommand implements Command {

    @Override
    public void execute(ClientSession clientSession,
                        CommandRequest commandRequest) {
        String[] param = commandRequest.getParam()[0].split(",");
        if (param.length != 6) {
            clientSession.getCommandHandler().sendMessage(ReturnCode.SYNTAXERROR);
        } else {
            //hostame + port
            String hostname = param[0] + "." + param[1] + "." + param[2] + "." + param[3];
            int port = Integer.parseInt(param[4]) * 256 + Integer.parseInt(param[5]);
            clientSession.getDataConnectionHandler().initializeActive(hostname, port);
            clientSession.getCommandHandler().sendMessage(ReturnCode.PORTSUCCESS);
        }
    }

}