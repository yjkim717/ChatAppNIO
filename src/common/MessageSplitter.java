package common;

import java.nio.ByteBuffer;

public class MessageSplitter {

    public static String bufferToString(ByteBuffer msgSize) {
        msgSize.flip();
        byte[] bytes = new byte[msgSize.remaining()];
        msgSize.get(bytes);
        return new String(bytes);
    }

//    public static ByteBuffer headerSplitter(ByteBuffer msgSize) throws IllegalAccessException {
//        ByteBuffer header = msgSize.get((byte)8);
//        byte[] body = new byte[msgSize.get(msgSize.remaining())];
//        if (passTheProtocol(ByteBuffer.wrap(header))) {
//            return ByteBuffer.wrap(body);
//        } else {
//            throw new IllegalAccessError("Unmatched Magic Number");
//        }
//
//    }


    public static boolean passTheProtocol(ByteBuffer rawBuffer) throws IllegalAccessException{
        //byte[] newHeader = new byte[8];
        /*
        Todo : while 빼기
         */
        while(magicBegins(rawBuffer.get(0))) {
            //newHeader[0] = 14;
            return true;
        }
        return false;

    }

//    public static byte typeConfig(byte type) {
//        //0-register, 1-unregister, 2-msg
//        if(type == 0) {
//
//        } else if (type == 1) {
//
//        } else if (type == 2){
//            return 2;
//        }
//    }

    public static ByteBuffer sendHeader(ByteBuffer bodylength) {
        bodylength.flip();
        int length = bodylength.limit();
        ByteBuffer finalMsg = ByteBuffer.allocate(8+length);
        finalMsg.put( (byte)14);
        finalMsg.put((byte)2);
        finalMsg.put((byte)1);
        finalMsg.put((byte)1);
        finalMsg.putInt(length);
        finalMsg.put(bodylength);
        return finalMsg;
    }

    private static boolean magicBegins(byte magic) throws IllegalAccessException {
        if(magic == 14) {
            return true;
        }else {
            System.out.println("You are From China Bye");
            throw new IllegalAccessError();
        }

    }

}
