package model;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    public static final String ATTRIBUTE_NAME = "Account Manager";
    private final Map<String, String> data;

    public AccountManager() {
        data = new HashMap<>();

        createNewAccount("Patrick", "1234");
        createNewAccount("Molly", "FloPup");
    }

    public boolean hasAccount(String username) {
        return data.containsKey(username);
    }

    public boolean isCorrectPassword(String username, String password) {
        return hasAccount(username) && data.get(username).equals(password);
    }

    public void createNewAccount(String username, String password) {
        data.put(username, password);
    }

}
