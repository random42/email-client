import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TestClient {

    public static void main(String[] args) {
        try {
            debug("start");
            Socket socket = new Socket("localhost", 4999);
            debug(socket);
            OutputStream os = socket.getOutputStream();
            debug("os");
            ObjectOutputStream ous = new ObjectOutputStream(os);
            debug("ous");
            ous.writeObject("ciao");
            debug("sent");
            InputStream is = socket.getInputStream();
            debug("is");
            ObjectInputStream ois = new ObjectInputStream(is);
            debug("ois");
            String s = (String)ois.readObject();
            debug(s);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void debug(Object... args) {
        for (Object a : args) {
            System.out.print(a);
        }
        System.out.println();
    }
}
