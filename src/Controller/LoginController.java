package Controller;

import Model.Connection;
import Model.Dictionary;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {

    private static LoginController loginController;
    public StackPane stackPane;

    public static LoginController getInstance() {
        return loginController;
    }

    @FXML
    GridPane gridPane;
    @FXML
    Button exitButton;
    @FXML
    Button loginBtn;
    @FXML
    Button signUpBtn;
    @FXML
    TextField userField;
    @FXML
    TextField passField;

    public void initialize() {
        loginController = this;

    }

    public void handleOnKeyPressedExit(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.exitGameBtn();
        } else if (event.getCode().equals(KeyCode.DOWN)) {
            userField.requestFocus();
        }
    }

    public void handleOnKeyPressedLogin(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.loginBtnAct();
        } else if (event.getCode().equals(KeyCode.UP)) {
            passField.requestFocus();
        }

    }

    public void handleOnKeyPressedSignUp(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.signUpBtnAct();
        }

    }

    public void handleOnKeyPressedPass(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DOWN)) {
            loginBtn.requestFocus();
        } else if (event.getCode().equals(KeyCode.UP)) {
            userField.requestFocus();
        }
    }

    public void handleOnKeyPressedUser(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DOWN)) {
            passField.requestFocus();
        } else if (event.getCode().equals(KeyCode.UP)) {
            exitButton.requestFocus();
        }
    }


    public void exitGameBtn() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void loginBtnAct() {
        userField.getStyleClass().remove("wrongPassword");
        passField.getStyleClass().remove("wrongPassword");
        if (!userField.getText().equals("") && !userField.getText().contains(" ") && !passField.getText().equals("")) {
            Dictionary dictionary = Dictionary.loginDict(userField.getText(), passField.getText());
            Connection.getConnectionToServer().send(dictionary);
        } else {
            if (userField.getText().equals("") || userField.getText().contains(" ")) {
                wrongUserStyle();
            }
            if (passField.getText().equals("")) {
                wrongPassStyle();
            }
        }
    }

    public void wrongPassStyle() {
        passField.getStyleClass().add("wrongPassword");
    }

    public void wrongUserStyle() {
        userField.getStyleClass().add("wrongPassword");
    }

    public void loginBtnActFocus() {
        loginBtn.requestFocus();
    }

    public void signUpBtnActFocus() {
        signUpBtn.requestFocus();
    }

    public void exitBtnActFocus() {
        exitButton.requestFocus();
    }


    public void signUpBtnAct() {
        passField.getStyleClass().remove("wrongPassword");
        userField.getStyleClass().remove("wrongPassword");
        if (!userField.getText().equals("") && !passField.getText().equals("")) {
            Dictionary dictionary = Dictionary.registerDict(userField.getText(), passField.getText());
            Connection.getConnectionToServer().send(dictionary);
        } else {
            if (userField.getText().equals("")) {
                wrongUserStyle();
            }
            if (passField.getText().equals("")) {
                wrongPassStyle();
            }
        }
    }

    public void showDialog(String message) {
        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            JFXButton jfxButton = new JFXButton("OK");
            jfxDialogLayout.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.3);");
            JFXDialog jfxDialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            jfxButton.getStyleClass().add("dialog-button");
            jfxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        jfxDialog.close();
                    }
            );
            jfxDialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
                gridPane.setEffect(null);
            });
            Label label = new Label(message);
            label.setStyle("-fx-font-size: 20px; -fx-text-fill: black");
            VBox vBox = new VBox();
            vBox.getChildren().addAll(label);
            jfxDialogLayout.getBody().add(vBox);
            jfxDialogLayout.setActions(jfxButton);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });


    }
}


