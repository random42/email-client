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
        Set<String> rec = new HashSet<>(Arrays.asList(receivers.getText().split("\n")));
        ctrl.forward(email, rec);
    }


}
