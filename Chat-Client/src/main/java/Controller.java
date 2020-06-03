import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public TextField login = new TextField();


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
        Users.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setMinWidth(USE_COMPUTED_SIZE);
                    setMaxWidth(USE_COMPUTED_SIZE);
                    setPrefWidth(199);
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
        System.exit(0);
    }

    public void start() {

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

    public void login(ActionEvent actionEvent) throws IOException {
        try {
            Socket socket = new Socket("localhost", 8080);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage authStage = new Stage();
        Image image = new Image(new FileInputStream("Chat-Client/src/main/resources/Images/chatIcon.png"));
        authStage.setTitle("LiTe Chat Auth");
        authStage.setMaxHeight(250);
        authStage.setMaxWidth(400);
        authStage.getIcons().add(image);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setY(350);
        authStage.setX(750);

        login.setLayoutX(100);
        login.setLayoutY(25);
        login.setPrefWidth(200);
        login.setPrefHeight(25);

        TextField password = new TextField();
        password.setLayoutX(100);
        password.setLayoutY(60);
        password.setPrefWidth(200);
        password.setPrefHeight(25);

        Button loginBtn = new Button();
        loginBtn.setLayoutX(150);
        loginBtn.setLayoutY(100);
        loginBtn.setPrefWidth(100);
        loginBtn.setPrefHeight(25);
        loginBtn.setText("Login");

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    out.writeUTF("auth/" + login.getText() + " " + password.getText());
                    login.setText("");
                    password.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(login);
        anchorPane.getChildren().add(password);
        anchorPane.getChildren().add(loginBtn);
        Scene authScene = new Scene(anchorPane, 400, 250);
        authStage.setScene(authScene);
        authStage.setResizable(false);
        authStage.show();
        Thread t1 = new Thread(() -> {
            try {

                while (true) {
                    String authOk = in.readUTF();
                    if (authOk.equals("authok")) {
                        clientName.setText(login.getText());
                        authStage.close();
                        start();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.setDaemon(true);
        t1.start();
    }

    public void ChangeBackground(ActionEvent actionEvent) throws FileNotFoundException {
        Image image = new Image(new FileInputStream("Chat-Client/src/main/resources/Images/backgroundImage.png"));
        Background.setImage(image);
    }
}



