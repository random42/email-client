package views;

import controllers.EmailCtrl;

import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.*;

public class LoginController {

    EmailCtrl ctrl;

    @FXML
    private TextField username;

    @FXML
    private Button login;

    @FXML
    private void initialize() {
        ctrl = EmailCtrl.getInstance();
    }

    @FXML
    private void _login() {
        String user = username.getText().trim();
        if (user.equals("")) {
            new Alert(Alert.AlertType.ERROR, "Enter a username!", ButtonType.OK).show();
            return;
        }
        ctrl.setUser(user);
        close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        stage.setTitle("Email client");
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void close() {
        Stage stage = (Stage) username.getScene().getWindow();
        stage.close();
    }


}
