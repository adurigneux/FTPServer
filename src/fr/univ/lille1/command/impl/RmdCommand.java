package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.File;

/**
 * Implementation of the RMD command.
 * This command is used to delete a directory in the current directory of the user
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class RmdCommand implements Command {
    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

        if (!clientSession.isReadOnly()) {

            File result = new File(clientSession.getCurrentPath() + "/" + commandRequest.getParam()[0]);

            if (result.exists()) {


                //call delete to delete files and empty directory
                recursiveDelete(result);
                clientSession.getCommandHandler().sendMessage(ReturnCode.DIRRMSUCCESS);
            } else {
                clientSession.getCommandHandler().sendMessage(ReturnCode.DIRRMERROR);
            }
        } else {
            clientSession.getCommandHandler().sendMessage(ReturnCode.READONLY);
        }
    }


    private void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return;

        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        file.delete();
    }
}
