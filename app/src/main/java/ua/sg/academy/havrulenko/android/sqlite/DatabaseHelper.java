package ua.sg.academy.havrulenko.android.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ua.sg.academy.havrulenko.android.model.Account;
import ua.sg.academy.havrulenko.android.model.Place;

import static ua.sg.academy.havrulenko.android.model.Account.FIELD_NAME_EMAIL;
import static ua.sg.academy.havrulenko.android.model.Account.FIELD_NAME_IS_ADMIN;
import static ua.sg.academy.havrulenko.android.model.Account.FIELD_NAME_PASSWORD;
import static ua.sg.academy.havrulenko.android.model.Account.TABLE_NAME_ACCOUNTS;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "my.db";
    private static final int DATABASE_VERSION = 8;
    private AccountDao accountDao = null;
    private PlaceDao placeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, Place.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
        insertAdmin(db);
        insertTestUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, Account.class, true);
            TableUtils.dropTable(connectionSource, Place.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + " from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    private void insertAdmin(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(FIELD_NAME_EMAIL, "admin@gmail.com");
        cv.put(FIELD_NAME_PASSWORD, "3627909a29c31381a071ec27f7c9ca97726182aed29a7ddd2e54353322cfb30abb9e3a6df2ac2c20fe23436311d678564d0c8d305930575f60e2d3d048184d79");
        cv.put(FIELD_NAME_IS_ADMIN, true);
        db.insert(TABLE_NAME_ACCOUNTS, null, cv);
    }

    private void insertTestUsers(SQLiteDatabase db) {
        String pass = "3627909a29c31381a071ec27f7c9ca97726182aed29a7ddd2e54353322cfb30abb9e3a6df2ac2c20fe23436311d678564d0c8d305930575f60e2d3d048184d79";
        ContentValues cv = new ContentValues();

       for(int i = 1; i < 21; i++) {
           cv.put(FIELD_NAME_EMAIL, "user" + i + "@ya.ru");
           cv.put(FIELD_NAME_PASSWORD, pass);
           cv.put(FIELD_NAME_IS_ADMIN, false);
           db.insert(TABLE_NAME_ACCOUNTS, null, cv);
       }
    }

    public AccountDao getAccountsDAO() throws SQLException {
        if (accountDao == null) {
            accountDao = new AccountDao(getConnectionSource(), Account.class);
        }
        return accountDao;
    }

    public PlaceDao getPlaceDAO() throws SQLException {
        if (placeDao == null) {
            placeDao = new PlaceDao(getConnectionSource(), Place.class);
        }
        return placeDao;
    }

    @Override
    public void close() {
        super.close();
        accountDao = null;
    }
}