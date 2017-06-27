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
    public static final String FIELD_NAME_FIRST_NAME = "firstName";
    public static final String FIELD_NAME_LAST_NAME = "lastName";
    public static final String FIELD_NAME_MIDDLE_NAME = "middleName";
    public static final String FIELD_NAME_NICKNAME = "nickname";
    public static final String FIELD_NAME_PHONE = "phone";

    @DatabaseField(id = true, columnName = FIELD_NAME_EMAIL)
    private String email;
    @DatabaseField(columnName = FIELD_NAME_PASSWORD)
    private String password;
    @DatabaseField(columnName = FIELD_NAME_IS_ADMIN)
    private boolean isAdmin;
    @DatabaseField(columnName = FIELD_NAME_FIRST_NAME)
    private String firstName;
    @DatabaseField(columnName = FIELD_NAME_LAST_NAME)
    private String lastName;
    @DatabaseField(columnName = FIELD_NAME_MIDDLE_NAME)
    private String middleName;
    @DatabaseField(columnName = FIELD_NAME_NICKNAME)
    private String nickname;
    @DatabaseField(columnName = FIELD_NAME_PHONE)
    private String phone;

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

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}