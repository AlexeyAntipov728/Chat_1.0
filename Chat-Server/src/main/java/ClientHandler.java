import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler implements Runnable {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean running;
    private String nickName;

    public ClientHandler(Socket socket, String nickName) throws IOException, SQLException, ClassNotFoundException {
        this.socket = socket;
        this.nickName = nickName;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        running = true;
        welcome();
        dbConnect();
    }

    public void dbConnect() throws SQLException, ClassNotFoundException, IOException {
        String authMessage = in.readUTF();
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\756\\testDB.db");
        Statement stmt = connection.createStatement();
        System.out.println("DB work");
        if (authMessage.startsWith("auth")) {
            String[] logPass = authMessage.split("/");
            String[] loginPassword = logPass[1].split(" ");
            stmt.executeUpdate("INSERT INTO users(login, password) VALUES('" + loginPassword[0] + "', '" + loginPassword[1] + "' );");
            ResultSet resultSet = stmt.executeQuery("SELECT login, password FROM users ");
//            WHERE login = '" + loginPassword[0] + "', password = '" + loginPassword[1] + "';"
            while (resultSet.next()) {

                if (resultSet.getString("login").equals(loginPassword[0]) && resultSet.getString("password").equals(loginPassword[1])) {
                    System.out.println("yes");
                    setNickName("[" + loginPassword[0] + "]");
                    out.writeUTF("authok");
                    break;
                }
//               else System.out.println("false");
            }
        }
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
//                    Это теперь делается на этапе работы с базой
//                    if (clientMessage.startsWith("nickname")) {
//                        String[] clientName = clientMessage.split("/");
//                        setNickName("[" + clientName[1] + "]");
////                        out.writeUTF("///" + nickName);
//                    }

                    if (clientMessage.equals("_exit_")) {
                        Server.getClients().remove(this);
                        sendMessage(clientMessage);
                        break;
                    }

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
