package fr.univ.lille1.request.impl;

import fr.univ.lille1.request.CommandRequest;

/**
 * Implementation of CommandRequest.
 * Contains parameters sent with the required command.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public class CommandRequestImpl implements CommandRequest {
    private String command;
    private String[] param;
    private boolean hasParam = false;


    public CommandRequestImpl(String commandLine) {

        if (commandLine == null) {
            this.command = "";
            this.hasParam = false;
            return;
        }
        String[] split = commandLine.split("\\s+");
        this.command = split[0];
        if (split.length > 1) {
            this.hasParam = true;
            this.param = new String[split.length - 1];
            for (int i = 1; i < split.length; i++) {
                this.param[i - 1] = split[i];
            }

        }
    }


    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public boolean hasParam() {
        return this.hasParam;
    }

    @Override
    public String[] getParam() {
        return this.param;
    }

    private String toStringParam() {
        String result = "[";
        if (hasParam) {
            for (String value : this.param) {
                result += " " + value;
            }
        }
        result += " ]";
        return result;
    }


    public String toString() {
        return "cmd : " + this.command + " || param : " + toStringParam();
    }

}
