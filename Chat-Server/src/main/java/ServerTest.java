import jdk.nashorn.internal.ir.IfNode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ServerTest {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        ServerSocket srv = new ServerSocket(8080);
        System.out.println("Server run");

        Socket socket = srv.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        out.writeUTF("В чате новенький");
        out.flush();
        String client = in.readUTF();
        System.out.println(client);
        while (true) {


            if(client.equals("exit"))
                break;
            else {

                String server = sc.nextLine();
                out.writeUTF("Server say:" + server);
            }



        }

    }
}
