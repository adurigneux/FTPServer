package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Implementation of the LS command. This command is used to list all the files
 * of the current client's directory.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class LsCommand implements Command {

    @Override
    public void execute(ClientSession clientSession,
                        CommandRequest commandRequest) {

        if (clientSession.isConnected()) {

            File res = new File(clientSession.getCurrentPath());
            OutputStream out = null;

            try {
                clientSession.getCommandHandler().sendMessage(
                        ReturnCode.FILESTATUSOK);

                out = clientSession.getDataConnectionHandler().open()
                        .getOutputStream();

                for (File f : res.listFiles()) {

                    String val = f.getName() + "\r\n";
                    final byte[] buffer = val.getBytes();
                    out.write(buffer, 0, buffer.length);
                }

                clientSession.getCommandHandler().sendMessage(
                        ReturnCode.CLOSEDATACONNECTION);
                out.close();
                clientSession.getDataConnectionHandler().close();
            } catch (IOException e) {

            }

        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.NOTLOGGED);
        }

    }

}
