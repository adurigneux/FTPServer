package test.fr.univ.lille1.client.impl;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.client.impl.ClientSessionImpl;
import fr.univ.lille1.handler.impl.FtpRequestHandler;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.*;

import java.net.Socket;

/**
 * ClientSessionImpl Tester.
 *
 * @author Antoine Durigneux & Emmanuel Scouflaire
 * @version 1.0
 * @since <pre>fevr. 16, 2015</pre>
 */
public class ClientSessionImplTest {
    private static ClientSession clientSession;

    @BeforeClass
    public static void beforeClass() throws Exception {
        FtpRequestHandler ftpRequestHandler = new FtpRequestHandler(12345,
                "datafiles");
        new Thread(ftpRequestHandler).start();
    }


    @Before
    public void before() throws Exception {

        FTPClient ftpClient = new FTPClient();

        ftpClient.connect("localhost", 12345);
        clientSession = new ClientSessionImpl(new Socket(
                ftpClient.getLocalAddress(), ftpClient.getRemotePort()),
                "datafiles", 1);


        clientSession.init();
    }

    @After
    public void after() throws Exception {
        clientSession.close();
    }


    /**
     * Method: getClientId()
     */
    @Test
    public void testGetClientId() throws Exception {
        Assert.assertEquals(1, clientSession.getClientId());
    }

    /**
     * Method: getCurrentPath()
     */
    @Test
    public void testGetCurrentPath() throws Exception {
        Assert.assertEquals("datafiles", clientSession.getCurrentPath());
    }

    /**
     * Method: setCurrentPath(String currentPath)
     */
    @Test
    public void testSetCurrentPath() throws Exception {
        String text = "newpath";
        clientSession.setCurrentPath(text);
        Assert.assertEquals(text, clientSession.getCurrentPath());
    }

    /**
     * Method: isConnected()
     */
    @Test
    public void testIsConnected() throws Exception {
        Assert.assertEquals(true, clientSession.isConnected());
    }

    /**
     * Method: isNotConnected()
     */
    @Test
    public void testIsNotConnected() throws Exception {
        clientSession.close();
        Assert.assertEquals(false, clientSession.isConnected());
    }


    /**
     * Method: quitSession()
     */
    @Test
    public void testQuitSession() throws Exception {
        clientSession.quitSession();
        Assert.assertEquals(false, clientSession.isConnected());

    }

    /**
     * Method: getConnectionSocket()
     */
    @Test
    public void testGetConnectionSocket() throws Exception {
        Assert.assertNotNull(clientSession.getConnectionSocket());
    }

    /**
     * Method: getCommandHandler()
     */
    @Test
    public void testGetCommandHandler() throws Exception {
        Assert.assertNotNull(clientSession.getCommandHandler());
    }

    /**
     * Method: getDataConnectionHandler()
     */
    @Test
    public void testGetDataConnectionHandler() throws Exception {
        Assert.assertNotNull(clientSession.getCommandHandler());
    }

    /**
     * Method: isUsernameCorrect()
     */
    @Test
    public void testIsUsernameCorrect() throws Exception {
        Assert.assertEquals(false, clientSession.isUsernameCorrect());
    }

    /**
     * Method: setUsernameCorrect(boolean usernameCorrect)
     */
    @Test
    public void testSetUsernameCorrect() throws Exception {
        clientSession.setUsernameCorrect(true);
        Assert.assertEquals(true, clientSession.isUsernameCorrect());
    }

    /**
     * Method: isPasswordCorrect()
     */
    @Test
    public void testIsPasswordCorrect() throws Exception {
        Assert.assertEquals(false, clientSession.isPasswordCorrect());
    }

    /**
     * Method: setPasswordCorrect(boolean passwordCorrect)
     */
    @Test
    public void testSetPasswordCorrect() throws Exception {
        clientSession.setPasswordCorrect(true);
        Assert.assertEquals(true, clientSession.isPasswordCorrect());
    }

    /**
     * Method: getUsername()
     */
    @Test
    public void testGetUsername() throws Exception {
        Assert.assertNull(clientSession.getUsername());
    }

    /**
     * Method: setUsername(String username)
     */
    @Test
    public void testSetUsername() throws Exception {
        String username = "antoine";
        clientSession.setUsername(username);
        Assert.assertEquals(username, clientSession.getUsername());
    }

    /**
     * Method: isBinaryFlag()
     */
    @Test
    public void testIsBinaryFlag() throws Exception {
        Assert.assertEquals(false, clientSession.isBinaryFlag());
    }

    /**
     * Method: setBinaryFlag(boolean binaryFlag)
     */
    @Test
    public void testSetBinaryFlag() throws Exception {
        clientSession.setBinaryFlag(true);
        Assert.assertEquals(true, clientSession.isBinaryFlag());
    }

    /**
     * Method: isReadOnly()
     */
    @Test
    public void testIsReadOnly() throws Exception {
        Assert.assertEquals(false, clientSession.isReadOnly());
    }

    /**
     * Method: setReadOnly(boolean isReadOnly)
     */
    @Test
    public void testSetReadOnly() throws Exception {
        clientSession.setReadOnly(true);
        Assert.assertEquals(true, clientSession.isReadOnly());
    }


} 
