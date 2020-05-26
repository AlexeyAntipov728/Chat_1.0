import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Socket socket;
    private static DataOutputStream out;
    private static DataInputStream in;
    public TextField textField;
    public ListView<String> List;
    public String nickName;
    public TextField clientName;
    public ListView<String> Users;
    public ImageView Background;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


//        start();
//        updateUserList();

        List.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);

                } else {
                    setMinWidth(USE_COMPUTED_SIZE);
                    setMaxWidth(USE_COMPUTED_SIZE);
                    setPrefWidth(450);
                    setWrapText(true);
                    setText(item);
                }
            }
        });
    }

    public void SendMess() throws IOException {

        if (!textField.getText().equals("")) {
            List.getItems().addAll("[" + nickName + "] in " + getTime() + ": " + textField.getText());
            out.writeUTF(textField.getText());
            out.flush();
            textField.requestFocus();
            textField.setText("");
        }
    }
//    "[" + nickName + "] in " + getTime() + ": " +
//    "[ " + nickName + " ] in "

    public String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public void Enter(ActionEvent actionEvent) throws IOException {
        SendMess();
    }

    public void Send(ActionEvent actionEvent) throws IOException {
        SendMess();
    }

    public void showAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("LiTe chat ver. 1.0.\nI think that it's beginning of something great.");
        alert.showAndWait();
    }

    public void Exit(ActionEvent actionEvent) throws IOException {
        String msgToExit = "_exit_";
        out.writeUTF(msgToExit);
        System.exit(0);
    }

    public void closeApp() throws IOException {
        String msgToExit = "_exit_";
        out.writeUTF(msgToExit);
    }

    public void start() {
        try {
            Socket socket = new Socket("localhost", 8080);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(() -> {
            try {
                while (true) {
                    String server = in.readUTF();
                    if (server.equalsIgnoreCase("/exit")) {
                        break;
                    }

                    if (server.startsWith("/")) {
                        Users.getItems().clear();
                        String[] nicks = server.split("/");

                        Users.getItems().add(nicks[1]);
                    } else List.getItems().addAll(server);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

//    public synchronized void  updateUserList() throws InterruptedException {
//        Thread t1 = new Thread(() -> {
//            try {
//                while (true){
//                    String server1 = in.readUTF();
//                    if (server1.startsWith("nickname/")){
//                        String[] clientName = server1.split("/");
//                        Users.getItems().addAll(clientName[1]);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
////        t1.setDaemon(true);
//        t1.start();
//    }

    public void login(ActionEvent actionEvent) {
        start();
//        updateUserList();
    }

    public void enterNickName(ActionEvent actionEvent) throws IOException {
        nickName = clientName.getText();
        out.writeUTF("nickname/" + nickName);
    }

    public void ChangeBackground(ActionEvent actionEvent) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("Chat-Client/src/main/resources/Images/backgroundImage.png"));
        Background.setImage(image);
    }
}


