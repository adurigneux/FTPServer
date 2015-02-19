package fr.univ.lille1.exception;

/**
 * This exception is thrown when the required command isn't supported.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class UnsupportedCommandException extends RuntimeException {

    public UnsupportedCommandException() {
    }

    public UnsupportedCommandException(String message) {
        super(message);
    }

}
