import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Socket socket;
    private static DataOutputStream out;
    public TextField textField;
    public ListView<String> List;
    private DataInputStream in;
    public String nickName;
    public TextField clientName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


//        start();

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
            List.getItems().addAll("LiTe " + getTime() + ": " + textField.getText());
            out.writeUTF(textField.getText());
            out.flush();
            textField.requestFocus();
            textField.setText("");
        }
    }

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

    public void start() {
        try {
            Socket socket = new Socket("localhost", 8080);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String server = in.readUTF();
                        if (server.equalsIgnoreCase("/exit")) {
                            break;
                        }
                        List.getItems().addAll(server);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void login(ActionEvent actionEvent) {
        start();
    }

    public String nickName(ActionEvent actionEvent) {
        nickName = clientName.getText();
        return nickName;
    }
}


