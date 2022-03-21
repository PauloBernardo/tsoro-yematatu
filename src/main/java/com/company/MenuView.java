package com.company;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuView extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public Label nameText;

    @FXML
    public void initialize() {
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
                nameText.setText(server.getName(Context.getInstance().getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            connectionStatus.setText(bundle.getString("connectedError"));
        }
    }

    @FXML
    public void switchToConnectServer(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "connect-server.fxml");
    }

    public void onButtonClick(ActionEvent event, String type) throws IOException {
        Context.getInstance().removeListening(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("game-view.fxml")));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR")));
        Parent rootMain = fxmlLoader.load();
        GameView controller = fxmlLoader.getController();
        controller.setType(type);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        controller.setStage(stage);
        Scene scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onButtonRandomClick(ActionEvent event) throws IOException {
        this.onButtonClick(event, "random");
    }

    @FXML
    public void onButtonNewClick(ActionEvent event) throws IOException {
        this.onButtonClick(event, "new");
    }

    @FXML
    public void onButtonChooseClick(ActionEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "chooseGame.fxml");
    }

    @FXML
    public void onButtonHistoricClick(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "history-view.fxml");
    }

    @FXML
    public void handleResponse(String message) {
        if (message.startsWith("getName:OK")) {
            nameText.setText(message.substring(11));
        }
    }

}
