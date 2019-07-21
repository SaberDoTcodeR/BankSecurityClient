package Model;

import Controller.LoginController;
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

    private static Connection connectionToServer;
    public static PrintWriter outputStream;
    public static BufferedReader inputStream;
    public static String sign;
    static RSA serverToClientRSA;
    static RSA clientToServerRSA;
    static BigInteger n; // RSA for client
    static BigInteger d; // RSA for client
    static ArrayList<String> random_keys = new ArrayList<>();

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
                //View.makeMainMenuScene();
            } else if (str.contains("logged in")) {

                Dictionary.myUserName = str.split(" ")[0];
                int x = str.split(" ").length;
                Dictionary.myAccountNumber = str.split(" ")[x - 1];
                //View.makeMainMenuScene();
            } else if (str.equals("username not valid")) {
                LoginController.getInstance().wrongUserStyle();
            }else if(str.equals("this user name is online")){
                //LoginController.getInstance()
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
            outputStream = new PrintWriter(socket.getOutputStream());
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connectionToServer = this;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.inputStream = inputStream;
        new Thread(this).start();


    }

    public static void send(Dictionary dict) {
        ArrayList<BigInteger> ar = clientToServerRSA.encrypt(new Gson().toJson(dict));
        sendPacket(String.valueOf(ar));
    }

    public static void sendPacket(String string) {
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

class RSA {
    static int stirngSize = 5;
    int firstPrime;
    int secondPrim;
    BigInteger n;
    int e = 65537;
    BigInteger d; // private key
    BigInteger phi;

    public RSA(BigInteger n, BigInteger d) {
        this.n = n;
        this.d = d;
    }

    public RSA(String n) {
        this.n = new BigInteger(n);
    }

    public BigInteger convertStringToInt(String message) {
        long res = 0L;
        for (char i : message.toCharArray()) {
            res = res * 1000 + i;
        }
        return BigInteger.valueOf(res);
    }

    public String convertIntToString(BigInteger number) {
        String res = "";
        String converted = String.valueOf(number);
        if (converted.length() % 3 == 2) {
            converted = "0" + converted;
        } else if (converted.length() % 3 == 1) {
            converted = "00" + converted;
        }
        for (int i = 0; i <= converted.length(); i++) {
            if ((i + 1) % 3 == 0) {
                res += (char) (Long.valueOf(converted.substring((i - 2), i + 1)).intValue());
            }
        }
        return res;
    }

    public ArrayList<String> devideMessage(String message) {
        if (message.length() <= 3) {
            ArrayList<String> s = new ArrayList<>();
            s.add(message);
            return s;
        }
        long chunks = message.length();
        int chunkSize = stirngSize;
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < chunks; i += chunkSize) {
            int last = Math.min(i + chunkSize, message.length());
            res.add(message.substring(i, last));
        }
        return res;
    }


    public ArrayList<BigInteger> encrypt(String message) {
        ArrayList<String> div = devideMessage(message);
        ArrayList<BigInteger> res = new ArrayList<>();
        for (String s : div) {
            BigInteger m2 = convertStringToInt(s);
            res.add(m2.modPow(BigInteger.valueOf(e), n));
        }
        return res;
    }

    public String decrypt(ArrayList<BigInteger> integer) {
        String res = "";
        for (BigInteger aLong : integer) {
            res += convertIntToString(aLong.modPow(d, n));
        }
        return res;
    }
}
