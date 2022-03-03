package com.example.tsoroyematatu;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Optional;

public class GameView extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Label nameText;
    @FXML
    public Label loadingLabel;
    @FXML
    public Pane gamePanel;
    @FXML
    public Pane chooseColorPanel;
    @FXML
    public Pane choosePlayer;
    @FXML
    public Pane board;
    @FXML
    public Circle blackColor;
    @FXML
    public Circle yellowColor;
    @FXML
    public Circle blueColor;
    @FXML
    public Circle redColor;
    @FXML
    public Circle greenColor;
    @FXML
    public Circle player1Ball;
    @FXML
    public Circle player2Ball;
    @FXML
    public Label turnLabel;
    @FXML
    public Circle playerBall1;
    @FXML
    public Circle playerBall2;
    @FXML
    public Circle playerBall3;
    @FXML
    public Pane anotherBallsPanel1;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendButton;
    @FXML
    public Button buttonDraw;
    @FXML
    public VBox messageBox;

    private int playerBall1Position = -1;
    private int playerBall2Position = -1;
    private int playerBall3Position = -1;
    private Circle selectedPlayerBall;

    @FXML
    public Circle anotherBall1;
    @FXML
    public Circle anotherBall2;
    @FXML
    public Circle anotherBall3;


    private int anotherBall1Position = -1;
    private int anotherBall2Position = -1;

    @FXML
    public Pane playerBallsPanel;
    @FXML
    public Circle gameBall0;
    @FXML
    public Circle gameBall1;
    @FXML
    public Circle gameBall2;
    @FXML
    public Circle gameBall3;
    @FXML
    public Circle gameBall4;
    @FXML
    public Circle gameBall5;
    @FXML
    public Circle gameBall6;

    private Socket client;
    private String type;
    private String playerTurn;
    private String actualPlayTurn;

    private final DraggableMaker draggableMaker = new DraggableMaker(this);

    private PrintStream output = null;
    private boolean ignoreClick = false;

    private double previousPositionX;
    private double previousPositionY;

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
            Platform.runLater(() -> {
                try {
                    output = new PrintStream(client.getOutputStream());
                    switch (this.type) {
                        case "new":
                            output.println("startNewMatch:");
                            break;
                        case "random":
                            output.println("startRandomMatch:");
                            break;
                        case "choose":
                            board.getChildren().add(chooseColorPanel);
                            chooseColorPanel.setLayoutX(50);
                            chooseColorPanel.setLayoutY(100);
                            chooseColorPanel.setOpacity(1);
                            loadingLabel.setText("");
                            break;
                    }
                    output.println("getName:");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            loadingLabel.setText("Loading...");
        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    private int getPlayerBallPosition(Circle circle) {
        switch (circle.getId()) {
            case "playerBall1":
                return playerBall1Position;
            case "playerBall2":
                return playerBall2Position;
            case "playerBall3":
                return playerBall3Position;
        }
        return 7;
    }

    @FXML
    public void handleBallDragStarted(Circle circle) {
        if (circle.getStyleClass().toString().equals("gameBalls")) {
            if (playerBall1.getStyleClass().toString().equals("gameBallsSelected")) {
                playerBall1.getStyleClass().remove("gameBallsSelected");
                playerBall1.getStyleClass().add("gameBalls");
            }
            if (playerBall2.getStyleClass().toString().equals("gameBallsSelected")) {
                playerBall2.getStyleClass().remove("gameBallsSelected");
                playerBall2.getStyleClass().add("gameBalls");
            }
            if (playerBall3.getStyleClass().toString().equals("gameBallsSelected")) {
                playerBall3.getStyleClass().remove("gameBallsSelected");
                playerBall3.getStyleClass().add("gameBalls");
            }
            circle.getStyleClass().remove("gameBalls");
            circle.getStyleClass().add("gameBallsSelected");
            activeBoardBalls();
            this.selectedPlayerBall = circle;
        } else {
            circle.getStyleClass().remove("gameBallsSelected");
            circle.getStyleClass().add("gameBalls");
            removeActiveBalls();
            this.selectedPlayerBall = null;
        }
    }

    @FXML
    public void handleBallDragDropped(Circle node, double startX, double startY) {
        if (node.getStyleClass().toString().contains("gameBallsDisabled")) {
            node.setTranslateX(startX);
            node.setTranslateY(startY);
            return;
        }
        double posX = node.getLayoutX() + node.getTranslateX();
        double posY = node.getLayoutY() + node.getTranslateY();

        int playerPosition = getPlayerBallPosition(node);
        if (playerPosition != -1 && (playerBall1Position == -1 || playerBall2Position == -1 || playerBall3Position == -1)) {
            node.setTranslateX(startX);
            node.setTranslateY(startY);
            return;
        }

        this.selectedPlayerBall = node;
        this.ignoreClick = true;
        this.previousPositionX = startX;
        this.previousPositionY = startY;

        if (Math.abs(posX - gameBall0.getLayoutX()) < 20 && Math.abs(posY - gameBall0.getLayoutY()) < 20 && playerPosition != 0) {
            output.println("move:" + playerPosition + "," + gameBall0.getId().substring(8));
        } else if (Math.abs(posX - gameBall1.getLayoutX()) < 20 && Math.abs(posY - gameBall1.getLayoutY()) < 20 && playerPosition != 1) {
            output.println("move:" + playerPosition + "," + gameBall1.getId().substring(8));
        } else if (Math.abs(posX - gameBall2.getLayoutX()) < 20 && Math.abs(posY - gameBall2.getLayoutY()) < 20 && playerPosition != 2) {
            output.println("move:" + playerPosition + "," + gameBall2.getId().substring(8));
        } else if (Math.abs(posX - gameBall3.getLayoutX()) < 20 && Math.abs(posY - gameBall3.getLayoutY()) < 20 && playerPosition != 3) {
            output.println("move:" + playerPosition + "," + gameBall3.getId().substring(8));
        } else if (Math.abs(posX - gameBall4.getLayoutX()) < 20 && Math.abs(posY - gameBall4.getLayoutY()) < 20 && playerPosition != 4) {
            output.println("move:" + playerPosition + "," + gameBall4.getId().substring(8));
        } else if (Math.abs(posX - gameBall5.getLayoutX()) < 20 && Math.abs(posY - gameBall5.getLayoutY()) < 20 && playerPosition != 5) {
            output.println("move:" + playerPosition + "," + gameBall5.getId().substring(8));
        } else if (Math.abs(posX - gameBall6.getLayoutX()) < 20 && Math.abs(posY - gameBall6.getLayoutY()) < 20 && playerPosition != 6) {
            output.println("move:" + playerPosition + "," + gameBall6.getId().substring(8));
        }
        else {
            node.setTranslateX(startX);
            node.setTranslateY(startY);
        }

    }

    public void setType(String type) {
        this.type = type;
    }

    @FXML
    public void switchToConnectServer() throws IOException {
        PrintStream saida = new PrintStream(client.getOutputStream());
        if (this.actualPlayTurn != null) {
            saida.println("endGame:");
        } else {
            saida.println("cancelGame:");
        }
    }

    @FXML
    public void selectColor(MouseEvent event) throws IOException {
        PrintStream saida = new PrintStream(client.getOutputStream());
        saida.println("chooseColor:" + ((Circle)event.getSource()).getId());
    }

    @FXML
    public void selectPlayer(MouseEvent event) throws IOException {
        if (
            player1Ball.getStyleClass().toString().equals("playerBallsSelected") ||
            player2Ball.getStyleClass().toString().equals("playerBallsSelected")
        )
        {
            return;
        }

        PrintStream saida = new PrintStream(client.getOutputStream());
        if (((Circle)event.getSource()).getId().equals("player1Ball")) {
            player1Ball.getStyleClass().remove("playerBalls");
            player1Ball.getStyleClass().add("playerBallsSelected");
            saida.println("choosePlayer:player1");
        } else {
            player2Ball.getStyleClass().remove("playerBalls");
            player2Ball.getStyleClass().add("playerBallsSelected");
            saida.println("choosePlayer:player2");
        }
    }

    @FXML
    public void activeBoardBalls() {
        if (!gameBall0.getStyleClass().toString().equals("fillGameBall")) {
            gameBall0.getStyleClass().add("emptyBoardBall");
        }
        if (!gameBall1.getStyleClass().toString().equals("fillGameBall")) {
            gameBall1.getStyleClass().add("emptyBoardBall");
        }
        if (!gameBall2.getStyleClass().toString().equals("fillGameBall")) {
            gameBall2.getStyleClass().add("emptyBoardBall");
        }
        if (!gameBall3.getStyleClass().toString().equals("fillGameBall")) {
            gameBall3.getStyleClass().add("emptyBoardBall");
        }
        if (!gameBall4.getStyleClass().toString().equals("fillGameBall")) {
            gameBall4.getStyleClass().add("emptyBoardBall");
        }
        if (!gameBall5.getStyleClass().toString().equals("fillGameBall")) {
            gameBall5.getStyleClass().add("emptyBoardBall");
        }
        if (!gameBall6.getStyleClass().toString().equals("fillGameBall")) {
            gameBall6.getStyleClass().add("emptyBoardBall");
        }
    }

    @FXML
    public void removeActiveBalls() {
        gameBall0.getStyleClass().clear();
        gameBall1.getStyleClass().clear();
        gameBall2.getStyleClass().clear();
        gameBall3.getStyleClass().clear();
        gameBall4.getStyleClass().clear();
        gameBall5.getStyleClass().clear();
        gameBall6.getStyleClass().clear();
    }

    @FXML
    public void selectGameBall(MouseEvent event) {
        if (this.ignoreClick) return;
        Circle circle = ((Circle)event.getSource());
        if (circle.getStyleClass().toString().equals("gameBalls")) {
            if (playerBall1.getStyleClass().toString().equals("gameBallsSelected")) {
                playerBall1.getStyleClass().remove("gameBallsSelected");
                playerBall1.getStyleClass().add("gameBalls");
            }
            if (playerBall2.getStyleClass().toString().equals("gameBallsSelected")) {
                playerBall2.getStyleClass().remove("gameBallsSelected");
                playerBall2.getStyleClass().add("gameBalls");
            }
            if (playerBall3.getStyleClass().toString().equals("gameBallsSelected")) {
                playerBall3.getStyleClass().remove("gameBallsSelected");
                playerBall3.getStyleClass().add("gameBalls");
            }
            circle.getStyleClass().remove("gameBalls");
            circle.getStyleClass().add("gameBallsSelected");
            activeBoardBalls();
            this.selectedPlayerBall = circle;
        } else {
            circle.getStyleClass().remove("gameBallsSelected");
            circle.getStyleClass().add("gameBalls");
            removeActiveBalls();
            this.selectedPlayerBall = null;
        }
    }

    @FXML
    public void handleClickBoardBall(MouseEvent event) {
        Circle circle = ((Circle)event.getSource());
        if (circle.getStyleClass().toString().contains("emptyBoardBall") && selectedPlayerBall != null) {
            output.println("move:" + getPlayerBallPosition(selectedPlayerBall) + "," + circle.getId().substring(8));
        }
    }


    @FXML
    public void alterBallsPlayers(String player, String color) {
        Color colorSelected = null;
        switch (color) {
            case "blackColor":
                colorSelected = Color.BLACK;
                break;
            case "redColor":
                colorSelected = Color.RED;
                break;
            case "greenColor":
                colorSelected = Color.GREEN;
                break;
            case "yellowColor":
                colorSelected = Color.YELLOW;
                break;
            case "blueColor":
                colorSelected = Color.BLUE;
                break;
        }
        if (player.equals("another")) {
            anotherBall1.setFill(colorSelected);
            anotherBall2.setFill(colorSelected);
            anotherBall3.setFill(colorSelected);
        } else if (player.equals("yourself")) {
            playerBall1.setFill(colorSelected);
            playerBall2.setFill(colorSelected);
            playerBall3.setFill(colorSelected);
        }
    }


    @FXML
    public void chatMessageSend() throws IOException {
        //Creating a Label
        Label label = new Label(this.nameText.getText());
        //Setting font to the label
        label.getStyleClass().add("namePlayerLabel");
        label.setLayoutX(100);
        label.setLayoutY(100);
        messageBox.getChildren().add(label);

        //Creating a Label
        Label label1 = new Label(messageField.getText());
        label1.getStyleClass().add("messagePlayerLabel");
        //Setting the position
        label1.setLayoutX(100);
        label1.setLayoutY(100);
        messageBox.getChildren().add(label1);
        messageBox.getChildren().add(new Label("\n"));


        PrintStream saida = new PrintStream(client.getOutputStream());
        saida.println("chatMessage:" + messageField.getText());
        messageField.clear();
    }

    @FXML
    private void askDraw() throws IOException {
        PrintStream saida = new PrintStream(client.getOutputStream());
        saida.println("drawGame:YES");
    }

    @FXML
    private void goToMenu() throws IOException {
        this.switchBetweenScreen(anchorPane.getScene(), "menu-view.fxml");
    }

    @FXML
    private void translateTransitionBall(Circle circle, double x, double y) {
        TranslateTransition translateTransition = new TranslateTransition();

        //Setting the duration of the transition
        translateTransition.setDuration(Duration.millis(1000));

        //Setting the node for the transition
        translateTransition.setNode(circle);

        //Setting the value of the transition along the x axis.
        translateTransition.setToX(x - circle.getLayoutX());
        translateTransition.setToY(y - circle.getLayoutY());

        //Playing the animation
        translateTransition.play();
    }

    @FXML
    public void handleResponse(String message) throws IOException {
        if (message.startsWith("getName:OK")) {
            nameText.setText(message.substring(11));
        }
        if (message.startsWith("startRandomMatch:OK")) {
            if (message.split(",").length > 1) {
                if (message.split(",")[1].equals("wait")) {
                    loadingLabel.setText("Waiting for another player...");
                } else {
                    board.getChildren().add(chooseColorPanel);
                    chooseColorPanel.setLayoutX(50);
                    chooseColorPanel.setLayoutY(100);
                    chooseColorPanel.setOpacity(1);
                    loadingLabel.setText("");
                }
            }
        }

        if (message.startsWith("startChooseMatch:OK,start")) {
            board.getChildren().add(chooseColorPanel);
            chooseColorPanel.setLayoutX(50);
            chooseColorPanel.setLayoutY(100);
            chooseColorPanel.setOpacity(1);
            loadingLabel.setText("");
        }

        if (message.startsWith("startNewMatch:OK")) {
            loadingLabel.setText("Waiting for another player...");
        }
        if (message.startsWith("chooseColor:OK")) {
            if (message.startsWith("chooseColor:OK,another,")) {
                switch (message.substring(23)) {
                    case "blackColor":
                        blackColor.getStyleClass().remove("colorBalls");
                        blackColor.getStyleClass().add("colorBallsBlocked");
                        break;
                    case "redColor":
                        redColor.getStyleClass().remove("colorBalls");
                        redColor.getStyleClass().add("colorBallsBlocked");
                        break;
                    case "greenColor":
                        greenColor.getStyleClass().remove("colorBalls");
                        greenColor.getStyleClass().add("colorBallsBlocked");
                        break;
                    case "yellowColor":
                        yellowColor.getStyleClass().remove("colorBalls");
                        yellowColor.getStyleClass().add("colorBallsBlocked");
                        break;
                    case "blueColor":
                        blueColor.getStyleClass().remove("colorBalls");
                        blueColor.getStyleClass().add("colorBallsBlocked");
                        break;
                }
                alterBallsPlayers("another", message.substring(23));
            } else if (message.startsWith("chooseColor:OK,yourself")) {
                board.getChildren().remove(chooseColorPanel);
                chooseColorPanel.setLayoutX(-500);
                chooseColorPanel.setLayoutY(-1000);
                chooseColorPanel.setOpacity(0);
                board.getChildren().add(choosePlayer);
                choosePlayer.setLayoutX(50);
                choosePlayer.setLayoutY(100);
                choosePlayer.setOpacity(1);

                String playerColor = message.substring(24);
                alterBallsPlayers("yourself", playerColor);
                loadingLabel.setText("");
            }
        }
        if (message.startsWith("choosePlayer:OK")) {
            this.playerTurn = message.substring(16);
            loadingLabel.setText("Waiting another player choose...");
        }
        if (message.startsWith("choosePlayer:ERROR")) {
            this.playerTurn = message.substring(19);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Choose Player");
            alert.setContentText("Your rival choose this player first.");
            alert.show();
        }
        if (message.startsWith("begin:OK")) {
            board.getChildren().remove(choosePlayer);
            choosePlayer.setLayoutX(-500);
            choosePlayer.setLayoutY(-1000);
            choosePlayer.setOpacity(0);
            board.getChildren().add(gamePanel);
            gamePanel.setLayoutX(20);
            gamePanel.setLayoutY(20);
            gamePanel.setOpacity(1);
            playerBallsPanel.getChildren().remove(playerBall1);
            playerBallsPanel.getChildren().remove(playerBall2);
            playerBallsPanel.getChildren().remove(playerBall3);
            gamePanel.getChildren().add(playerBall1);
            gamePanel.getChildren().add(playerBall2);
            gamePanel.getChildren().add(playerBall3);
            playerBall1.setLayoutX(650);
            playerBall1.setLayoutY(20);
            playerBall2.setLayoutX(650);
            playerBall2.setLayoutY(80);
            playerBall3.setLayoutX(650);
            playerBall3.setLayoutY(140);
            draggableMaker.makeDraggable(playerBall1);
            draggableMaker.makeDraggable(playerBall2);
            draggableMaker.makeDraggable(playerBall3);

            gamePanel.getChildren().add(anotherBall1);
            gamePanel.getChildren().add(anotherBall2);
            gamePanel.getChildren().add(anotherBall3);
            anotherBall1.setLayoutX(650);
            anotherBall1.setLayoutY(260);
            anotherBall2.setLayoutX(650);
            anotherBall2.setLayoutY(320);
            anotherBall3.setLayoutX(650);
            anotherBall3.setLayoutY(380);

            loadingLabel.setText("");
            sendButton.setDisable(false);
            buttonDraw.setDisable(false);
        }
        if (message.startsWith("turn:OK")) {
            this.actualPlayTurn = message.substring(8);
            if (this.actualPlayTurn.equals(this.playerTurn)) {
                if (playerBall1.getParent() != gamePanel || (playerBall2.getParent() == gamePanel && playerBall3.getParent() == gamePanel))
                    playerBall1.getStyleClass().remove("gameBallsDisabled");
                if (playerBall2.getParent() != gamePanel || (playerBall1.getParent() == gamePanel && playerBall3.getParent() == gamePanel))
                    playerBall2.getStyleClass().remove("gameBallsDisabled");
                if (playerBall3.getParent() != gamePanel || (playerBall1.getParent() == gamePanel && playerBall2.getParent() == gamePanel))
                    playerBall3.getStyleClass().remove("gameBallsDisabled");
                turnLabel.setText("Your turn");
            } else {
                playerBall1.getStyleClass().add("gameBallsDisabled");
                playerBall2.getStyleClass().add("gameBallsDisabled");
                playerBall3.getStyleClass().add("gameBallsDisabled");
                turnLabel.setText(message.substring(8));
                turnLabel.setText("Waiting another player move...");
            }
        }
        if (message.startsWith("move:OK")) {
            this.ignoreClick = false;
            String playerString = message.split(":")[1].split(",")[1];
            int older = Integer.parseInt(message.split(":")[1].split(",")[2]);
            int newer = Integer.parseInt(message.split(":")[1].split(",")[3]);
            if (playerString.equals(this.playerTurn)) {
                selectedPlayerBall.getStyleClass().remove("gameBallsSelected");
                selectedPlayerBall.getStyleClass().add("gameBalls");
                removeActiveBalls();

                if (selectedPlayerBall == playerBall1) {
                    playerBall1Position = newer;
                }

                if (selectedPlayerBall == playerBall2) {
                    playerBall2Position = newer;
                }

                if (selectedPlayerBall == playerBall3) {
                    playerBall3Position = newer;
                }

                switch (newer) {
                    case 0:
                        translateTransitionBall(selectedPlayerBall, gameBall0.getLayoutX(), gameBall0.getLayoutY());
                        break;
                    case 1:
                        translateTransitionBall(selectedPlayerBall, gameBall1.getLayoutX(), gameBall1.getLayoutY());
                        break;
                    case 2:
                        translateTransitionBall(selectedPlayerBall, gameBall2.getLayoutX(), gameBall2.getLayoutY());
                        break;
                    case 3:
                        translateTransitionBall(selectedPlayerBall, gameBall3.getLayoutX(), gameBall3.getLayoutY());
                        break;
                    case 4:
                        translateTransitionBall(selectedPlayerBall, gameBall4.getLayoutX(), gameBall4.getLayoutY());
                        break;
                    case 5:
                        translateTransitionBall(selectedPlayerBall, gameBall5.getLayoutX(), gameBall5.getLayoutY());
                        break;
                    case 6:
                        translateTransitionBall(selectedPlayerBall, gameBall6.getLayoutX(), gameBall6.getLayoutY());
                        break;
                }

                this.selectedPlayerBall = null;
                this.removeActiveBalls();
            } else {
                Circle selected;
                if (older == -1) {
                    if (anotherBall1Position == -1) {
                        selected = anotherBall1;
                        anotherBall1Position = newer;
                    }
                    else if (anotherBall2Position == -1) {
                        selected = anotherBall2;
                        anotherBall2Position = newer;
                    }
                    else {
                        selected = anotherBall3;
                    }
                } else {
                    if (older == anotherBall1Position) {
                        selected = anotherBall1;
                        anotherBall1Position = newer;
                    }
                    else if (older == anotherBall2Position) {
                        selected = anotherBall2;
                        anotherBall2Position = newer;
                    }
                    else {
                        selected = anotherBall3;
                    }
                }

                switch (newer) {
                    case 0:
                        translateTransitionBall(selected, gameBall0.getLayoutX(), gameBall0.getLayoutY());
                        break;
                    case 1:
                        translateTransitionBall(selected, gameBall1.getLayoutX(), gameBall1.getLayoutY());
                        break;
                    case 2:
                        translateTransitionBall(selected, gameBall2.getLayoutX(), gameBall2.getLayoutY());
                        break;
                    case 3:
                        translateTransitionBall(selected, gameBall3.getLayoutX(), gameBall3.getLayoutY());
                        break;
                    case 4:
                        translateTransitionBall(selected, gameBall4.getLayoutX(), gameBall4.getLayoutY());
                        break;
                    case 5:
                        translateTransitionBall(selected, gameBall5.getLayoutX(), gameBall5.getLayoutY());
                        break;
                    case 6:
                        translateTransitionBall(selected, gameBall6.getLayoutX(), gameBall6.getLayoutY());
                        break;
                }
            }
        }
        if (message.startsWith("move:ERROR")) {
            this.ignoreClick = false;
            this.selectedPlayerBall.setTranslateX(previousPositionX);
            this.selectedPlayerBall.setTranslateY(previousPositionY);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Choose Player");
            alert.setContentText(message.substring(11));
            alert.show();
        }

        if (message.startsWith("cancelGame:OK")) {
            this.goToMenu();
        }

        if (message.startsWith("endGame:OK")) {
            if (message.equals("endGame:OK,giveUp")) {
                this.goToMenu();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("END GAME");
            alert.setContentText(message.substring(11).toUpperCase());
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isEmpty() || result.get() == ButtonType.OK) {
                this.goToMenu();
            }
        }

        if (message.startsWith("chatMessage:OK")) {
            String player = message.split(":")[1].split(",")[1];
            String remoteMessage = message.split(":")[1].split(",")[2];

            HBox hBox = new HBox();
            hBox.setMinWidth(290);
            hBox.setMaxWidth(290);
            //Creating a Label
            Label label = new Label(player);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            //Setting font to the label
            label.getStyleClass().add("namePlayerLabel");
            hBox.getChildren().add(label);
            messageBox.getChildren().add(hBox);


            HBox hBox1 = new HBox();
            hBox1.setMinWidth(290);
            hBox1.setMaxWidth(290);
            //Creating a Label
            Label label1 = new Label(remoteMessage);
            label1.getStyleClass().add("messageAnotherLabel");
            //Setting the position
            hBox1.getChildren().add(label1);
            hBox1.setAlignment(Pos.CENTER_RIGHT);
            messageBox.getChildren().add(hBox1);
            messageBox.getChildren().add(new Label("\n"));
            messageField.clear();
        }

        if (message.startsWith("drawGame:")) {
            if (message.startsWith("drawGame:OK,draw")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("END GAME");
                alert.setContentText(message.substring(12).toUpperCase());
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isEmpty() || result.get() == ButtonType.OK) {
                    this.goToMenu();
                }
            }
            if (message.startsWith("drawGame:OK,ask")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("ASK FOR DRAW");
                alert.setContentText("Do you want to draw the game?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    PrintStream saida = new PrintStream(client.getOutputStream());
                    saida.println("drawGame:YES");
                } else {
                    PrintStream saida = new PrintStream(client.getOutputStream());
                    saida.println("drawGame:NO");
                }
            }
            if (message.startsWith("drawGame:OK,refused")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("DRAW GAME");
                alert.setContentText("Your opponent refused to draw the game");
                alert.show();
            }
        }
    }

}
