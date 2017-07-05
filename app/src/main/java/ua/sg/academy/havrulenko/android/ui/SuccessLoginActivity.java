package ua.sg.academy.havrulenko.android.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import ua.sg.academy.havrulenko.android.CityAsyncTask;
import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.fragments.AccountDetailsFragment;
import ua.sg.academy.havrulenko.android.model.Account;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static ua.sg.academy.havrulenko.android.ui.AddPlaceActivity.KEY_EMAIL;
import static ua.sg.academy.havrulenko.android.ui.MainActivity.KEY_SESSION_EMAIL;

public class SuccessLoginActivity extends AppCompatActivity {

    private static final String TAG = SuccessLoginActivity.class.getSimpleName();
    private static final int CODE_CHECK_PERMISSIONS = 101;
    private LocationManager locationManager;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(TAG, "onLocationChanged: lat: " + latitude + " lon: " + longitude);
            new CityAsyncTask(SuccessLoginActivity.this, latitude, longitude).execute();
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        Button buttonUserList = (Button) findViewById(R.id.buttonUserList);
        Button buttonEditData = (Button) findViewById(R.id.buttonEditData);
        Button buttonAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        Button buttonMyPlaces = (Button) findViewById(R.id.buttonMyPlaces);

        String email = getIntent().getStringExtra(KEY_SESSION_EMAIL);
        Account account = SqLiteStorage.getInstance().getUserByEmail(email);
        buttonLogout.setOnClickListener(v -> onClickLogout());
        buttonUserList.setVisibility(account.isAdmin() ? VISIBLE : GONE);
        buttonUserList.setClickable(account.isAdmin());
        buttonUserList.setOnClickListener(v -> onClickButtonUserList());
        buttonEditData.setOnClickListener(v -> onClickEditData(email));
        buttonAddPlace.setOnClickListener(v -> onClickAddPlace(email));
        buttonMyPlaces.setOnClickListener(v -> onClickMyPlaces(email));

        AccountDetailsFragment fragment = AccountDetailsFragment.newInstance(email, false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
    }

    private void enableLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "enableLocationUpdates()");
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private void onClickLogout() {
        clearSession();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void onClickButtonUserList() {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    private void onClickEditData(String email) {
        Intent intent = new Intent(this, EditUserDataActivity.class);
        intent.putExtra(KEY_SESSION_EMAIL, email);
        startActivity(intent);
    }

    private void onClickAddPlace(String email) {
        Intent intent = new Intent(this, AddPlaceActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);
    }

    private void onClickMyPlaces(String email) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(KEY_EMAIL, email);
        startActivity(intent);
    }

    private void clearSession() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_EMAIL, "");
        editor.apply();
    }

    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, CODE_CHECK_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_CHECK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocationUpdates();
                } else {
                    Toast.makeText(this, "No permissions!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
