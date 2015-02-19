package fr.univ.lille1.main;

import fr.univ.lille1.handler.impl.FtpRequestHandler;

/**
 * Main of the ftp server.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class Main {


    public static void main(String[] args) {
        //usage ftp [folder port]

        String directoryPath = "datafiles";
        int port = 8000;
        if (args.length == 2) {
            //there is a defined path set in args
            directoryPath = args[0];
            port = Integer.parseInt(args[1]);
        }

        try {

            new FtpRequestHandler(port, directoryPath).run();
        } catch (RuntimeException ignore) {
        }
    }
}
