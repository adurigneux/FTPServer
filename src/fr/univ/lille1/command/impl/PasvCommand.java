package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.handler.impl.FtpRequestHandler;
import fr.univ.lille1.request.CommandRequest;

/**
 * Implementation of the PASV command.
 * This command is used to start the passive mode.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class PasvCommand implements Command {

    @Override
    public void execute(ClientSession clientSession,
                        CommandRequest commandRequest) {
        int port = FtpRequestHandler.port + clientSession.getClientId();
        String host = clientSession.getConnectionSocket().getLocalAddress().toString();
        System.out.println(host + ":" + port);
        clientSession.getDataConnectionHandler().initializePassive(port);
        clientSession.getCommandHandler().sendMessage("227 Entering Passive Mode (" + host.replace('.', ',') + "," + port / 256 + "," + port % 256 + ")\r\n");
    }

}
