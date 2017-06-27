package ua.sg.academy.havrulenko.android.sqlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("all")
@DatabaseTable(tableName = Account.TABLE_NAME_ACCOUNTS)
public class Account {

    public static final String TABLE_NAME_ACCOUNTS = "accounts";
    public static final String FIELD_NAME_EMAIL = "email";
    public static final String FIELD_NAME_PASSWORD = "password";
    public static final String FIELD_NAME_IS_ADMIN = "isAdmin";


    @DatabaseField(id = true, columnName = FIELD_NAME_EMAIL)
    private String email;
    @DatabaseField(columnName = FIELD_NAME_PASSWORD)
    private String password;
    @DatabaseField(columnName = FIELD_NAME_IS_ADMIN)
    private boolean isAdmin;

    public Account() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}