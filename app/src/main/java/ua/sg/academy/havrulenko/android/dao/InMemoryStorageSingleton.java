package ua.sg.academy.havrulenko.android.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;

import ua.sg.academy.havrulenko.android.HashUtils;

public class InMemoryStorageSingleton implements UsersDaoInterface {

    private static InMemoryStorageSingleton instance;
    private final HashMap<String, String> hashMap;

    private InMemoryStorageSingleton() {
        hashMap = new LinkedHashMap<>();
    }

    public static InMemoryStorageSingleton getInstance() {
        if (instance == null) {
            instance = new InMemoryStorageSingleton();
        }
        return instance;
    }

    @Override
    public boolean contains(String email) {
        return hashMap.containsKey(email);
    }

    @Override
    public String getPasswordByEmail(String email) {
        return hashMap.get(email);
    }

    @Override
    public void addUser(String email, String password) {
        hashMap.put(email, HashUtils.sha512(password));
    }

    @Override
    public String getAllRecordsLog() {
        String spacer = "###########################################################################\n";
        StringBuilder sb = new StringBuilder(spacer);
        sb.append("Type: in memory\n");
        sb.append("Records count: ").append(hashMap.size()).append("\n");
        sb.append("Accounts info:\nEmail\t\tPassword\n");
        for (String key : hashMap.keySet()) {
            sb.append(key)
                    .append(" \t- ")
                    .append(hashMap.get(key))
                    .append("\n");
        }
        sb.append(spacer);
        return sb.toString();
    }
}
