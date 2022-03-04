package com.example.tsoroyematatu;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChooseGame extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public TableView tableGames;
    @FXML
    private TableColumn<Game, String> playerCol;
    @FXML
    private TableColumn<Game, String> idCol;
    @FXML
    public AnchorPane anchorPane;

    private Socket client;

    ResourceBundle bundle = ResourceBundle.getBundle("com.example.tsoroyematatu.i18n", new Locale("pt_br", "pt_BR"));

    @FXML
    public void initialize() {
        playerCol.setCellValueFactory(
                new PropertyValueFactory<>("player"));
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        tableGames.setRowFactory(tableView -> {
            final TableRow<Game> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
                final Game person = row.getItem();

                if (row.isHover() && person != null) {
                    row.setCursor(Cursor.HAND);
                } else {
                    row.setCursor(Cursor.DEFAULT);
                }
            });

            return row;
        });
        initClient();
    }

    @FXML
    private void initClient(){
        try {
            Context context = Context.getInstance();
            client = context.getClient();
            context.addListening(this);
            connectionStatus.setText(bundle.getString("connectedOK"));
            PrintStream saida = new PrintStream(client.getOutputStream());
            saida.println("getChooseMatch:");
        } catch (IOException ex) {
            connectionStatus.setText(bundle.getString("connectedError"));
        }
    }

    @FXML
    public void handleTableClick(MouseEvent event) throws IOException {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
            PrintStream saida = new PrintStream(client.getOutputStream());
            Game game = (Game)tableGames.getSelectionModel().getSelectedItem();
            if (game != null) {
                saida.println("startChooseMatch:" + game.getPlayer() + "#" + game.getId());
            }
        }
    }

    @FXML
    public void switchToMenu(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "menu-view.fxml");
    }


    @FXML
    public void handleResponse(String message) throws IOException {
        if (message.startsWith("getChooseMatch:OK,")) {
            String games[] = message.substring(18).split(",");
            ArrayList<Game> gamesObject = new ArrayList<>();
            for (String game: games) {
                gamesObject.add(new Game("", "", game.split("#")[0], game.split("#")[1]));
            }
            tableGames.setItems(FXCollections.observableArrayList(gamesObject));
        }
        if (message.startsWith("startChooseMatch:OK,start")) {
            Context.getInstance().removeListening(this);
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
            fxmlLoader.setResources(ResourceBundle.getBundle("com.example.tsoroyematatu.i18n", new Locale("pt_br", "pt_BR")));
            Parent rootMain = fxmlLoader.load();
            GameView controller = fxmlLoader.getController();
            controller.setType("choose");
            controller.setStage(stage);
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            Scene scene = new Scene(rootMain);
            stage.setScene(scene);
            stage.show();
        }
    }

}
