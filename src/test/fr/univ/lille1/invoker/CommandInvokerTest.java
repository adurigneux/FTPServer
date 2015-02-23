package test.fr.univ.lille1.invoker;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.client.impl.ClientSessionImpl;
import fr.univ.lille1.command.impl.UserCommand;
import fr.univ.lille1.exception.UnsupportedCommandException;
import fr.univ.lille1.handler.impl.FtpRequestHandler;
import fr.univ.lille1.invoker.CommandInvoker;
import fr.univ.lille1.request.impl.CommandRequestImpl;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * CommandInvoker Tester.
 *
 * @author Antoine Durigneux & Emmanuel Scouflaire
 * @version 1.0
 * @since <pre>
 * fevr. 16, 2015
 * </pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandInvokerTest {

    private static CommandInvoker commandInvoker = CommandInvoker.getInstance();
    private static ClientSession client;
    private static FTPClient ftpClient;

    @BeforeClass
    public static void beforeClass() throws Exception {
        FtpRequestHandler ftpRequestHandler = new FtpRequestHandler(12345,
                "datafiles");
        new Thread(ftpRequestHandler).start();
        init();
    }

    private static void init() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect("localhost", 12345);
            client = new ClientSessionImpl(new Socket(
                    ftpClient.getLocalAddress(), ftpClient.getRemotePort()),
                    "datafiles", 1);
        } catch (IOException e) {
        }
        client.init();
    }


    /**
     * Method: getCommand()
     */
    @Test
    public void test01GetCommand() {
        Assert.assertEquals(
                UserCommand.class,
                commandInvoker.getCommand(client,
                        new CommandRequestImpl("USER")).getClass());
    }

    /**
     * Method: getCommand()
     */
    @Test(expected = UnsupportedCommandException.class)
    public void test02GetUnknownCommand() {
        commandInvoker.getCommand(client, new CommandRequestImpl("TEST bug"));
    }

    /**
     * Method: executeCommand("User unknown")
     * The user doesn't exist
     */
    @Test
    public void test03ExecuteCommandUserUnknownUser() {
        try {
            ftpClient.sendCommand("USER unknown");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(430, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(User manu)
     */
    @Test
    public void test04ExecuteCommandUser() {
        try {
            ftpClient.sendCommand("USER manu");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(331, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(Pass 12345)
     * The password is incorrect
     */
    @Test
    public void test05ExecuteCommandPassNotOK() {
        try {
            ftpClient.sendCommand("PASS 12345");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(530, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(Pass 1234)
     */
    @Test
    public void test06ExecuteCommandPassOK() {
        init();
        try {
            ftpClient.sendCommand("USER manu");
            ftpClient.sendCommand("PASS 1234");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(230, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(PWD)
     */
    @Test
    public void test07ExecuteCommandPwd() {
        try {
            System.out.println(ftpClient.printWorkingDirectory());
        } catch (IOException ignore) {
        }
        Assert.assertEquals(257, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(MKDIR test)
     * The directory doesn't exist => test ok
     */
    @Test
    public void test08ExecuteCommandMkdirOk() {
        File result = null;
        try {
            result = new File(client.getCurrentPath() + "/test");
            ftpClient.mkd("test");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(257, ftpClient.getReplyCode());
        Assert.assertTrue(result.exists());
    }

    /**
     * Method: executeCommand(CWD test)
     */
    @Test
    public void test09ExecuteCommandCwd() {
        try {
            ftpClient.sendCommand("CWD test");
            Assert.assertEquals(200, ftpClient.getReplyCode());
            System.out.println(ftpClient.printWorkingDirectory());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(CWD unknown)
     * The directory unknown doesn't exist
     */
    @Test
    public void test10ExecuteCommandCwdNotOK() {
        try {
            ftpClient.cwd("unknown");
            Assert.assertEquals(550, ftpClient.getReplyCode());
            System.out.println(ftpClient.printWorkingDirectory());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(CDUP)
     */
    @Test
    public void test11ExecuteCommandCdup() {
        try {
            ftpClient.cdup();
            Assert.assertEquals(200, ftpClient.getReplyCode());
            System.out.println(ftpClient.printWorkingDirectory());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(MKDIR test)
     * The directory test already exists => creation fail
     */
    @Test
    public void test12ExecuteCommandMkdirNotOk() {
        try {
            ftpClient.mkd("test");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(550, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(RMDIR test)
     */
    @Test
    public void test13ExecuteCommandRmdirOk() {
        File result = null;
        try {
            result = new File(client.getCurrentPath() + "/test");
            ftpClient.rmd("test");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(250, ftpClient.getReplyCode());
        Assert.assertTrue(!result.exists());
    }

    /**
     * Method: executeCommand(RMDIR test)
     * The directory test is already deleted
     */
    @Test
    public void test14ExecuteCommandRmdirNotOk() {
        try {
            ftpClient.rmd("test");
        } catch (IOException ignore) {
        }
        Assert.assertEquals(550, ftpClient.getReplyCode());
    }

    /**
     * Method: executeCommand(PORT 127,0,0,1,X,X)
     */
    @Test
    public void test15ExecuteCommandPortOk() {
        try {
            ftpClient.sendCommand("PORT 127,0,0,1,"
                    + (ftpClient.getLocalPort()) / 256 + ","
                    + (ftpClient.getLocalPort()) % 256);
            Assert.assertEquals(200, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(LS)
     */
    @Test
    public void test16ExecuteCommandList() {
        try {
            String[] filesName = ftpClient.listNames();
            for (String name : filesName) {
                System.out.println(name);
            }
            Assert.assertEquals(226, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(STOR lena_original.jpg)
     */
    @Test
    public void test17ExecuteCommandStor() throws Exception {

        ftpClient.mkd("testSend");
        ftpClient.cwd("testSend");

        Assert.assertTrue(!(new File("datafiles/testSend/lena_original.jpg"))
                .exists());

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        File firstLocalFile = new File("datafiles/lena_original.jpg");

        String firstRemoteFile = "lena_original.jpg";
        InputStream inputStream = new FileInputStream(firstLocalFile);

        ftpClient.storeFile(firstRemoteFile, inputStream);
        inputStream.close();

        Assert.assertEquals(226, ftpClient.getReplyCode());

        Assert.assertTrue((new File("datafiles/testSend/lena_original.jpg"))
                .exists());

        ftpClient.cdup();
        ftpClient.rmd("testSend");

    }

    /**
     * Method: executeCommand(RECV lena_original.jpg)
     */
    @Test
    public void test18ExecuteCommandRecv() {
        try {
            BufferedInputStream in = new BufferedInputStream(
                    ftpClient.retrieveFileStream("lena_original.jpg"));
            File test = new File("datafiles/testSend/lena_original.jpg");

            Assert.assertTrue(!test.exists());

            FileOutputStream fos = new FileOutputStream(test);
            byte[] buffer = new byte[1024];
            int nbBytes = 0;
            while ((nbBytes = in.read(buffer)) != -1) {
                fos.write(buffer, 0, nbBytes);
            }
            fos.close();
            in.close();

            Assert.assertTrue(test.exists());
            test.delete();

        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(EPRT ::1,X)
     */
    @Test
    public void test19ExecuteCommandEprt() {
        try {
            init();
            ftpClient.eprt(InetAddress.getByName("::1"),
                    ftpClient.getLocalPort() + 2);

            Assert.assertEquals(200, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(QUIT)
     */
    @Test
    public void test20ExecuteCommandQuit() {
        try {
            init();
            ftpClient.quit();

            Assert.assertEquals(221, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(USER)
     * Test anonymous mode.
     */
    @Test
    public void test21ExecuteCommandUserAndPassAnonymous() {
        try {
            init();
            ftpClient.sendCommand("USER"); // anonyme
            Assert.assertEquals(331, ftpClient.getReplyCode());
            ftpClient.sendCommand("PASS "); // anonyme
            Assert.assertEquals(230, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(MKDIR testAnonymous)
     * Test MKDIR in anonymous mode (read-only)
     */
    @Test
    public void test22ExecuteCommandMkdirAnonymous() {
        try {
            ftpClient.mkd("testAnonymous");
            Assert.assertEquals(550, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(RMDIR testAnonymous)
     * Test RMDIR in anonymous mode (read-only)
     */
    @Test
    public void test23ExecuteCommandRmdirAnonymous() {
        try {
            ftpClient.rmd("testAnonymous");
            Assert.assertEquals(550, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(STOR lena.jpg)
     */
    @Test
    public void test24ExecuteCommandStorAnonymous() {
        try {
            ftpClient.stor("lena.jpg");
            Assert.assertEquals(550, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(TYPE ...)
     * With all valid types
     */
    @Test
    public void test25ExecuteCommandType() {
        try {
            ftpClient.sendCommand("TYPE Z");
            Assert.assertEquals(550, ftpClient.getReplyCode());
            ftpClient.sendCommand("TYPE A");
            Assert.assertEquals(200, ftpClient.getReplyCode());
            ftpClient.sendCommand("TYPE N");
            Assert.assertEquals(200, ftpClient.getReplyCode());
            ftpClient.sendCommand("TYPE 8");
            Assert.assertEquals(200, ftpClient.getReplyCode());
            ftpClient.sendCommand("TYPE I");
            Assert.assertEquals(200, ftpClient.getReplyCode());
            ftpClient.sendCommand("TYPE L");
            Assert.assertEquals(200, ftpClient.getReplyCode());
            ftpClient.sendCommand("TYPE");
            Assert.assertEquals(550, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }

    /**
     * Method: executeCommand(PASV)
     */
    @Test
    public void test26ExecuteCommandPasv() {
        try {
            ftpClient.pasv();
            Assert.assertEquals(227, ftpClient.getReplyCode());
        } catch (IOException ignore) {
        }
    }
}
