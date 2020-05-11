import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerTest {
    public static void main(String[] args) throws IOException {
        ServerSocket srv = new ServerSocket(8080);
        System.out.println("Server run");
        Socket socket = srv.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("В чате новенький");
        out.flush();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String word = in.readUTF();
        System.out.println(word);


    }
}
