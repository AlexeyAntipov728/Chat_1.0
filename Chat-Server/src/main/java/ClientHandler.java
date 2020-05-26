import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ClientHandler implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean running;
    private String nickName;

    public ClientHandler(Socket socket, String nickName) throws IOException {
        this.socket = socket;
        this.nickName = nickName;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        running = true;
        welcome();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void welcome() throws IOException {
        out.writeUTF("Hello " + nickName);
        out.flush();
    }


    public void broadCastMessage(String message) {
        for (ClientHandler client : Server.getClients()) {
            if (!client.equals(this)) {
                client.sendMessage(nickName + " in " + getTime() + ": " + message);
            }
        }
    }

    public synchronized void broadcastClientsList() {
        StringBuilder sb = new StringBuilder();
        for (ClientHandler client : Server.getClients()) {
            sb.append(client.getNickName() + "\n");
        }
        sendMessage("/" + sb.toString());

    }


    public void privateMessage(String message) {
        if (message.startsWith("to/")) {
            String[] split = message.split("to/");
            String[] userToPm = split[1].split("/");
            for (ClientHandler client : Server.getClients()) {
                if (client.getNickName().equals(userToPm[0])) {
                    client.sendMessage("from " + this.nickName + message);
                    this.sendMessage("to " + userToPm[0] + message);
                }
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    public void run() {

        while (running) {
            try {
                if (socket.isConnected()) {
                    String clientMessage = in.readUTF();
                    if (clientMessage.startsWith("nickname")) {
                        String[] clientName = clientMessage.split("/");
                        setNickName("[" + clientName[1] + "]");
//                        out.writeUTF("///" + nickName);
                    }

                    if (clientMessage.equals("_exit_")) {
                        Server.getClients().remove(this);
                        sendMessage(clientMessage);
                        break;
                    }

//                    out.writeUTF("/// " + Server.getClients());

                    System.out.println(clientMessage);
                    broadCastMessage(clientMessage);
                    privateMessage(clientMessage);

                }
                broadcastClientsList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
