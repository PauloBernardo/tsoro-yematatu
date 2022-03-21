package com.company;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class DraggableMaker {

    private double mouseAnchorX;
    private double mouseAnchorY;
    private double startX;
    private double startY;
    private final GameView parent;

    public DraggableMaker(GameView parent) {
        this.parent = parent;
    }

    public void makeDraggable(Node node) {
        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getSceneX() - node.getTranslateX();
            mouseAnchorY = mouseEvent.getSceneY() - node.getTranslateY();
            startX = node.getTranslateX();
            startY = node.getTranslateY();
            parent.handleBallDragStarted((Circle) node);
        });

        node.setOnMouseDragged(mouseEvent -> {
            node.setTranslateX(mouseEvent.getSceneX() - mouseAnchorX);
            node.setTranslateY(mouseEvent.getSceneY() - mouseAnchorY);
        });

        node.setOnMouseReleased(mouseEvent -> parent.handleBallDragDropped((Circle) node, startX, startY));

    }
}
