package fr.univ.lille1.exception;


/**
 * A ConnectionException is thrown when there is a problem while using a socket.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class ConnectionException extends RuntimeException {


    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }


}
