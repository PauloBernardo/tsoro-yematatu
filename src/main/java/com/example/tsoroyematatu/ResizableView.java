package com.example.tsoroyematatu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ResizableView implements ContextListening {
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
        Parent rootMain = fxmlLoader.load();
        ResizableView controller = fxmlLoader.getController();
        Stage stage = (Stage) oldScene.getWindow();
        controller.setStage(stage);
        Scene scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handleResponse(String message) throws Exception {}
}
