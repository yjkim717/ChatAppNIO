package server;

import common.MessageSplitter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadWrite {
    private static SocketChannel sc;
    public static Map<SocketChannel, ByteBuffer> clients = new HashMap<>();
    public static HashMap<SocketChannel, String> clients_list = new HashMap<>();
    private static ByteBuffer rawBuffer = ByteBuffer.allocateDirect(1024);
    private static ByteBuffer header;
    private static ByteBuffer body;
    private static ByteBuffer finalBuffer;


    public ReadWrite(SelectionKey key) {
        // this.bodyLength = 0;
        this.rawBuffer = ByteBuffer.allocateDirect(1024);
    }

    public static void read(SelectionKey key) throws IOException {
        try {
            sc = (SocketChannel) key.channel();
            sc.read(rawBuffer);
            rawBuffer.flip();
            MessageSplitter.passTheProtocol(rawBuffer);
            if (rawBuffer.get(1) == 2) {
                broadcast(body, key);
            }
            /*
            Todo: 끝는것에대한 처리 두기
             */
        } catch (IllegalAccessException e) {
            // connection close
            sc.close();
        }

    }

    private static void broadcast(ByteBuffer body, SelectionKey key) throws IOException {
        body.position(0);
        for (Map.Entry<SocketChannel, ByteBuffer> client : clients.entrySet()) {
            if(!client.getValue().hasRemaining()) {
                client.getValue().put(body);
            } else {
                ByteBuffer finalMsg = MessageSplitter.sendHeader(body);
                client.getKey().write(finalMsg);
                if(body.hasRemaining()) {
                    ByteBuffer bufToSave = body.duplicate();
                    client.getValue().put(bufToSave);
                    key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                }
            }
        }


    }


    public static void write(SelectionKey key) throws IOException {
        sc = (SocketChannel) key.channel();
        ByteBuffer buffer = clients.get(sc);
        if (buffer.hasRemaining()) {
            buffer.flip();
            sc.write(buffer);
            if (buffer.hasRemaining()) {
                buffer.compact();
            } else {
                key.interestOps(SelectionKey.OP_READ);
            }

        }

    }
}
