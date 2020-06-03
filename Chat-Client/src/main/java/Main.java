import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Image image = new Image(new FileInputStream("Chat-Client/src/main/resources/Images/chatIcon.png"));
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("LiTe Chat");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(new Scene(root, 550, 750));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(we -> {
            Controller controller = new Controller();
            try {
                controller.closeApp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
