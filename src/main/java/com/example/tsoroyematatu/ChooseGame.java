package com.example.tsoroyematatu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;

public class ChooseGame implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public Label nameText;
    @FXML
    public ScrollPane gamesPanel;
    @FXML
    public VBox gamesBox;

    private Stage stage;
    private Scene scene;
    private Socket client;

    @FXML
    public void initialize() {
        initClient();
    }

    @FXML
    private void initClient(){
        try {
            Context context = Context.getInstance();
            client = context.getClient();
            context.addListening(this);
            connectionStatus.setText("Connection established!");
            PrintStream saida = new PrintStream(client.getOutputStream());
            saida.println("getChooseMatch:");
        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    @FXML
    public void switchToMenu(MouseEvent event) throws IOException {
        Context.getInstance().removeListening(this);
        Parent rootMain = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void handleResponse(String message) {
        if (message.startsWith("getChooseMatch:OK,")) {
            String games[] = message.substring(18).split(",");

            gamesBox.getChildren().clear();
            for (String game: games) {
                HBox hBox = new HBox();
                hBox.setMinWidth(320);
                hBox.setMaxWidth(320);
                hBox.setMinHeight(40);
                //Creating a Label
                Label label = new Label(game);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                //Setting font to the label
                label.getStyleClass().add("gamesLabel");
                hBox.getChildren().add(label);
                hBox.setOnMouseClicked(mouseEvent -> {
                    System.out.println(game);
                });
                gamesBox.getChildren().add(hBox);
            }
        }
    }

}
