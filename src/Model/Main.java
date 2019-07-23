package Model;

import View.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;

public class Main extends Application {
    private static Stage primaryStage1;
    public static Main main;
    public final static String host = "localhost";

    public static Stage getPrimaryStage() {
        return primaryStage1;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        main = this;
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        RSAPrivateKey pr = ((RSAPrivateKey) kp.getPrivate());
        Key pvt = kp.getPrivate();
        Connection.n = RSA.getN(pub.toString());
        Connection.d = pr.getPrivateExponent();
        Connection.serverToClientRSA = new RSA(Connection.n, Connection.d); // for receiving from server
        while (true) {
            try {
                Socket socket = new Socket(host, 55555);
                new Connection(socket);
                break;
            } catch (IOException e2) {
            }
        }
        primaryStage1 = primaryStage;
        primaryStage1.setFullScreen(true);
        primaryStage1.setResizable(false);
        View.makeLoginScene();
        primaryStage1.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
