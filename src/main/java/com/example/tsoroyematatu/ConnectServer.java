package com.example.tsoroyematatu;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;

public class ConnectServer {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public TextField nameField;

    private Stage stage;
    private Scene scene;
    private Parent rootMain;
    private Socket cliente;


    @FXML
    public void initialize() {
        initCliente();
    }

    private void initCliente(){
        try {
            cliente = new Socket("127.0.0.1",3322);
            connectionStatus.setText("Connection established!");

            Task<Integer> threadOne = new Task<Integer>(){
                @Override
                protected Integer call() throws Exception{

                    DataInputStream istream = new DataInputStream(cliente.getInputStream());
                    while (true) {
                        String MRcv = istream.readUTF();
                        System.out.println("Remoto: "+ MRcv);
                        Platform.runLater(() ->  {
                            connectionStatus.setText(MRcv);
                        });
                    }
                }
            };

            Thread th = new Thread(threadOne);
            th.setDaemon(true);
            th.start();
        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    @FXML
    public void switchToConnectServer(MouseEvent event) throws IOException {
        Parent rootMain = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(rootMain);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onNextClick() throws IOException {
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        saida.println("setName:" + nameField.getText());
    }

    @FXML
    public void onCheckClick() throws IOException {
        PrintStream saida = new PrintStream(cliente.getOutputStream());
        saida.println("getName:" + nameField.getText());
    }

}
