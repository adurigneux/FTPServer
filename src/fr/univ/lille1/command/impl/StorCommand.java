package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of STOR command.
 * This command is used to store files on the server.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class StorCommand implements Command {

    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {
        if (!clientSession.isReadOnly()) {
            FileOutputStream fileStream = null;
            InputStream in = null;
            try {
                String path = commandRequest.getParam()[0];
                if (!path.contains(clientSession.getCurrentPath())) {
                    path = clientSession.getCurrentPath() + "/" + path;
                }

                // Opens file
                fileStream = new FileOutputStream(path);
                // Opens connection
                clientSession.getCommandHandler().sendMessage(ReturnCode.FILESTATUSOK);
                in = clientSession.getDataConnectionHandler().open().getInputStream();

                // Sends through data socket
                byte[] buffer = new byte[1024];
                int nbBytes = 0;
                while ((nbBytes = in.read(buffer)) != -1) {
                    fileStream.write(buffer, 0, nbBytes);
                }
                // Closes connection
                clientSession.getCommandHandler().sendMessage(ReturnCode.CLOSEDATACONNECTION);
                clientSession.getDataConnectionHandler().close();
            } catch (FileNotFoundException e) {
                clientSession.getCommandHandler().sendMessage(ReturnCode.FILEUNAVAILABLE);
            } catch (IOException ignore) {
                // Closes connection if error during write
                clientSession.getCommandHandler().sendMessage(ReturnCode.DATATRANSFERABORTED);
                clientSession.getDataConnectionHandler().close();
            } finally {
                try {
                    fileStream.close();
                } catch (Exception ignore) {
                }
                try {
                    in.close();
                } catch (Exception ignore) {
                }
            }
        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.READONLY);
        }
    }


}
