package fr.univ.lille1.command.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the LITS command. This command is used to list all the files
 * of the current client's directory.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class ListCommand implements Command {
    @Override
    public void execute(ClientSession clientSession, CommandRequest commandRequest) {

               /*
                   -rw-r--r-- 1 owner group           213 Aug 26 16:31 README

The line contains

- for a regular file or d for a directory;
the literal string rw-r--r-- 1 owner group for a regular file, or rwxr-xr-x 1 owner group for a directory;
the file size in decimal right-justified in a 13-byte field;
a three-letter month name, first letter capitalized;
a day number right-justified in a 3-byte field;
a space and a 2-digit hour number;
a colon and a 2-digit minute number;
a space and the abbreviated pathname of the file.
                */


        File res = new File(clientSession.getCurrentPath());
        OutputStream out = null;
        final Runtime runtime = Runtime.getRuntime();
        String path = clientSession.getCurrentPath();
        if (commandRequest.hasParam()) {
            path += "/" + commandRequest.getParam()[0];
        }

        Map<String, String> environment = new HashMap<String, String>(System.getenv());
        environment.put("LC_ALL", "en_EN");
        String[] envp = new String[environment.size()];
        int count = 0;
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            envp[count++] = entry.getKey() + "=" + entry.getValue();
        }

        final String cmd = "ls -l " + path;
        InputStream is = null;

        try {
            clientSession.getCommandHandler().sendMessage(ReturnCode.FILESTATUSOK);

            // Execute ls -l command.
            is = runtime.exec(cmd, envp).getInputStream();
            out = clientSession.getDataConnectionHandler().open().getOutputStream();

            // Send through data socket.

            final byte[] buffer = new byte[1024];
            int nbBytes = 0;
            while ((nbBytes = is.read(buffer)) != -1) {
                out.write(buffer, 0, nbBytes);
            }

            clientSession.getCommandHandler().sendMessage(ReturnCode.CLOSEDATACONNECTION);
            clientSession.getDataConnectionHandler().close();
        } catch (IOException e) {
        } finally {

            try {
                out.close();
                is.close();
            } catch (Exception ignore) {
            }
        }


    }


}
