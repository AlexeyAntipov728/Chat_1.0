import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean running;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        running = true;
    }


    public void broadCastMessage(String message) throws IOException {
        for (ClientHandler client : Server.getClients()) {
            //if (!client.equals(this)) {
            client.sendMessage(message);
            // }
        }
    }

    public void sendMessage(String message) throws IOException {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (running) {
            try {
                if (socket.isConnected()) {
                    String clientMessage = in.readUTF();
                    if (clientMessage.equals("_exit_")) {
                        Server.getClients().remove(this);
                        sendMessage(clientMessage);
                        break;
                    }
                    System.out.println(clientMessage);
                    broadCastMessage(clientMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
