package com.example.tsoroyematatu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class TsoroYematatuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TsoroYematatuApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 723, 365);
        Image image = new Image("file:icon.png");
        stage.getIcons().add(image);
        stage.setTitle("Tsoro Yematatu!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Context.serverIp = args.length > 0  ? args[0] : "127.0.0.1";
        Context.serverPort = args.length > 1 ? Integer.parseInt(args[1]) : 3322;
        launch();
    }
}
