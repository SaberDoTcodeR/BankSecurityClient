package Model;

import Controller.LoginController;
import Controller.MainMenuController;
import View.View;
import com.google.gson.Gson;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
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
        try {
            while (true) {
                String str = null;
                while (str == null) {
                    str = inputStream.readLine();
                }

                if (count == 0) {
                    clientToServerRSA = new RSA(str);
                    sendPacket(n.toString() + "]");
                    count++;
                    System.out.println(n.toString());
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
                } else if (str.equals("256 admin logged in")) {
                    /////admin
                    View.makeMainMenuScene();
                } else if (str.contains("logged In with")) {
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
                } else if (str.contains("deposit done")) {
                    int x = str.split(" ").length;
                    String money = str.split(" ")[x - 1];
                    MainMenuController.getInstance().showDialog("Successfully Deposited ,Your Account Balance is " + money + "$");
                } else if (str.contains("withdraw done")) {
                    int x = str.split(" ").length;
                    String money = str.split(" ")[x - 1];
                    MainMenuController.getInstance().showDialog("Successfully withdraw ,Your Account Balance is " + money + "$");
                } else if (str.contains("withdraw error")) {/// not needed should handled bye not enough money
                    MainMenuController.getInstance().showDialog("You Dont Have Enough Money To Do this Action");
                } else if (str.equals("not enough money")) {
                    MainMenuController.getInstance().showDialog("You Dont Have Enough Money To Do this Action.");
                } else if (str.contains("bill made with")) {
                    MainMenuController.getInstance().showDialog(str + "$");
                } else if (str.equals("bill paid successfully")) {///////////////
                    MainMenuController.getInstance().showDialog("Bill Paid Successfully.");
                } else if (str.equals("bill id not found")) {///////////////
                    MainMenuController.getInstance().showDialog("Bill ID Not Found.");
                } else if (str.equals("password changed")) {
                    MainMenuController.getInstance().showDialog("PASSWORD CHANGED SUCCESSFULLY.");
                } else if (str.equals("old password not correct")) {
                    MainMenuController.getInstance().showDialog("OLD PASSWORD NOT CORRECT.");
                } else if (str.equals("no trans action")) {
                    MainMenuController.getInstance().showDialog("YOU HAVE NOT DONE ANY TRANSACTIONS YET.");
                } else if (str.equals("error for transfer request")) {
                    MainMenuController.getInstance().showDialog("TRANSFER REQUEST FAILED.");
                } else if (str.equals("transfer done")) {
                    MainMenuController.getInstance().showDialog("TRANSFER DONE SUCCESSFULLY.");
                } else if (str.equals("no account with this number")) {
                    MainMenuController.getInstance().showDialog("YOUR ENTERED ACCOUNT NUMBER IS NOT EXIST.");
                } else if (str.contains("You're Transferring")) {
                    MainMenuController.getInstance().showTransferDialog(str);
                } else if (str.startsWith("224[")) {//how to recognise trans todo
                    str = str.replace("[", "");
                    str = str.replace("]", "");
                    str = str.replace("\"", "");
                    str = str.replace(",", "\n");
                    MainMenuController.getInstance().showDialog("TRANSACTIONS :\n" + str);
                }
            }
        } catch (IOException e) {
            View.makeLoginScene();

            while (true) {
                try {
                    Socket socket = new Socket("192.168.43.157", 55555);
                    new Connection(socket);
                    break;
                } catch (Exception e2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }


            return;

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
            System.out.println("asdad");
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


}
