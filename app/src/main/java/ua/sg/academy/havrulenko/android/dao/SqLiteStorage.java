package ua.sg.academy.havrulenko.android.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ua.sg.academy.havrulenko.android.HashUtils;
import ua.sg.academy.havrulenko.android.model.Account;
import ua.sg.academy.havrulenko.android.model.Place;
import ua.sg.academy.havrulenko.android.sqlite.AccountDao;
import ua.sg.academy.havrulenko.android.sqlite.HelperFactory;
import ua.sg.academy.havrulenko.android.sqlite.PlaceDao;

@SuppressWarnings("unused")
public class SqLiteStorage {

    private static SqLiteStorage instance;

    private SqLiteStorage() {
    }

    public static SqLiteStorage getInstance() {
        if (instance == null) {
            instance = new SqLiteStorage();
        }
        return instance;
    }

    public boolean contains(String email) {
        try {
            return HelperFactory.getHelper().getAccountsDAO().idExists(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPasswordByEmail(String email) {
        try {
            Account account = HelperFactory.getHelper().getAccountsDAO().queryForId(email);
            return account.getPassword();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getUserByEmail(String email) {
        try {
            return HelperFactory.getHelper().getAccountsDAO().queryForId(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(String email, String password, String image) {
        Account account = new Account();
        account.setEmail(email);
        account.setImage(image);
        account.setPassword(HashUtils.sha512(password));
        try {
            HelperFactory.getHelper().getAccountsDAO().create(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Account account) {
        try {
            HelperFactory.getHelper().getAccountsDAO().update(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public AccountDao getDAO() throws SQLException {
        return HelperFactory.getHelper().getAccountsDAO();
    }

    public PlaceDao getPlaceDAO() throws SQLException {
        return HelperFactory.getHelper().getPlaceDAO();
    }

    public void addPlaceForUser(Account account, Place place) {
        place.setAccount(account);
        account.getPlaces().add(place);
    }

    public Place getPlaceById(int id) {
        try {
            return HelperFactory.getHelper().getPlaceDAO().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<Place> getAllPlacesByUser(String email) {
        return getUserByEmail(email).getPlaces();
    }

    public List<Account> getAll() {
        try {
            return HelperFactory.getHelper().getAccountsDAO().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void delete(Account account) {
        try {
            HelperFactory.getHelper().getAccountsDAO().delete(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAllRecordsLog() {
        try {
            List<Account> accounts = HelperFactory.getHelper().getAccountsDAO().getAll();
            String spacer = "###########################################################################\n";
            StringBuilder sb = new StringBuilder(spacer);
            sb.append("Type: SQLite\n");
            sb.append("Records count: ").append(accounts.size()).append("\n");
            sb.append("Accounts info:\nEmail\t\tPassword\n");
            for (Account a : accounts) {
                sb.append(a.getEmail())
                        .append(" \t- ")
                        .append(a.getPassword())
                        .append("\n");
            }
            sb.append(spacer);
            return sb.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
