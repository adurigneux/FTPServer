package fr.univ.lille1.exception;

/**
 * This exception is thrown when there is a problem while initializing the server.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class ServerException extends RuntimeException {


    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }


}
