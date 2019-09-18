package views;

import java.io.IOException;
import java.util.*;

import controllers.EmailCtrl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.*;
import models.Email;

public class ForwardController {

    EmailCtrl ctrl;

    @FXML
    private Label sender;

    @FXML
    private Label subject;

    @FXML
    private Button forward;

    @FXML
    private TextArea receivers;

    Email email;

    public void setEmail(Email selected) {
        email = selected;
        sender.setText(email.getSender());
        subject.setText(email.getSubject());
    }

    @FXML
    private void initialize() {
        ctrl = EmailCtrl.getInstance();
    }

    @FXML
    private void _forward() {
        String recString = receivers.getText().trim();
        Set<String> rec = new HashSet<>(Arrays.asList(recString.split("\n")));
        rec.remove("");
        if (rec.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Set receivers!", ButtonType.OK).show();
            return;
        }
        hide();
        boolean b = ctrl.forward(email, rec);
        new Alert(b ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR, b ? "Forwarded!" : "Failed", ButtonType.OK).show();
        close();
    }

    private void close() {
        Stage stage = (Stage) receivers.getScene().getWindow();
        stage.close();
    }

    private void hide() {
        Stage stage = (Stage) receivers.getScene().getWindow();
        stage.hide();
    }

}
