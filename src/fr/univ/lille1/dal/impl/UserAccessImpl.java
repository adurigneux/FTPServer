package fr.univ.lille1.dal.impl;

import fr.univ.lille1.dal.UserAccess;
import fr.univ.lille1.exception.ServerException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class load the database file to save every username and password in a map. Then it
 * is used to check the username and password of each client.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class UserAccessImpl implements UserAccess {

    private final Map<String, String> userDatabase = new HashMap<String, String>();

    private UserAccessImpl() {
        //only for a little database, see file
        userDatabase.put("antoine", "1234");
        userDatabase.put("manu", "1234");
        userDatabase.put("anonymous", "");


        String pathDatabase = "database/database.txt";
        BufferedReader reader = null;
        try {

            File res = new File(pathDatabase);


            reader = new BufferedReader(new FileReader(res));
            String line = reader.readLine();

            while (line != null) {


                String[] user = line.split(":");
                //username : password
                if (user[0].compareToIgnoreCase("anonymous") == 0) {
                    this.userDatabase.put(user[0], "");
                } else {
                    this.userDatabase.put(user[0], user[1]);
                }
                line = reader.readLine();

            }


        } catch (FileNotFoundException ignore) {
            // throw new ServerException("UserAccessHandlerImpl - impossible to find user database.");
            //ignore this exception because the database is not found, need to be standalone
        } catch (IOException e) {
            throw new ServerException("UserAccessHandlerImpl - impossible to find user database.");
        }

    }

    /**
     * get unique singleton of instance invoker
     */
    public static UserAccessImpl getInstance() {
        return SingletonHolder.instance;
    }

    public boolean isUserExist(String user) {
        return this.userDatabase.get(user) != null;
    }

    public boolean isPasswordOK(String user, String pass) {
        String userPass = this.userDatabase.get(user);
        if (userPass != null) {
            return userPass.compareTo(pass) == 0;
        }
        return false;

    }

    /**
     * Holder method for singleton Used only for multitrheaded use of singleton
     */
    private static class SingletonHolder {
        /**
         * unique instance non preinit
         */
        private final static UserAccessImpl instance = new UserAccessImpl();
    }

}
