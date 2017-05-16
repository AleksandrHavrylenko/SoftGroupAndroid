package ua.sg.academy.havrulenko.android.dao;

public interface UsersDaoInterface {

    boolean contains(String email);

    String getPasswordByEmail(String email);

    void addUser(String email, String password);

    String getAllRecordsLog();
}
