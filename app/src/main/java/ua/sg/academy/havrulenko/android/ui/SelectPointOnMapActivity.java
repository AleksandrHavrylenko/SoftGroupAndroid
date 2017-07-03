package ua.sg.academy.havrulenko.android.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;

import ua.sg.academy.havrulenko.android.R;

import static ua.sg.academy.havrulenko.android.R.id.map;

public class SelectPointOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    private static final int CODE_CHECK_PERMISSIONS = 101;
    private static final LatLng DEFAULT_COORDINATES = new LatLng(51.494481515522786, 31.295225881040093);
    private static final float DEFAULT_ZOOM = 12;
    private static final String TAG = SelectPointOnMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private double latitude = Double.NaN;
    private double longitude = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_point_on_map);
        if (savedInstanceState != null) {
            latitude = savedInstanceState.getDouble(KEY_LATITUDE, Double.NaN);
            longitude = savedInstanceState.getDouble(KEY_LONGITUDE, Double.NaN);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
                Intent intent = new Intent();
                intent.putExtra(KEY_LATITUDE, latitude);
                intent.putExtra(KEY_LONGITUDE, longitude);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
            LatLng marker = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_COORDINATES));
        }

        mMap.setOnMapClickListener(p -> {
            latitude = p.latitude;
            longitude = p.longitude;
            DecimalFormat df = new DecimalFormat("#.0000");
            String msg = df.format(latitude) + " " + df.format(longitude);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(p));
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onMapReady: NoPermissions");
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(KEY_LATITUDE, latitude);
        outState.putDouble(KEY_LONGITUDE, longitude);
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
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(this, "No permissions!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
