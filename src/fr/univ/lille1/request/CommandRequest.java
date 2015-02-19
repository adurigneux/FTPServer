package fr.univ.lille1.request;

/**
 * Interface of the CommandRequest
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public interface CommandRequest {


    String getCommand();


    boolean hasParam();


    String[] getParam();

}
