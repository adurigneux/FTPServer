package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

/**
 * Implementation of EPRT command. Equivalent of PORT for Windows.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class EprtCommand implements Command {

    @Override
    public void execute(ClientSession clientSession,
                        CommandRequest commandRequest) {
        String[] param = commandRequest.getParam()[0].replace('|', ' ').split(" "); // bug with split, if we split with "|" it split everything
        if (param.length != 4) {
            clientSession.getCommandHandler().sendMessage(ReturnCode.SYNTAXERROR);
        } else {
            String hostname = param[2];
            int port = Integer.parseInt(param[3]);
            clientSession.getDataConnectionHandler().initializeActive(hostname, port);
            clientSession.getCommandHandler().sendMessage(ReturnCode.PORTSUCCESS);
        }

    }

}
