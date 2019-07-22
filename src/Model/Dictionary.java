package Model;

public class Dictionary {
    public static String myAccountNumber;
    public static String myUserName;
    public String function;
    public String username;
    public String password;
    public String newpassword;
    public String account_number;
    public String money;
    public String bill_ID;
    public String sign;
    public String random_key;
    public String fromuser; // for admin in transfer


    public static Dictionary registerDict(String username, String password) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "1";
        dictionary.username = username;
        dictionary.password = password;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary loginDict(String username, String password) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "2";
        dictionary.username = username;
        dictionary.password = password;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary balanceDict() {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "3";
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary depositDict(String money) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "9";
        dictionary.money = money;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary withdrawDict(String money) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "10";
        dictionary.money = money;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary defBillDict(String money) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "6";
        dictionary.money = money;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary payBillDict(String bill_ID) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "7";
        dictionary.bill_ID = bill_ID;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary changePass(String old, String newPassword) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "5";
        dictionary.password = old;
        dictionary.newpassword = newPassword;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary transactionsDict() {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "8";
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary transferDict(String money, String accountNumber) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "4";
        dictionary.money = money;
        dictionary.account_number = accountNumber;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }
    public static Dictionary transferRequestDict(String money, String accountNumber) {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "12";
        dictionary.money = money;
        dictionary.account_number = accountNumber;
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }

    public static Dictionary logoutDict() {
        Dictionary dictionary = new Dictionary();
        dictionary.function = "13";
        dictionary.sign = Connection.sign;
        dictionary.random_key = Connection.generateRandomKey(10);
        return dictionary;
    }
}

