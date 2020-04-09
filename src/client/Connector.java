package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;

public class Connector extends ServerConnection{

    public Connector(Socket socket) {
        super(socket);
    }

    public static void doFinishConnect(SelectionKey key) throws IOException {
        //complete the connection process initiated by a call of connect(socketaddress)
        boolean connected = sc.finishConnect();
        System.out.println("Connected????" + connected);
        key.interestOps(SelectionKey.OP_READ);

    }
}
