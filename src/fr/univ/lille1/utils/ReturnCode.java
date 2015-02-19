package fr.univ.lille1.utils;

/**
 * Enum of each return code used in the application. Each ReturnCode is composed of the return code
 * and the corresponding message.
 *
 * @author Durigneux Antoine
 * @author Scouflaire Emmanuel
 */
public enum ReturnCode {


    OK(200, "OK."),
    WELCOME(220, "Welcome to your favorite ftp server."),
    GOODBYE(221, "Goodbye!"),
    READONLY(550, "Anonymous user : READ ONLY"),

    //LIST,RETR,STOR return code
    FILESTATUSOK(150, "File status okay; about to open data connection."),
    CLOSEDATACONNECTION(226, "Closing data connection. Requested file action successful."),
    DATATRANSFERABORTED(426, "Connection closed; transfer aborted."),
    FILEUNAVAILABLE(550, "Requested action not taken. File unavailable"),

    //USER return code
    NEEDPSD(331, "User name okay, need password."),
    USERNAMENOTOK(430, "Invalid username or password"),

    //PASS return code
    PASSOK(230, "User logged in, proceed."),
    PASSNOTOK(530, "Not logged in."),

    //PORT return code
    PORTSUCCESS(200, "PORT command successful."),
    SYNTAXERROR(501, "Syntax error in parameters or arguments."),

    //Type
    TYPEERROR(550, "Syntax error in parameters."),
    //PWD
    PWDSUCCESS(257, ""),

    //MKD
    DIRSUCCESS(257, ""),
    DIRERROR(550, "Problem in the creation of the directory"),

    //RMD
    DIRRMSUCCESS(250, "The dir is succefully deleted"),
    DIRRMERROR(550, "Problem in the delete of the directory"),

    //CWD
    CWDNOFILE(550, "There is no such file or directory"),

    //CDUP
    CDUPERROR(550, "Impossible to execute CDUP"),

    NOTLOGGED(530, "Not logged in."),
    NOTIMPL(502, "Command not implemented.");


    private int returnCode;
    private String textToReturn = "";

    ReturnCode(int returnCode, String textToReturn) {
        this.returnCode = returnCode;
        this.textToReturn = textToReturn;
    }

    public void setText(String text) {
        this.textToReturn = text;
    }

    @Override
    public String toString() {
        return this.returnCode + " " + this.textToReturn + "\r\n";
    }


}