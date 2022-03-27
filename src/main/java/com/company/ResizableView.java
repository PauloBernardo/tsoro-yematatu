package com.company;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class ResizableView implements TsoroYematatuClient {
    protected Stage stage;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
        anchorPane.setPrefWidth(stage.getScene().getWidth());
        anchorPane.setPrefHeight(stage.getScene().getHeight());
        anchorPane.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setMaxHeight(Double.MAX_VALUE);

        stage.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> anchorPane.setPrefWidth(newSceneWidth.doubleValue()));
        stage.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> anchorPane.setPrefWidth(newSceneHeight.doubleValue()));
    }

    public void switchBetweenScreen(Scene oldScene, String newScreen) throws IOException {
        Context.getInstance().removeListening(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(newScreen)));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR")));
        Parent rootMain = fxmlLoader.load();
        ResizableView controller = fxmlLoader.getController();
        Stage stage = (Stage) oldScene.getWindow();
        controller.setStage(stage);
        Scene scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void getName(String name) throws RemoteException {

    }

    @Override
    public void startRandomMatch() throws RemoteException {

    }

    @Override
    public void startChooseMatch() throws RemoteException {

    }

    @Override
    public void cancelGame() throws RemoteException {

    }

    @Override
    public void endGame(String status) throws Exception {

    }

    @Override
    public void chooseColor(String color) throws Exception {

    }

    @Override
    public void move(int older, int newer) throws Exception {

    }

    @Override
    public void turn() throws RemoteException {

    }

    @Override
    public void begin(String player) throws RemoteException {

    }

    @Override
    public void choosePlayer(String color) throws Exception {

    }

    @Override
    public void drawGame(String response) throws Exception {

    }

    @Override
    public void chatMessage(String name, String message) throws RemoteException {

    }

}
