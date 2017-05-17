package ua.sg.academy.havrulenko.android.sqlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class AccountDao extends BaseDaoImpl<Account, String> {
    AccountDao(ConnectionSource connectionSource, Class<Account> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
    public List<Account> getAll() throws SQLException {
        return this.queryForAll();
    }
}