package com.company;

import javafx.beans.property.SimpleStringProperty;

public class Game {
    private final SimpleStringProperty game;
    private final SimpleStringProperty result;
    private final SimpleStringProperty player;
    private final SimpleStringProperty id;


    public Game(String game, String result, String player, String id) {
        this.game = new SimpleStringProperty(game);
        this.result = new SimpleStringProperty(result);
        this.player = new SimpleStringProperty(player);
        this.id = new SimpleStringProperty(id);
    }

    public String getGame() {
        return game.get();
    }

    public SimpleStringProperty gameProperty() {
        return game;
    }

    public void setGame(String game) {
        this.game.set(game);
    }

    public String getResult() {
        return result.get();
    }

    public SimpleStringProperty resultProperty() {
        return result;
    }

    public String getPlayer() {
        return player.get();
    }

    public SimpleStringProperty playerProperty() {
        return player;
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setResult(String result) {
        this.result.set(result);
    }
}
