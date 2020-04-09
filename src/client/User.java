package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class User {
    public static void main(String[] args) {
        //default values
        int portNumber = 1004;
        String host = "localhost";

        if (args.length < 2) {
            System.out.println("Usage : java User <host> <portNumber>\n"
                    + "Now using host= " + host + ", portNumber= " + portNumber);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        try {
            new ServerConnection(host, portNumber).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
