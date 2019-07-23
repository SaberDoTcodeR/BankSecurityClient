package Controller;

import Model.Connection;
import Model.Dictionary;
import View.View;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuController {
    private static MainMenuController mainMenuController;
    public static boolean isAdmin = false;
    public GridPane gridDepWith;
    public GridPane gridBill;
    public GridPane gridChangePass;


    public static MainMenuController getInstance() {
        return mainMenuController;
    }


    public StackPane stackPane;
    public GridPane gridPane;
    @FXML
    Label accountInfo;
    @FXML
    Button balance;
    @FXML
    Button transfer;
    @FXML
    Button deposit;
    @FXML
    Button withdraw;
    @FXML
    Button logOut;
    @FXML
    Button makeBill;
    @FXML
    Button payBill;
    @FXML
    Button changePass;
    @FXML
    Button transactions;

    @FXML
    public void initialize() {
        mainMenuController = this;
        if (isAdmin)
            accountInfo.setText("ADMIN" + "\n");
        else
            accountInfo.setText(Dictionary.myUserName + "\n" + "ACC-NUM :" + Dictionary.myAccountNumber);
        accountInfo.setStyle("-fx-text-fill: #ffffff");
        if (isAdmin) {
            gridPane.getChildren().remove(logOut);
            gridPane.getChildren().removeAll(gridDepWith, gridBill,gridChangePass);
            gridChangePass.getChildren().remove(changePass);
            gridChangePass.add(logOut,0,0);
            gridPane.add(gridChangePass,0,2);
        }
    }

    public void logOutBtnAct() {
        isAdmin = false;
        Dictionary.myAccountNumber = "";
        Dictionary.myUserName = "";
        Connection.getConnectionToServer().send(Dictionary.logoutDict());
        View.makeLoginScene();
    }


    public void logOutBtnActFocus() {
        logOut.requestFocus();
    }


    public void handleOnKeyPressedLogOut(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.logOutBtnAct();
        }
    }

    ////////////////////////////////////
    public void transactionsBtnActFocus() {
        transactions.requestFocus();
    }


    public void handleOnKeyPressedTransactions(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.transactionsAct();
        }
    }

    public void transactionsAct() {
        if (isAdmin) {
            adminUserChooseDialog(Dictionary.transactionsDict());
        } else
            Connection.getConnectionToServer().send(Dictionary.transactionsDict());
    }

    public void changePassBtnAct() {
        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            JFXButton jfxButton = new JFXButton("OK");
            JFXTextField newPassWord = new JFXTextField();
            JFXTextField oldPassWord = new JFXTextField();
            oldPassWord.setPromptText("OLD PASSWORD...");
            newPassWord.setPromptText("NEW PASSWORD...");
            oldPassWord.setId("text");
            newPassWord.setId("text");
            jfxDialogLayout.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.3);");
            JFXDialog jfxDialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            jfxButton.getStyleClass().add("dialog-button");
            jfxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        if (!oldPassWord.getText().equals("") && !newPassWord.getText().equals("")) {
                            Connection.getConnectionToServer().send(Dictionary.changePass(oldPassWord.getText(), newPassWord.getText()));
                            jfxDialog.close();
                        } else if (oldPassWord.getText().equals("")) {
                            oldPassWord.setId("wrongPassword");
                        } else {
                            newPassWord.setId("wrongPassword");
                        }

                    }
            );
            jfxDialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
                gridPane.setEffect(null);
            });
            VBox vBox = new VBox();
            vBox.getChildren().addAll(oldPassWord, newPassWord);
            jfxDialogLayout.getBody().add(vBox);
            jfxDialogLayout.setActions(jfxButton);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });

    }

    public void changePassBtnActFocus() {
        changePass.requestFocus();
    }


    public void handleOnKeyPressedChangePass(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.changePassBtnAct();
        }
    }

    public void payBillAct() {
        depOrWithOrMakeBill(3);
    }

    public void payBillBtnActFocus() {
        payBill.requestFocus();
    }


    public void handleOnKeyPressedPayBill(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.payBillAct();
        }
    }

    public void makeBillBtnAct() {
        depOrWithOrMakeBill(2);
    }

    public void makeBillBtnActFocus() {
        makeBill.requestFocus();
    }


    public void handleOnKeyPressedMakeBill(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.makeBillBtnAct();
        }
    }


    public void withdrawBtnActFocus() {
        withdraw.requestFocus();
    }

    public void withdrawAct() {
        depOrWithOrMakeBill(1);
    }

    public void handleOnKeyPressedWithdraw(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.withdrawAct();
        }

    }

    public void depositAct() {
        depOrWithOrMakeBill(0);
    }

    private void depOrWithOrMakeBill(int b) {
        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            JFXButton jfxButton = new JFXButton("OK");
            JFXTextField jfxTextField = new JFXTextField();

            jfxTextField.setPromptText("MONEY AMOUNT...");
            if (b == 3)
                jfxTextField.setPromptText("BILL ID...");
            jfxTextField.setId("text");
            jfxDialogLayout.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.3);");
            JFXDialog jfxDialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            jfxButton.getStyleClass().add("dialog-button");
            jfxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        if (!jfxTextField.getText().equals("") && jfxTextField.getText().matches("\\d+")) {
                            if (b == 0)
                                Connection.getConnectionToServer().send(Dictionary.depositDict(jfxTextField.getText()));
                            else if (b == 1)
                                Connection.getConnectionToServer().send(Dictionary.withdrawDict(jfxTextField.getText()));
                            else if (b == 2)
                                Connection.getConnectionToServer().send(Dictionary.defBillDict(jfxTextField.getText()));
                            else if (b == 3)
                                Connection.getConnectionToServer().send(Dictionary.payBillDict(jfxTextField.getText()));

                            jfxDialog.close();
                        } else {
                            jfxTextField.setId("wrongPassword");
                        }

                    }
            );
            jfxDialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
                gridPane.setEffect(null);
            });
            VBox vBox = new VBox();
            vBox.getChildren().addAll(jfxTextField);
            jfxDialogLayout.getBody().add(vBox);
            jfxDialogLayout.setActions(jfxButton);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });

    }

    public void depositBtnActFocus() {
        deposit.requestFocus();
    }


    public void handleOnKeyPressedDeposit(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.depositAct();
        }

    }


    public void handleOnKeyPressedTransfer(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.transferAct();
        }

    }

    private Dictionary transferDic;

    public void transferAct() {

        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            JFXButton jfxButton = new JFXButton("OK");

            JFXTextField fromAccountNumber = new JFXTextField();
            JFXTextField accountNumber = new JFXTextField();
            JFXTextField money = new JFXTextField();
            fromAccountNumber.setPromptText("SRC ACC-NUM...");
            money.setPromptText("MONEY...");

            accountNumber.setPromptText("ACC-NUM...");
            if (isAdmin)
                accountNumber.setPromptText("DEST ACC-NUM...");
            money.setId("text");
            fromAccountNumber.setId("text");
            accountNumber.setId("text");
            jfxDialogLayout.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.3);");
            JFXDialog jfxDialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            jfxButton.getStyleClass().add("dialog-button");
            jfxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        if (!money.getText().equals("") && !accountNumber.getText().equals("") && (!fromAccountNumber.getText().equals("") || !isAdmin) &&
                                money.getText().matches("\\d+") && accountNumber.getText().matches("\\d+")
                                && (fromAccountNumber.getText().matches("\\d+") || !isAdmin)) {
                            transferDic = Dictionary.transferDict(money.getText(), accountNumber.getText());
                            if (isAdmin)
                                transferDic.fromuser = fromAccountNumber.getText();
                            Connection.getConnectionToServer().send(transferDic);
                            jfxDialog.close();
                        } else if (money.getText().equals("") || !money.getText().matches("\\d+")) {
                            money.setId("wrongPassword");
                        } else {
                            accountNumber.setId("wrongPassword");
                        }

                    }
            );
            jfxDialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
                gridPane.setEffect(null);
            });
            VBox vBox = new VBox();
            vBox.getChildren().addAll(money, accountNumber, fromAccountNumber);
            jfxDialogLayout.getBody().add(vBox);
            jfxDialogLayout.setActions(jfxButton);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });
    }

    public void transferBtnActFocus() {
        transfer.requestFocus();
    }

    public void balanceBtnActFocus() {
        balance.requestFocus();
    }

    public void handleOnKeyPressedBalance(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.balanceAct();
        }

    }

    public void balanceAct() {
        if (isAdmin) {
            adminUserChooseDialog(Dictionary.balanceDict());
        } else {
            Connection.getConnectionToServer().send(Dictionary.balanceDict());
        }

    }

    private void adminUserChooseDialog(Dictionary balanceDict) {
        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            JFXButton jfxButton = new JFXButton("OK");
            JFXTextField jfxTextField = new JFXTextField();

            jfxTextField.setPromptText("ENTER A USERNAME...");
            jfxTextField.setId("text");
            jfxDialogLayout.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.3);");
            JFXDialog jfxDialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            jfxButton.getStyleClass().add("dialog-button");
            jfxButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        if (!jfxTextField.getText().equals("")) {
                            balanceDict.username = jfxTextField.getText();
                            Connection.getConnectionToServer().send(balanceDict);

                            jfxDialog.close();
                        } else {
                            jfxTextField.setId("wrongPassword");
                        }

                    }
            );
            jfxDialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
                gridPane.setEffect(null);
            });
            VBox vBox = new VBox();
            vBox.getChildren().addAll(jfxTextField);
            jfxDialogLayout.getBody().add(vBox);
            jfxDialogLayout.setActions(jfxButton);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });
    }

    public void showDialog(String message) {
        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            ScrollPane scrollPane = new ScrollPane();

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
            scrollPane.setContent(vBox);
            jfxDialogLayout.getBody().addAll(scrollPane);
            jfxDialogLayout.setActions(jfxButton);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });


    }

    public void showTransferDialog(String str) {
        Platform.runLater(() -> {
            BoxBlur blur = new BoxBlur(5, 5, 10);
            JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
            JFXButton accept = new JFXButton("ACCEPT");
            JFXButton decline = new JFXButton("DECLINE");
            jfxDialogLayout.setStyle(" -fx-background-color: rgba(0, 0, 0, 0.3);");
            JFXDialog jfxDialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.TOP);
            accept.getStyleClass().add("dialog-button");
            decline.getStyleClass().add("dialog-button");
            accept.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        Connection.getConnectionToServer().send(Dictionary.transferDict(transferDic.money, transferDic.account_number));

                        jfxDialog.close();
                    }
            );
            decline.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                        jfxDialog.close();
                    }
            );
            jfxDialog.setOnDialogClosed((JFXDialogEvent jfxEvent) -> {
                gridPane.setEffect(null);
            });
            Label label = new Label(str);
            label.setStyle("-fx-font-size: 20px; -fx-text-fill: black");
            VBox vBox = new VBox();
            vBox.getChildren().addAll(label);
            jfxDialogLayout.getBody().add(vBox);
            jfxDialogLayout.setActions(accept, decline);
            jfxDialog.show();
            gridPane.setEffect(blur);
        });
    }
}
