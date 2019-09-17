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

public class CreateController {

    EmailCtrl ctrl;

    @FXML
    private TextField receivers;

    @FXML
    private TextField subject;

    @FXML
    private TextArea body;

    @FXML
    private Button send;

    @FXML
    private void initialize() {
        ctrl = EmailCtrl.getInstance();
    }

    private void close() {
        Stage stage = (Stage) send.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void _send() {
        String recString = receivers.getText().trim();
        String sub = subject.getText().trim();
        String b = body.getText().trim();
        Set<String> rec = new HashSet<>(Arrays.asList(recString.split(",")));
        rec.remove("");
        if (rec.isEmpty() || sub.equals("")) {
            ModalController.modal("Check fields again!", true);
            return;
        }
        Email e = ctrl.createEmail(rec, sub, b);
        if (!ctrl.send(e)) {
            ModalController.modal("Error sending email", true);
        }
        close();
    }

    public void setFields(Set<String> rec, String sub, String _body) {
        if (rec != null)
            receivers.setText(String.join(",", rec));
        if (sub != null)
            subject.setText(sub);
        if (_body != null)
            body.setText(_body);
    }
}
