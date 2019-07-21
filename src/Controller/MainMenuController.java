package Controller;

import Model.Connection;
import Model.Dictionary;
import Model.RSA;
import View.View;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import com.google.gson.Gson;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

public class MainMenuController {
    private static MainMenuController mainMenuController;

    public static MainMenuController getInstance() {
        return mainMenuController;
    }

    public Button save;
    public StackPane stackPane;
    public GridPane gridPane;
    @FXML
    Label accountInfo;
    @FXML
    Button startGame;
    @FXML
    Button recordedMatch;
    @FXML
    Button shop;
    @FXML
    Button collection;
    @FXML
    Button logOut;

    @FXML
    public void initialize() {
        mainMenuController = this;
        accountInfo.setText(Dictionary.myUserName + "\n" + "ACC-NUM :" + Dictionary.myAccountNumber);
        accountInfo.setStyle("-fx-text-fill: #ffffff");
    }

    public void logOutBtnAct() {
        Gson gson = new Gson();
        Dictionary.myAccountNumber = "";
        Dictionary.myUserName = "";

        try {
            Connection.getConnectionToServer().outputStream.close();
            Connection.getConnectionToServer().inputStream.close();
            Connection.getConnectionToServer().socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        RSAPrivateKey pr = ((RSAPrivateKey) kp.getPrivate());
        Key pvt = kp.getPrivate();
        System.out.println("public key : " + pub.toString());
        Connection.n = RSA.getN(pub.toString());
        Connection.d = pr.getPrivateExponent();
        Connection.serverToClientRSA = new RSA(Connection.n, Connection.d); // for receiving from server
        try {
            Socket socket = new Socket("localhost", 55555);
        } catch (IOException e) {
            e.printStackTrace();
        }
        View.makeLoginScene();
    }


    public void shopAct() {

    }

    public void collectionAct() {

    }

    public void recordedMatchAct() {

    }

    public void saveBtnAct() {
    }


    public void logOutBtnActFocus() {
        logOut.requestFocus();
    }

    public void saveBtnActFocus() {
        save.requestFocus();
    }

    public void collectionBtnActFocus() {
        collection.requestFocus();
    }

    public void shopBtnActFocus() {
        shop.requestFocus();
    }

    public void recordedMatchBtnActFocus() {
        recordedMatch.requestFocus();
    }


    public void handleOnKeyPressedShop(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.shopAct();
        }

    }

    public void handleOnKeyPressedCollection(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.collectionAct();
        }

    }

    public void handleOnKeyPressedRecordedMatch(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.recordedMatchAct();
        }

    }

    public void handleOnKeyPressedSave(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.saveBtnAct();
        }
    }

    public void handleOnKeyPressedLogOut(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.logOutBtnAct();
        } else if (event.getCode().equals(KeyCode.DOWN)) {
            startGame.requestFocus();
        }
    }


    public void balanceBtnActFocus() {
        startGame.requestFocus();
    }

    public void handleOnKeyPressedBalance(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.balanceAct();
        } else if (event.getCode().equals(KeyCode.UP)) {
            logOut.requestFocus();
        }

    }

    public void balanceAct() {
        Connection.getConnectionToServer().send(Dictionary.balanceDict());

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
