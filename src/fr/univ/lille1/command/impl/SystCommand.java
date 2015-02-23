package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;

public class SystCommand implements Command {

    @Override
    public void execute(ClientSession clientSession,
                        CommandRequest commandRequest) {
        clientSession.getCommandHandler().sendMessage("215 UNIX Type: L8\r\n");

    }

}
