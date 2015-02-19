package fr.univ.lille1.handler.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.invoker.CommandInvoker;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;


/**
 * This class is owned by every client. It contains the client session and the reference to the
 * commandInvoker. Every command sent to the server by the user is sent to the commandInvoker.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class ClientHandler extends Thread {

    private ClientSession clientSession;
    private CommandInvoker commandInvoker;


    public ClientHandler(ClientSession session) {
        this.clientSession = session;
        this.commandInvoker = CommandInvoker.getInstance();
    }

    /**
     * Wait the command sent by the user to send them to the commandInvoker.
     * This thread run until the client disconnect.
     */
    public void run() {

        clientSession.init();

        try {
            clientSession.getCommandHandler().sendMessage(ReturnCode.WELCOME);

            while (clientSession.isConnected()) {
                CommandRequest commandRequest = clientSession.getCommandHandler().receiveMessage();
                this.commandInvoker.executeCommand(clientSession, commandRequest);
            }

        } catch (RuntimeException e) {
            // e.printStackTrace();
            System.out.println("ClientID : " + clientSession.getClientId() + " || Error : " + e.getMessage());

        } finally {
            try {
                clientSession.close();
            } catch (RuntimeException ignore) {
                //nothing to do, the server is closed, no need to work on this exception
            }
        }
    }


}
