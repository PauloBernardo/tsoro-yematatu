package com.example.tsoroyematatu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ConnectServer extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public TextField nameField;
    @FXML
    public AnchorPane anchorPane;

    private Socket cliente;


    @FXML
    public void initialize() {
        initCliente();
    }

    private void initCliente(){
        try {
            Context context = Context.getInstance();
            cliente = context.getClient();
            context.addListening(this);
            connectionStatus.setText("Connection established!");

        } catch (IOException ex) {
            connectionStatus.setText("Connection not established!");
        }
    }

    @FXML
    public void handleResponse(String message) throws Exception {
        if (message.startsWith("setName:OK")) {
            this.switchBetweenScreen(anchorPane.getScene(), "menu-view.fxml");
        }
    }

    @FXML
    public void switchToHelloView(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "hello-view.fxml");
    }

    @FXML
    public void onNextClick(ActionEvent event) throws IOException {
        if (connectionStatus.getText().equals("Connection established!") && cliente.isConnected()) {
            PrintStream saida = new PrintStream(cliente.getOutputStream());
            saida.println("setName:" + nameField.getText());
            ((Button)event.getSource()).setText("Loading...");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Connection");
            alert.setContentText("There is a error on the connection if server. Please, be sure that server is on and try again.");
            alert.show();
        }
    }
}
