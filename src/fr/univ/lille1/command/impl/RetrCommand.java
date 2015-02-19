package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Implementation of RETR command.
 * This command is used to send required files to the client.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class RetrCommand implements Command {

    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {
        FileInputStream fileStream = null;
        OutputStream out = null;
        try {
            // Opens file
            String path = commandRequest.getParam()[0];
            if (!path.contains(clientSession.getCurrentPath())) {
                path = clientSession.getCurrentPath() + "/" + path;
            }


            fileStream = new FileInputStream(path);
            // Opens connection
            clientSession.getCommandHandler().sendMessage(ReturnCode.FILESTATUSOK);
            out = clientSession.getDataConnectionHandler().open().getOutputStream();

            // Sends through data socket
            byte[] buffer = new byte[1024];
            int nbBytes = 0;
            while ((nbBytes = fileStream.read(buffer)) != -1) {
                out.write(buffer, 0, nbBytes);
            }
            // Closes connection
            clientSession.getCommandHandler().sendMessage(ReturnCode.CLOSEDATACONNECTION);
            clientSession.getDataConnectionHandler().close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
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
                out.close();
            } catch (Exception ignore) {
            }
        }
    }


}
