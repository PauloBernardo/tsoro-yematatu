package com.example.tsoroyematatu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;

public class GameView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
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
    private int anotherBall3Position = -1;

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


    private Stage stage;
    private Scene scene;
    private Socket client;
    private String type;
    private String playerTurn;
    private String playerColor;
    private String actualPlayTurn;

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
                PrintStream saida = null;
                try {
                    saida = new PrintStream(client.getOutputStream());
                    if (this.type.equals("new")) {
                        saida.println("startNewMatch:");
                    } else if (this.type.equals("random")) {
                        saida.println("startRandomMatch:");
                    }
                    saida.println("getName:");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            loadingLabel.setText("Loading...");
        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    @FXML
    public void switchToConnectServer(MouseEvent event) throws IOException {
        Context.getInstance().removeListening(this);
        Parent rootMain = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void selectColor(MouseEvent event) throws IOException {
        PrintStream saida = new PrintStream(client.getOutputStream());
        System.out.println("Send color: " + ((Circle)event.getSource()).getId());
        saida.println("chooseColor:" + ((Circle)event.getSource()).getId());
    }

    @FXML
    public void selectPlayer(MouseEvent event) throws IOException {
        PrintStream saida = new PrintStream(client.getOutputStream());
        if (((Circle)event.getSource()).getId().equals("player1Ball")) {
            if (((Circle)event.getSource()).getStyleClass().toString().equals("playerBalls")) {
                player1Ball.getStyleClass().remove("playerBalls");
                player1Ball.getStyleClass().add("playerBallsSelected");
            } else {
                player1Ball.getStyleClass().remove("playerBallsSelected");
                player1Ball.getStyleClass().add("playerBalls");
            }
            saida.println("choosePlayer:player1");
        } else {
            System.out.println(((Circle)event.getSource()).getStyleClass());
            if (((Circle)event.getSource()).getStyleClass().toString().equals("playerBalls")) {
                player2Ball.getStyleClass().remove("playerBalls");
                player2Ball.getStyleClass().add("playerBallsSelected");
            } else {
                player2Ball.getStyleClass().remove("playerBallsSelected");
                player2Ball.getStyleClass().add("playerBalls");
            }
            saida.println("choosePlayer:player2");
        }
    }

    public void selectBoardBall(MouseEvent event) throws IOException {
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
        } else {
            circle.getStyleClass().remove("gameBallsSelected");
            circle.getStyleClass().add("gameBalls");
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
        gameBall0.getStyleClass().remove("emptyBoardBall");
        gameBall1.getStyleClass().remove("emptyBoardBall");
        gameBall2.getStyleClass().remove("emptyBoardBall");
        gameBall3.getStyleClass().remove("emptyBoardBall");
        gameBall4.getStyleClass().remove("emptyBoardBall");
        gameBall5.getStyleClass().remove("emptyBoardBall");
        gameBall6.getStyleClass().remove("emptyBoardBall");
    }

    @FXML
    public void selectGameBall(MouseEvent event) throws IOException {
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
    public void handleClickBoardBall(MouseEvent event) throws IOException {
        Circle circle = ((Circle)event.getSource());
        PrintStream saida = new PrintStream(client.getOutputStream());
        if (circle.getStyleClass().toString().equals("emptyBoardBall") && selectedPlayerBall != null) {
            switch (selectedPlayerBall.getId()) {
                case "playerBall1":
                    saida.println("move:" + playerBall1Position + "," + circle.getId().substring(8));
                    break;
                case "playerBall2":
                    saida.println("move:" + playerBall2Position + "," + circle.getId().substring(8));
                    break;
                case "playerBall3":
                    saida.println("move:" + playerBall3Position + "," + circle.getId().substring(8));
                    break;
            }
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

                playerColor = message.substring(24);
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
            board.getChildren().add(playerBallsPanel);
            playerBallsPanel.setLayoutX(550);
            playerBallsPanel.setLayoutY(20);
            playerBallsPanel.setOpacity(1);
            loadingLabel.setText("");
        }
        if (message.startsWith("turn:OK")) {
            this.actualPlayTurn = message.substring(8);
            turnLabel.setText(message.substring(8));
            if (this.actualPlayTurn.equals(this.playerTurn)) {
                playerBall1.getStyleClass().remove("gameBallsDisabled");
                playerBall2.getStyleClass().remove("gameBallsDisabled");
                playerBall3.getStyleClass().remove("gameBallsDisabled");
            } else {
                playerBall1.getStyleClass().add("gameBallsDisabled");
                playerBall2.getStyleClass().add("gameBallsDisabled");
                playerBall3.getStyleClass().add("gameBallsDisabled");
            }
        }
        if (message.startsWith("move:OK")) {
            String playerString = message.split(":")[1].split(",")[1];
            int older = Integer.parseInt(message.split(":")[1].split(",")[2]);
            int newer = Integer.parseInt(message.split(":")[1].split(",")[3]);
            if (playerString.equals(this.playerTurn)) {
                selectedPlayerBall.getStyleClass().remove("gameBallsSelected");
                selectedPlayerBall.getStyleClass().add("gameBalls");
                removeActiveBalls();

                if (older == -1) {
                    playerBallsPanel.getChildren().remove(selectedPlayerBall);
                    gamePanel.getChildren().add(selectedPlayerBall);
                }

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
                        selectedPlayerBall.setLayoutX(gameBall0.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall0.getLayoutY());
                        break;
                    case 1:
                        selectedPlayerBall.setLayoutX(gameBall1.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall1.getLayoutY());
                        break;
                    case 2:
                        selectedPlayerBall.setLayoutX(gameBall2.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall2.getLayoutY());
                        break;
                    case 3:
                        selectedPlayerBall.setLayoutX(gameBall3.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall3.getLayoutY());
                        break;
                    case 4:
                        selectedPlayerBall.setLayoutX(gameBall4.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall4.getLayoutY());
                        break;
                    case 5:
                        selectedPlayerBall.setLayoutX(gameBall5.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall5.getLayoutY());
                        break;
                    case 6:
                        selectedPlayerBall.setLayoutX(gameBall6.getLayoutX());
                        selectedPlayerBall.setLayoutY(gameBall6.getLayoutY());
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
                        anotherBall3Position = newer;
                    }
                    anotherBallsPanel1.getChildren().remove(selected);
                    gamePanel.getChildren().add(selected);
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
                        anotherBall3Position = newer;
                    }
                }

                switch (newer) {
                    case 0:
                        selected.setLayoutX(gameBall0.getLayoutX());
                        selected.setLayoutY(gameBall0.getLayoutY());
                        break;
                    case 1:
                        selected.setLayoutX(gameBall1.getLayoutX());
                        selected.setLayoutY(gameBall1.getLayoutY());
                        break;
                    case 2:
                        selected.setLayoutX(gameBall2.getLayoutX());
                        selected.setLayoutY(gameBall2.getLayoutY());
                        break;
                    case 3:
                        selected.setLayoutX(gameBall3.getLayoutX());
                        selected.setLayoutY(gameBall3.getLayoutY());
                        break;
                    case 4:
                        selected.setLayoutX(gameBall4.getLayoutX());
                        selected.setLayoutY(gameBall4.getLayoutY());
                        break;
                    case 5:
                        selected.setLayoutX(gameBall5.getLayoutX());
                        selected.setLayoutY(gameBall5.getLayoutY());
                        break;
                    case 6:
                        selected.setLayoutX(gameBall6.getLayoutX());
                        selected.setLayoutY(gameBall6.getLayoutY());
                        break;
                }
            }
        }
        if (message.startsWith("move:ERROR")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Choose Player");
            alert.setContentText(message.substring(11));
            alert.show();
        }
        if (message.startsWith("endGame:OK")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("END GAME");
            alert.setContentText(message.substring(11).toUpperCase());
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isEmpty() || result.get() == ButtonType.OK) {
                Context.getInstance().removeListening(this);
                FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("menu-view.fxml")));
                Parent rootMain = (Parent)fxmlLoader.load();
                stage = (Stage)anchorPane.getScene().getWindow();
                scene = new Scene(rootMain);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

}
