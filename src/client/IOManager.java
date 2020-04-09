package client;

import common.MessageSplitter;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;

public class IOManager extends ServerConnection{
    private static ByteBuffer msgSize = ByteBuffer.allocateDirect(1024);
    private static String rcvdMsg;
    private static ByteBuffer body;

    public IOManager(Socket socket) {
        super(socket);
    }

    public static void readFromServer (SelectionKey key) throws IOException {
        try {
            sc.read(msgSize);
            msgSize.flip();
            MessageSplitter.passTheProtocol(msgSize);
            if (msgSize.get(1) == 2) {
                msgSize.position(8);
                System.out.println(MessageSplitter.bufferToString(msgSize));
            }
        } catch (IllegalAccessException e) {

        }

    }

    public static void sendToServer(SelectionKey key, ByteBuffer msgFromServer) throws IOException {
        sc.write(msgFromServer);

//        Thread tmp = new Thread(() -> {
//            System.out.println("hello");
//        });
//
//        tmp.start();
    }
}
