package fr.univ.lille1.invoker;

import fr.univ.lille1.client.ClientSession;
import fr.univ.lille1.command.Command;
import fr.univ.lille1.command.impl.*;
import fr.univ.lille1.exception.UnsupportedCommandException;
import fr.univ.lille1.request.CommandRequest;
import fr.univ.lille1.utils.ReturnCode;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to invoke implemented commands.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class CommandInvoker {

    private final Map<String, Command> factoryMap = new HashMap<String, Command>();

    private CommandInvoker() {
        this.factoryMap.put("USER", new UserCommand());
        this.factoryMap.put("PASS", new PassCommand());
        this.factoryMap.put("STOR", new StorCommand());
        this.factoryMap.put("RETR", new RetrCommand());
        this.factoryMap.put("QUIT", new QuitCommand());
        this.factoryMap.put("PWD", new PwdCommand());
        this.factoryMap.put("XPWD", this.factoryMap.get("PWD"));
        this.factoryMap.put("LS", new LsCommand());
        this.factoryMap.put("NLST", this.factoryMap.get("LS"));
        this.factoryMap.put("CWD", new CwdCommand());
        this.factoryMap.put("XCWD", this.factoryMap.get("CWD"));
        this.factoryMap.put("CDUP", new CdupCommand());
        this.factoryMap.put("XCUP", this.factoryMap.get("CDUP"));
        this.factoryMap.put("PORT", new PortCommand());
        this.factoryMap.put("EPRT", new EprtCommand());
        this.factoryMap.put("PASV", new PasvCommand());
        this.factoryMap.put("MKD", new MkdCommand());
        this.factoryMap.put("RMD", new RmdCommand());
        this.factoryMap.put("XMKD", this.factoryMap.get("MKD"));
        this.factoryMap.put("XRMD", this.factoryMap.get("RMD"));
        this.factoryMap.put("DELE", this.factoryMap.get("RMD"));
        this.factoryMap.put("TYPE", new TypeCommand());


        //list of commands that can only be executed in windows
        if (isWindows()) {
            this.factoryMap.put("LIST", this.factoryMap.get("LS"));
        } else {
            //ls -l
            this.factoryMap.put("LIST", new ListCommand());
        }


    }

    /**
     * get unique singleton of instance invoker
     */
    public static CommandInvoker getInstance() {
        return SingletonHolder.instance;
    }

    public Command getCommand(ClientSession client,
                              CommandRequest commandToExecute) {

        String command = commandToExecute.getCommand();

        Command cmd = this.factoryMap.get(command.toUpperCase());
        if (cmd == null) {
            throw new UnsupportedCommandException(
                    "CommandInvoker - Command unsupported or not implemented.");
        }

        return cmd;
    }

    /**
     * Execute the required command.
     *
     * @param client           The session's client of the client who required the command.
     * @param commandToExecute The command to execute.
     */
    public void executeCommand(ClientSession client,
                               CommandRequest commandToExecute) {

        System.out.println("clientId : " + client.getClientId() + " || " + commandToExecute.toString());
        try {
            Command cmd = getCommand(client, commandToExecute);
            if (commandToExecute.hasParam()) {
                String path = commandToExecute.getParam()[0];
                path = clearPath(path);
                commandToExecute.getParam()[0] = path;
            }

            cmd.execute(client, commandToExecute);
        } catch (UnsupportedCommandException e) {
            client.getCommandHandler().sendMessage(ReturnCode.NOTIMPL);

        }

    }

    private boolean isWindows() {

        return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);

    }

    private String clearPath(String path) {
        if (path.charAt(0) == '/' || path.charAt(0) == '\\') {
            path = path.substring(1);
            clearPath(path);
        }
        return path;
    }

    /**
     * Holder method for singleton Used only for multitrheaded use of singleton
     */
    private static class SingletonHolder {
        /**
         * unique instance non preinit
         */
        private final static CommandInvoker instance = new CommandInvoker();
    }

}
