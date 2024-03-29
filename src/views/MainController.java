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
import javafx.scene.control.Alert.AlertType;
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
    private Button reply;

    @FXML
    private Button replyAll;

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
        reply.setDisable(true);
        replyAll.setDisable(true);
        inbox.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        inbox.setItems(FXCollections.observableList(account.getInbox()));
        inbox.getSelectionModel().selectedIndexProperty().addListener((observable) -> {
            SelectionModel<Email> selection = inbox.getSelectionModel();
            if (!selection.isEmpty()) {
                selected = selection.getSelectedItem();
                delete.setDisable(false);
                forward.setDisable(false);
                reply.setDisable(false);
                replyAll.setDisable(false);
                showEmail(selected);
            } else {
                selected = null;
                delete.setDisable(true);
                forward.setDisable(true);
                reply.setDisable(true);
                replyAll.setDisable(true);
                hideEmail();
            }
        });
    }

    private void showEmail(Email e) {
        sender.setText(e.getSender());
        receivers.setText(String.join(", ", e.getReceivers()));
        subject.setText(e.getSubject());
        body.setText(e.getBody());
    }

    private void hideEmail()  {
        sender.setText("");
        receivers.setText("");
        subject.setText("");
        body.setText("");

    }

    @FXML
    private void _toggleConnection() {
        if (!ctrl.isSocketConnected()) { // connect
            if (!ctrl.connect()) {
                connect.setText("Connect");
                new Alert(AlertType.ERROR, "Error connecting", ButtonType.OK).show();
            } else {
                connect.setText("Disconnect");
                new Alert(AlertType.INFORMATION, "Connected", ButtonType.OK).show();
            }
        } else { // disconnect
            ctrl.disconnect();
            connect.setText("Connect");
        }
    }

    @FXML
    private void _create() {
        openCreateWindow(null, null, null, "New email");
    }

    @FXML
    private void _delete() {
        if (!ctrl.delete(selected)) {
            new Alert(AlertType.ERROR, "Error deleting email", ButtonType.OK).show();
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
    private void _reply() {
        Set<String> rec = new HashSet<>();
        rec.add(selected.getSender());
        openCreateWindow(rec, selected.getSubject(), null, "Reply");
    }

    @FXML
    private void _replyAll() {
        Set<String> rec = new HashSet<>(selected.getReceivers());
        rec.remove(ctrl.getUser());
        rec.add(selected.getSender());
        openCreateWindow(rec, selected.getSubject(), null, "Reply all");
    }

    @FXML
    private void _clearDb() {
        ctrl.clearDb();
    }

    @Override
    public void update(Observable o, Object arg) { // email account
        Platform.runLater(() -> {
            inbox.setItems(FXCollections.observableList(ctrl.getAccount().getInbox()));
            if (arg != null) { // arg is the number of emails received
                new Alert(Alert.AlertType.INFORMATION, "Received " + arg + " new emails!", ButtonType.OK).show();
            }
        });
    }

    private void openCreateWindow(Set<String> receivers, String subject, String body, String title) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        CreateController cc = fxmlLoader.getController();
        cc.setFields(receivers, subject, body);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.showAndWait();
    }

}