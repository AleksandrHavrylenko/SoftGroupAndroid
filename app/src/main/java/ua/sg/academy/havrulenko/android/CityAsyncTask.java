package ua.sg.academy.havrulenko.android;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CityAsyncTask extends AsyncTask<String, String, String> {
    private final Activity activity;
    private final double latitude;
    private final double longitude;

    public CityAsyncTask(Activity activity, double latitude, double longitude) {
        this.activity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            Log.d("Address", "-->" + addresses);
            if (addresses.isEmpty()) return "null";
            result = addresses.get(0).getAddressLine(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
        super.onPostExecute(result);

    }
}