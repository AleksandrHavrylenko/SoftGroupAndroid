package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ua.sg.academy.havrulenko.android.R;

import static ua.sg.academy.havrulenko.android.R.id.map;

public class MapTestActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    private GoogleMap mMap;
    private TextView textView;
    private FloatingActionButton fab;
    private double latitude = Double.NaN;
    private double longitude = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);
        if(savedInstanceState != null) {
            latitude = savedInstanceState.getDouble(KEY_LATITUDE, Double.NaN);
            longitude = savedInstanceState.getDouble(KEY_LONGITUDE, Double.NaN);
        }

        textView = (TextView) findViewById(R.id.textView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            if(!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
                Intent intent = new Intent();
                intent.putExtra(KEY_LATITUDE, latitude);
                intent.putExtra(KEY_LONGITUDE, longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng che = new LatLng(51, 31);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.camera_colored)));
        //mMap.addMarker(new MarkerOptions().position(che).title("Cherhigov"));
        mMap.addCircle(new CircleOptions().center(che)).setRadius(500);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(che));

        mMap.setOnMapClickListener(point -> {
            latitude = point.latitude;
            longitude = point.longitude;
            textView.setText(point.latitude + " " + point.longitude);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(point));
        });
    }
}
