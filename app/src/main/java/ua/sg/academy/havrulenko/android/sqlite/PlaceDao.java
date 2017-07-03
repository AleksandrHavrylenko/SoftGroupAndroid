package ua.sg.academy.havrulenko.android.sqlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ua.sg.academy.havrulenko.android.model.Place;

@SuppressWarnings("unused")
public class PlaceDao extends BaseDaoImpl<Place, Integer> {
    PlaceDao(ConnectionSource connectionSource, Class<Place> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Place> getAll() throws SQLException {
        return this.queryForAll();
    }
}