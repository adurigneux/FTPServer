package test.fr.univ.lille1.dal.impl;

import fr.univ.lille1.dal.UserAccess;
import fr.univ.lille1.dal.impl.UserAccessImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * UserAccessImpl Tester.
 *
 * @author Antoine Durigneux & Emmanuel Scouflaire
 * @version 1.0
 * @since <pre>fevr. 16, 2015</pre>
 */
public class UserAccessImplTest {
    private UserAccess userAccess = null;

    @Before
    public void before() throws Exception {
        userAccess = UserAccessImpl.getInstance();
    }

    @After
    public void after() throws Exception {
    }


    /**
     * Method: isUserExist(String user)
     */
    @Test
    public void testIsUserExist() throws Exception {
        Assert.assertEquals(true, userAccess.isUserExist("antoine"));
    }

    /**
     * Method: isUserNOTExist(String user)
     */
    @Test
    public void testIsUserNotExist() throws Exception {
        Assert.assertEquals(false, userAccess.isUserExist("USERTHATDOESNTEXIST"));
    }

    /**
     * Method: isPasswordOK(String user, String pass)
     */
    @Test
    public void testIsPasswordOK() throws Exception {
        Assert.assertEquals(true, userAccess.isPasswordOK("antoine", "1234"));
    }


    /**
     * Method: isPasswordNotOK(String user, String pass)
     */
    @Test
    public void testIsPasswordNotOK() throws Exception {
        Assert.assertEquals(false, userAccess.isPasswordOK("antoine", "emptymessage"));
    }


} 
