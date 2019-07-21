package Model;

import Controller.LoginController;
import Controller.MainMenuController;
import View.View;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Connection extends Thread {
    public Socket socket;
    private static Connection connectionToServer;
    public PrintWriter outputStream;
    public BufferedReader inputStream;
    public static String sign;
    public static RSA serverToClientRSA;
    public static RSA clientToServerRSA;
    public static BigInteger n; // RSA for client
    public static BigInteger d; // RSA for client
    public static ArrayList<String> random_keys = new ArrayList<>();

    public static Connection getConnectionToServer() {
        return connectionToServer;
    }

    public static String generateRandomKey(int length) {
        String choose = "QWERTYUIOPASDFGHJKLZVXCBNMqwertyuiopasdfghjklzxvcbnm";
        // 52
        String generated = "";
        for (int i = 0; i < length; i++) {
            generated += choose.charAt(new Random().nextInt(52));
        }
        if (random_keys.contains(generated))
            return generateRandomKey(length);
        random_keys.add(generated);
        return generated;
    }

    @Override
    public void run() {
        int count = 0;
        while (true) {
            String str = "";
            str = receivePacket();
            while (str == null) {
                str = receivePacket();
            }

            if (count == 0) {
                clientToServerRSA = new RSA(str);
                sendPacket(n.toString() + "]");
                count++;
                continue;
            }
            if (count == 1) {
                sign = str;
                count++;
                continue;
            }
            str = getRSAmsg(str);
            System.out.println(str);
            if (str.equals("username or password not valid")) {
                LoginController.getInstance().wrongPassStyle();
                LoginController.getInstance().wrongUserStyle();
            } else if (str.contains("registered and logged in")) {

                Dictionary.myUserName = str.split(" ")[0];
                int x = str.split(" ").length;
                Dictionary.myAccountNumber = str.split(" ")[x - 1];
                View.makeMainMenuScene();
            } else if (str.contains("logged In")) {

                Dictionary.myUserName = str.split(" ")[0];
                int x = str.split(" ").length;
                Dictionary.myAccountNumber = str.split(" ")[x - 1];
                View.makeMainMenuScene();
            } else if (str.equals("username not valid")) {
                LoginController.getInstance().wrongUserStyle();
            } else if (str.equals("this user name is online")) {
                LoginController.getInstance().showDialog("Another Session is Active");
            } else if (str.contains(" has")) {
                int x = str.split(" ").length;
                String money = str.split(" ")[x - 1];
                MainMenuController.getInstance().showDialog("Your Account Balance is " + money + "$");
            }

        }
    }

    public String getRSAmsg(String msg) {
        msg = msg.substring(1, msg.length() - 1);
        ArrayList<BigInteger> decInput = new ArrayList<>();
        for (String s : msg.split(", ")) {
            if (s.length() != 0)
                decInput.add(new BigInteger(s));
        }
        return serverToClientRSA.decrypt(decInput);
    }

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            outputStream = new PrintWriter(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connectionToServer = this;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.inputStream = inputStream;
        new Thread(this).start();


    }

    public void send(Dictionary dict) {
        ArrayList<BigInteger> ar = clientToServerRSA.encrypt(new Gson().toJson(dict));
        sendPacket(String.valueOf(ar));
    }

    public void sendPacket(String string) {
        outputStream.write(string);
        outputStream.flush();
    }

    public String receivePacket() {
        try {
            return inputStream.readLine();
        } catch (IOException e) {
        }

        return null;
    }
}
