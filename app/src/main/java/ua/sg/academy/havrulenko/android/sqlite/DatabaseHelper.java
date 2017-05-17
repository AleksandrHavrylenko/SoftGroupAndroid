package ua.sg.academy.havrulenko.android.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "my.db";
    private AccountDao accountDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Account.class);
        } catch (SQLException e) {
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, Account.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "error upgrading db " + DATABASE_NAME + " from ver " + oldVer);
            throw new RuntimeException(e);
        }
    }

    public AccountDao getAccountsDAO() throws SQLException {
        if (accountDao == null) {
            accountDao = new AccountDao(getConnectionSource(), Account.class);
        }
        return accountDao;
    }

    @Override
    public void close() {
        super.close();
        accountDao = null;
    }
}