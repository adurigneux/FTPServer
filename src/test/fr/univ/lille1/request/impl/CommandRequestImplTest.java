package test.fr.univ.lille1.request.impl;

import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.request.impl.CommandRequestImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * CommandRequestImpl Tester.
 *
 * @author Antoine Durigneux & Emmanuel Scouflaire
 * @version 1.0
 * @since <pre>fevr. 16, 2015</pre>
 */
public class CommandRequestImplTest {
    private CommandRequest commandRequest;

    @Before
    public void before() throws Exception {
        commandRequest = new CommandRequestImpl("USER antoine");
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCommand()
     */
    @Test
    public void testGetCommand() throws Exception {

        Assert.assertEquals("USER", commandRequest.getCommand());
    }

    /**
     * Method: hasParam()
     */
    @Test
    public void testHasParam() throws Exception {
        Assert.assertEquals(true, commandRequest.hasParam());
    }

    /**
     * Method: getParam()
     */
    @Test
    public void testGetParam() throws Exception {
        Assert.assertEquals("antoine", commandRequest.getParam()[0]);
    }


}
