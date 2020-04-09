package client;

import com.sun.org.apache.bcel.internal.generic.Select;
import server.Acceptor;
import server.ReadWrite;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ServerConnection extends Thread {
    private static Socket socket;
    public static SocketChannel sc;
    private InetSocketAddress address;
    public static boolean connected;
    public Selector selector;
    private static String host;
    private static int portNumber;
    public static SelectionKey key;
    public static ByteBuffer msgFromServer;



    public ServerConnection(String host, int portNumber) {
        this.host = host;
        this.portNumber = portNumber;


    }

    public ServerConnection(Socket socket) { this.socket = socket; }


    @Override
    public void run() {
        try {
            initConnection();
            initUser();
            selector = Selector.open();
            sc.register(selector, SelectionKey.OP_CONNECT);


            while(connected) {
                selector.select();
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator it = readyKeys.iterator();
                while (it.hasNext()) {
                    key = (SelectionKey) it.next();
                    it.remove();
                    if (key.isValid()) {
                        if (key.isConnectable()) {
                            Connector.doFinishConnect(key);
                            new UserInput(sc).start();
                        } else if (key.isReadable()) { //server가 끊으면 FIN packet이 날라와
                            IOManager.readFromServer(key);
                        } else if (key.isWritable()) {
                            //IOManager.sendToServer(key, msgFromServer);
                        }
                    }
                }
            }
        } catch(IOException e) {
            System.out.println("Connection Lost");
            e.printStackTrace();
        }

    }

    private void initUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your id");
    }

    private void initConnection() throws IOException {
        sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress(host, portNumber));
        connected = true;
    }


}
