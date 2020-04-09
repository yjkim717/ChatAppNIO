package client;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class UserInput extends Thread{
    private SelectionKey key;
    private SocketChannel sc;

    public UserInput(SocketChannel sc) {
       this.sc = sc;
    }

    @Override
    public void run() {
        ByteBuffer msgFromServer = ByteBuffer.allocateDirect(1024);
        try{
            while(true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.next();
                msgFromServer.clear();
                msgFromServer.put(msg.getBytes());
                msgFromServer.flip();
                System.out.println("position?? : " + msgFromServer.position());
                System.out.println("limit??" + msgFromServer.limit());
                /*
                Todo: Header추가
                 */
                sc.write(msgFromServer);
                //IOManager.sendToServer(key, msgFromServer);


                //나는
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
