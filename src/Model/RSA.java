package Model;

import java.math.BigInteger;
import java.util.ArrayList;

public class RSA {
    static int stirngSize = 5;
    int firstPrime;
    int secondPrim;
    BigInteger n;
    int e = 65537;
    BigInteger d; // private key
    BigInteger phi;

    public static BigInteger getN(String pubKey) {
        int modulus = pubKey.indexOf("modulus");
        String num = pubKey.substring(modulus + 9, pubKey.indexOf("public exponent") - 3);
        return new BigInteger(num);
    }

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
