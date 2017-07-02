package ua.sg.academy.havrulenko.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("all")
@DatabaseTable(tableName = Place.TABLE_NAME_PLACES)
public class Place {
    public static final String TABLE_NAME_PLACES = "places";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_TITLE = "title";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_LATITUDE = "latitude";
    public static final String FIELD_NAME_LONGITUDE = "longitude";
    public static final String FIELD_NAME_IMAGE = "image";
    public static final String FIELD_NAME_ACCOUNT = "account";

    @DatabaseField(generatedId = true, columnName = FIELD_NAME_ID)
    private int id;
    @DatabaseField(columnName = FIELD_NAME_TITLE)
    private String title;
    @DatabaseField(columnName = FIELD_NAME_DESCRIPTION)
    private String description;
    @DatabaseField(columnName = FIELD_NAME_LATITUDE)
    private double latitude;
    @DatabaseField(columnName = FIELD_NAME_LONGITUDE)
    private double longitude;
    @DatabaseField(columnName = FIELD_NAME_IMAGE)
    private String image;
    @DatabaseField(foreign = true, columnName = FIELD_NAME_ACCOUNT, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Account account;

    public Place() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude='" + longitude + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}