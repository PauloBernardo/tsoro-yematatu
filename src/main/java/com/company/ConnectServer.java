package com.company;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class ConnectServer extends ResizableView implements ContextListening {

    @FXML
    public Label connectionStatus;
    @FXML
    public Pane backContainer;
    @FXML
    public TextField nameField;
    @FXML
    public AnchorPane anchorPane;

    private TsoroYematatuServerInterface server;

    ResourceBundle bundle = ResourceBundle.getBundle("com.company.i18n", new Locale("pt_br", "pt_BR"));

    @FXML
    public void initialize() {
        initCliente();
    }

    private void initCliente(){
        try {
            Context context = Context.getInstance();
            server = context.getServer();
            context.addListening(this);
            connectionStatus.setText(bundle.getString("connectedOK"));

        } catch (IOException ex) {
            connectionStatus.setText(bundle.getString("connectError"));
        }
    }

    @FXML
    public void handleResponse(String message) {

    }

    @FXML
    public void switchToHelloView(MouseEvent event) throws IOException {
        this.switchBetweenScreen(((Node) event.getSource()).getScene(), "hello-view.fxml");
    }

    @FXML
    public void onNextClick(ActionEvent event) {
        if (connectionStatus.getText().equals(bundle.getString("connectedOK"))) {
            try {
                ((Button)event.getSource()).setText(bundle.getString("loadingLabel"));
                server.setName(nameField.getText());
                this.switchBetweenScreen(anchorPane.getScene(), "menu-view.fxml");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("connect.errorTitle"));
                alert.setContentText(bundle.getString("connect.errorText"));
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("connect.errorTitle"));
            alert.setContentText(bundle.getString("connect.errorText"));
            alert.show();
        }
    }
}
