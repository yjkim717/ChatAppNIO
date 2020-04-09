package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChannelManager {
    private static ServerSocketChannel ssc;
    //private static SocketChannel sc;
    public static Selector selector;

    public ChannelManager(int port) throws IOException {
        ssc = ServerSocketChannel.open().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (ssc.isOpen()) {
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator it = readyKeys.iterator();

            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                it.remove();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        Acceptor.accept(key);
                    } else if (key.isReadable()) {
                        ReadWrite.read(key);
                    } else if (key.isWritable()) {
                        ReadWrite.write(key);
                    }
                }
            }
        }
    }
}
