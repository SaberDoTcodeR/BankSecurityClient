package Model;

import View.View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;

public class Main extends Application {
    private static Stage primaryStage1;

    public static Stage getPrimaryStage() {
        return primaryStage1;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        Key pub = kp.getPublic();
        RSAPrivateKey pr = ((RSAPrivateKey) kp.getPrivate());
        Key pvt = kp.getPrivate();
        System.out.println("public key : " + pub.toString());
        Connection.n = getN(pub.toString());
        Connection.d = pr.getPrivateExponent();
        Connection.serverToClientRSA = new RSA(Connection.n, Connection.d); // for receiving from server
        Socket socket = new Socket("localhost", 55555);
        primaryStage1 = primaryStage;
        primaryStage1.setFullScreen(true);
        primaryStage1.setResizable(false);
        View.makeLoginScene();
        primaryStage1.show();
        new Connection(socket);
    }

    public static BigInteger getN(String pubKey) {
        int modulus = pubKey.indexOf("modulus");
        String num = pubKey.substring(modulus + 9, pubKey.indexOf("public exponent") - 3);
        return new BigInteger(num);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
