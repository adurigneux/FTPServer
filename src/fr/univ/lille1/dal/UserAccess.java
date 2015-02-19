package fr.univ.lille1.dal;

/**
 * This interface contains the methods that are used in order to access user informations
 * every DAL implementation should have those.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public interface UserAccess {

    public boolean isUserExist(String user);

    public boolean isPasswordOK(String user, String pass);

}
