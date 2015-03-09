Serveur FTP en java
===================
Auteurs
: Antoine Durigneux
: Emmanuel Scouflaire

09/03/2015

Dépendances
----------
Commons-net-3.3.jar
hamcrest-core-1.3.jar
junit-4.12.jar

Introduction
-------------
Ce logiciel permet de créer un serveur FTP simple en Java. Les commandes de bases permettent une utilisation normale d'un serveur FTP (Voir liste de toutes les commandes implémentées ci-dessous).

Les RFC 959 ont été utilisées.

Utilisation
----------

"java -jar tp1-durigneux-scouflaire.jar [datafilefolder [port]]"


Liste des commandes
-------------

CMD     | Description
-------- | ---
CDUP | Acceder au dossier parent
CWD | Changer de repertoire courant
EPRT | Equivalent de la commande PORT (pour windows)
LIST | Affiche toutes les informations des fichiers du repertoire courant
LS | Affiche les noms des fichiers et repertoire du dossier courant
MKDIR | Creer un dossier
PASS | Envoyer son mot de passe
PASV | Entrer en mode Passif (voir rfc FTP)
PORT | Port de connection pour le mode Actif (voir rfc FTP)
PWD | Affiche le chemin du repertoire courant
QUIT | Cloturer une connexion avec un client
RETR | Recuperer un fichier
RMDIR | Supprimer un dossier (et sous repertoire)
STOR | Envoyer un fichier
TYPE | Accepter certains mode d'envoie (voir RFC FTP)
USER | Envoyer son nom d'utilisateur


Architecture
-------------

###Interfaces
####ClientSession 
Interface qui permet de définir les fonctions utilisées dans le code. 
Cette interface permet d'abstraire l'implémentation d'un client. En effet, il est possible de décider d'utiliser differentes
manière de gérer les clients.

####Command
Interface utilisée pour le pattern Command Java, en effet, cette classe ne contient qu'une seule méthode, execute().
Cette même méthode est redéfinie dans chaque sous commande, et chaque commande est appellée par cette même methode.

####UserAccess
Interface simple qui définit les appels possibles sur la base de données. Actuellement, il s'agit d'un simple fichier texte,
mais il pourrait être très simple d'implémenter une solution différente heritant de cette interface.

####CommandHandler
Interface de communication entre le client et le serveur. Définit la manière dont les messages sont transmis

####CommandRequest
Interface qui définit le comportement d'une commande. Il est possible de gérer et recevoir des commandes de différentes façons.
Cet interface contient un constructeur et certains getter d'attributs simples de manière à garder une structure simple


###Polymorphisme
Le pattern command utilise le polymorphisme java. En effet chaque instance de Command execute la même méthode
avec un comportement différent pour chaque commande.

De plus le code contient des interfaces rendant le polymorphisme encore plus simple, par exemple dans le cas de plusieurs bases de données.


###Erreurs
Le serveur contient 3 types d'erreurs de même niveaux (RuntimeException) dans l'application, chacunes ayant un contexte différent.

ConnectionException
: Permet de gerer les problèmes de connexions avec un client

ServerException
: Permet de gerer les problèmes venant du serveur, et d'eventuellement couper le serveur

UnsupportedCommandException
: Dans le cas où une commande serait impossible à executer car inconnue



```
        public class ConnectionException extends RuntimeException {
            public ConnectionException() {
            }
            public ConnectionException(String message) {
                super(message);
            }
        }
```

Dans cet exemple, une exception peut survenir lorsqu'il est impossible de créer un StreamReader depuis le socket donné,
de ce fait, une exception de connexion est lancée, contenant la classe et la cause de cette exception.
Cette erreur remonte jusqu'a être stopée par une instruction "catch".
Un traitement adequat sera alors exécuté.

```
        public void init() {
           try {
               inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           } catch (IOException e) {
               throw new ConnectionException("CommandHandler - Impossible to initialize reader and writer.");
           }
        }
```

