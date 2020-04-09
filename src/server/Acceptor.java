package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static server.ReadWrite.clients;
import static server.ReadWrite.clients_list;

public class Acceptor{
    private static SocketChannel sc;
    private static ByteBuffer buf;



    public static void accept (SelectionKey key) {
        try{
            sc = ((ServerSocketChannel) key.channel()).accept();
            sc.configureBlocking(false);
            sc.register(ChannelManager.selector, SelectionKey.OP_READ);
            clients.put(sc, buf.allocateDirect(2048));

        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }

    }

}