package views;

import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.*;

public class ModalController {

    public static synchronized void modal(String msg, boolean error) {
        FXMLLoader fxmlLoader = new FXMLLoader(ModalController.class.getResource("modal.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ModalController mc = fxmlLoader.getController();
        mc.setText(msg);
        String title = error ? "Error" : "Notification";
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private Label msg;


    public void setText(String s) {
        msg.setText(s);
    }

    @FXML
    private void _close() {
        Stage stage = (Stage) msg.getScene().getWindow();
        stage.close();
    }

}
