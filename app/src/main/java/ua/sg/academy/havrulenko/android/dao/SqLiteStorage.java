package ua.sg.academy.havrulenko.android.dao;

import java.sql.SQLException;
import java.util.List;

import ua.sg.academy.havrulenko.android.HashUtils;
import ua.sg.academy.havrulenko.android.sqlite.Account;
import ua.sg.academy.havrulenko.android.sqlite.HelperFactory;

public class SqLiteStorage implements UsersDaoInterface {

    private static SqLiteStorage instance;

    private SqLiteStorage() {
    }

    public static SqLiteStorage getInstance() {
        if (instance == null) {
            instance = new SqLiteStorage();
        }
        return instance;
    }


    @Override
    public boolean contains(String email) {
        try {
            return HelperFactory.getHelper().getAccountsDAO().idExists(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getPasswordByEmail(String email) {
        try {
            Account account = HelperFactory.getHelper().getAccountsDAO().queryForId(email);
            return account.getPassword();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUser(String email, String password) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(HashUtils.sha512(password));
        try {
            HelperFactory.getHelper().getAccountsDAO().create(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAllRecordsLog() {
        try {
            List<Account> accounts = HelperFactory.getHelper().getAccountsDAO().getAll();
            String spacer = "###########################################################################\n";
            StringBuilder sb = new StringBuilder(spacer);
            sb.append("Type: SQLite\n");
            sb.append("Records count: ").append(accounts.size()).append("\n");
            sb.append("Accounts info:\nEmail\t\tPassword\n");
            for (Account a: accounts) {
                sb.append(a.getEmail())
                        .append(" \t- ")
                        .append(a.getPassword())
                        .append("\n");
            }
            sb.append(spacer);
            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " +e.getMessage();
        }
    }
}