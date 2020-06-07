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
import java.util.Scanner;

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
    public InputStream historyIn;
    public OutputStream historyOut;
    public File file;
    public Label UserNAME;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Socket socket = new Socket("localhost", 8080);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File("Chat-Client/src/main/resources/History/History.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            historyIn = new FileInputStream("Chat-Client/src/main/resources/History/History.txt");
            historyOut = new FileOutputStream("Chat-Client/src/main/resources/History/History.txt", true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(file.exists());
        System.out.println(file.getAbsolutePath());


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
            String toHistory = "[" + nickName + "] in " + getTime() + ": " + textField.getText() + "\n";
            historyOut.write(toHistory.getBytes());
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

    public void getHistory() throws IOException {

        Scanner in = new Scanner(file);
        while (in.hasNext()) {
            List.getItems().addAll(in.nextLine());
        }
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
                getHistory();
                while (true) {
                    String server = in.readUTF();
                    if (server.equalsIgnoreCase("/exit")) {
                        break;
                    }
                    if (server.startsWith("/")) {
                        Users.getItems().clear();
                        String[] nicks = server.split("/");

                        Users.getItems().add(nicks[1]);

                    } else {
                        List.getItems().addAll(server);
                        historyOut.write(server.getBytes());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();

    }

    public void login(ActionEvent actionEvent) throws IOException {

        Stage authStage = new Stage();
        Image image = new Image(new FileInputStream("Chat-Client/src/main/resources/Images/chatIcon.png"));
        authStage.setTitle("LiTe Chat Auth");
        authStage.setMaxHeight(250);
        authStage.setMaxWidth(400);
        authStage.getIcons().add(image);
        authStage.initModality(Modality.APPLICATION_MODAL);
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
                    nickName = login.getText();
                    UserNAME.setText(nickName);
                    login.setText("");
                    password.setText("");
                    authStage.close();

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
                        List.getItems().add("Welcome " + nickName);
                        start();
                        break;
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




