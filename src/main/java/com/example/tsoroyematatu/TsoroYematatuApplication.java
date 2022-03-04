package com.example.tsoroyematatu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TsoroYematatuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TsoroYematatuApplication.class.getResource("hello-view.fxml"));
        loader.setResources(ResourceBundle.getBundle("com.example.tsoroyematatu.i18n", new Locale("pt_br", "pt_BR")));
        Scene scene = new Scene(loader.load(), 723, 365);
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
