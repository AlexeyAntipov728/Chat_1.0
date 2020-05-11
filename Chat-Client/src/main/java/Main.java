import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.net.Socket;

public class Main extends Application {


    public static void main(String[] args)  {

        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

       // Socket socket = new Socket("localhost", 8080);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("LiTe Chat");
        Image image = new Image(new FileInputStream("Chat-Client/src/main/resources/Images/chatIcon.png"));
        primaryStage.getIcons().add(image);
        primaryStage.setScene(new Scene(root, 550, 750));
        primaryStage.show();
        primaryStage.setResizable(false);


    }
}
