import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;


public class Server {
    private final static int PORT = 8080;
    private final static String HOST = "localhost";
    private static int cnt = 1;
    private static ConcurrentLinkedDeque<ClientHandler> clients;
    private boolean running;

    public Server(int port) {
        running = true;
        clients = new ConcurrentLinkedDeque<>();

        try (ServerSocket srv = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            while (running) {
                Socket socket = srv.accept();
                ClientHandler client = new ClientHandler(socket, "client #" + cnt);

                cnt++;
                clients.add(client);
                client.dbConnect();
                System.out.println(client.getNickName() + " accepted!");
//                Scanner scanner = new Scanner(System.in);
//                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                String test = scanner.nextLine();
//                out.writeUTF("Server " + test);

                new Thread(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConcurrentLinkedDeque<ClientHandler> getClients() {
        return clients;
    }

    public static void main(String[] args) {
        new Server(PORT);
    }

}