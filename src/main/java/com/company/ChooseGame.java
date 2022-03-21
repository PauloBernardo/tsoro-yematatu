package com.company;

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

    private TsoroYematatuServerInterface server;

    ResourceBundle bundle = ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR"));

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
            server = context.getServer();
            context.addListening(this);
            connectionStatus.setText(bundle.getString("connectedOK"));

            try {
                ArrayList<GameDescription> games = server.getChooseMatch(Context.getInstance().getPath());
                ArrayList<Game> gamesObject = new ArrayList<>();
                for (GameDescription game: games) {
                    gamesObject.add(new Game("", "", game.getPlayer(), game.getId()));
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
    public void handleTableClick(MouseEvent event) throws IOException {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
            try {
                if(server.startChooseMatch(Context.getInstance().getPath(), ((Game)tableGames.getSelectionModel().getSelectedItem()).getId())) {
                    Context.getInstance().removeListening(this);
                    FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
                    fxmlLoader.setResources(ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR")));
                    Parent rootMain = fxmlLoader.load();
                    GameView controller = fxmlLoader.getController();
                    controller.setType("choose");
                    controller.setStage(stage);
                    Stage stage = (Stage) anchorPane.getScene().getWindow();
                    Scene scene = new Scene(rootMain);
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void switchToMenu(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "menu-view.fxml");
    }


    @FXML
    public void handleResponse(String message) {}

}
