package com.company;

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
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class HistoryView extends ResizableView {

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

    ResourceBundle bundle = ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR"));

    @FXML
    private void initClient(){
        try {
            Context context = Context.getInstance();
            TsoroYematatuServerInterface server = context.getServer();
            context.addListening(this);
            connectionStatus.setText(bundle.getString("connectedOK"));
            try {
                ArrayList<GameDescription> games = server.getHistory(Context.getInstance().getPath());
                ArrayList<Game> gamesObject = new ArrayList<>();
                for (GameDescription game: games) {
                    gamesObject.add(new Game(game.getGame(), game.getResult(), "", ""));
                }
                tableGames.setItems(FXCollections.observableArrayList(gamesObject));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            connectionStatus.setText(bundle.getString("connectedError"));
        }
    }

    @FXML
    public void switchToMenu(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "menu-view.fxml");
    }

}
