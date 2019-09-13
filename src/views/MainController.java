package views;

import controllers.EmailCtrl;

import javafx.event.EventHandler;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import models.*;

public class MainController implements Observer {

    EmailCtrl ctrl;

    Email selected;

    @FXML
    private Button connect;

    @FXML
    private Button create;

    @FXML
    private Button delete;

    @FXML
    private Button forward;

    @FXML
    private ListView<Email> inbox;

    @FXML
    private Text sender;

    @FXML
    private Text receivers;

    @FXML
    private Text subject;

    @FXML
    private TextArea body;

    @FXML
    private Label user;

    @FXML
    private void initialize() {
        ctrl = EmailCtrl.getInstance();
        EmailAccount account = ctrl.getAccount();
        account.addObserver(this);
        user.setText(ctrl.getUser());
        create.setDisable(false);
        delete.setDisable(true);
        forward.setDisable(true);
        inbox.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        inbox.setItems(FXCollections.observableList(account.getInbox()));
        inbox.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            SelectionModel<Email> selection = inbox.getSelectionModel();
            if (!selection.isEmpty()) {
                selected = selection.getSelectedItem();
                delete.setDisable(false);
                forward.setDisable(false);
                showSelectedEmail();
            } else {
                selected = null;
                delete.setDisable(true);
                forward.setDisable(true);
            }
        });
    }

    private void showSelectedEmail() {
        sender.setText(selected.getSender());
        receivers.setText(String.join(", ", selected.getReceivers()));
        subject.setText(selected.getSubject());
        body.setText(selected.getBody());
    }

    @FXML
    private void _toggleConnection() {
        if (!ctrl.isSocketConnected()) { // connect
            if (!ctrl.connect()) {
                ModalController.modal("Error connecting", true);
            }
            connect.setText("Disconnect");
        } else { // disconnect
            ctrl.disconnect();
            connect.setText("Connect");
        }
    }

    @FXML
    private void _create() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("New email");
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void _delete() {
        if (!ctrl.delete(selected)) {
            ModalController.modal("Error deleting email", true);
        }
    }

    @FXML
    private void _forward() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("forward.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ForwardController fc = fxmlLoader.getController();
        fc.setEmail(selected);
        Stage stage = new Stage();
        stage.setTitle("Forward");
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void _clearDb() {
        ctrl.clearDb();
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            public void run() {
                inbox.setItems(FXCollections.observableList(ctrl.getAccount().getInbox()));
                if (arg != null) { // arg is the number of emails received
                    ModalController.modal("Received " + arg + " new emails!", false);
                }
            }
        });
    }

}