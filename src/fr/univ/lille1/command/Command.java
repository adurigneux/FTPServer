package fr.univ.lille1.command;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.request.CommandRequest;

public interface Command {

    public void execute(ClientSession clientSession, CommandRequest commandRequest);

}
