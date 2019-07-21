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

}

