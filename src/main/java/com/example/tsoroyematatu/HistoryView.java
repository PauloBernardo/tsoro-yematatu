package com.example.tsoroyematatu;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class HistoryView extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public TableView tableGames;
    @FXML
    private TableColumn<Game, String> gameCol;
    @FXML
    private TableColumn<Game, String> resultCol;
    @FXML
    public AnchorPane anchorPane;

    @FXML
    public void initialize() {
        gameCol.setCellValueFactory(
                new PropertyValueFactory<>("game"));
        resultCol.setCellValueFactory(
                new PropertyValueFactory<>("result"));
        initClient();
    }

    @FXML
    private void initClient(){
        try {
            Context context = Context.getInstance();
            Socket client = context.getClient();
            context.addListening(this);
            connectionStatus.setText("Connection established!");
            PrintStream saida = new PrintStream(client.getOutputStream());
            saida.println("getHistory:");
        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    @FXML
    public void switchToMenu(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "menu-view.fxml");
    }

    @FXML
    public void handleResponse(String message) {
        if (message.startsWith("getHistory:OK,")) {
            String[] games = message.substring(14).split(",");
            ArrayList<Game> gamesObject = new ArrayList<>();
            for (String game: games) {
                gamesObject.add(new Game(game.split("----->")[0], game.split("----->")[1], "", ""));
            }
            tableGames.setItems(FXCollections.observableArrayList(gamesObject));
        }
    }

}