Voir ci-dessous un exemple d'une exception


Codes samples
-------------

###1.Execution d'une commande

Dans ce code, il est facile d'executer une commande selon ce que l'utilisateur a envoyé.
De plus, c'est un exemple d'un traitement d'une exception, dans cet exemple, une commande inconnue repondra que celle-ci n'est
pas implémentée sur le serveur FTP.

```
        public void executeCommand(ClientSession client, CommandRequest commandToExecute) {
           ...
            try {
                Command cmd = getCommand(client, commandToExecute);
                cmd.execute(client, commandToExecute);
            } catch (UnsupportedCommandException e) {
                client.getCommandHandler().sendMessage(ReturnCode.NOTIMPL);
            }
        }
```



###2.Singleton Multithreadé
L'utilisation d'un singleton sur un serveur multithradé requiert l'utilisation de la méthode dites du holder.
La classe contient un constructeur privé, une methode getInstance et une classe interne.

La classe interne serait initialisée une et une seule fois sur le serveur, lors de la toute première mise en 
mémoire de la classe, en effet tous ses blocs statics seront executé une seule fois, et l'instance par la suite sera connue.

Dans le cas contraire, il serait possible de faire une double instanciation lors d'un getInstance == null classique.

```
        /**
         * get unique singleton of instance invoker
         */
        public static UserAccessImpl getInstance() {
            return SingletonHolder.instance;
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
```


###3.ReturnCode
Les messages de retour sont gérés par un énumérateur un peu particulier. En effet, chaque message doit etre représenté sous formes :
"XXX YYYYYYY", XXX = un code de retour à 3 chiffres, un espace et un texte explicatif, suivi par un retour à la ligne.

De cette facon, le message envoyé est toujours bien formulé, une source d'érreur en moins.

```
    public enum ReturnCode {

        OK(200, "OK."),
        WELCOME(220, "Welcome to your favorite ftp server.");
       //... every return code here
       
        private int returnCode;
        private String textToReturn = "";
        
        ReturnCode(int returnCode, String textToReturn) {
            this.returnCode = returnCode;
            this.textToReturn = textToReturn;
        }
        
        //just in case we have to change the text
        public void setText(String text) {
            this.textToReturn = text;
        }
        
        @Override
        public String toString() {
            return this.returnCode + " " + this.textToReturn + "\r\n";
        }
    }
```


###4.Pattern command

Interface Commmand
: L'interface Command définit chaque commande avec une méthode execute.    

Class UserCommand,StorCommand,...
: Chaque classe définit le comportement de chaque commande

Class CommmandInvoker
: Permet d'executer la bonne instance de Command, pour la commande utilisée par l'utilisateur

Class ClientHandler
: Se charge de faire appel à l'invoker de commande.

Class ClientSession
: Instance d'une session d'un utilisateur qui sera eventuellement modifée lors d'une commande.



###5.Abstraction des messages

Cette classe permet d'abstraire et de recevoir/envoyer des messages facilement sur les sockets client/serveurs.
Abstraction et simplification des échanges de messages.

```
        public class CommandHandlerImpl implements CommandHandler {
            private Socket socket;
            private BufferedReader inputStream;
            private BufferedWriter outputStream;

        //constructeur + init + close not copied here ...

            /**
             * Send a message to the client.
             *
             * @param message Message sent to the client.
             */
            @Override
            public void sendMessage(String message) {
                try {
                    outputStream.write(message);
                    outputStream.flush();
                } catch (IOException e) {
                    throw new ConnectionException("CommandHandler - Impossible to send message.");
                }
            }

            /**
             * Used to receive messages from a client.
             */
            @Override
            public CommandRequest receiveMessage() {
                try {
                    return new CommandRequestImpl(inputStream.readLine());
                } catch (IOException e) {
                    throw new ConnectionException("CommandHandler - Impossible to receive message.");
                }
            }
        }
```

