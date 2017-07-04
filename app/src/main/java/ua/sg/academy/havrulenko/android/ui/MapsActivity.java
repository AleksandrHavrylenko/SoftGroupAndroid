package ua.sg.academy.havrulenko.android.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.Collection;

import ua.sg.academy.havrulenko.android.MyApplication;
import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.model.Account;
import ua.sg.academy.havrulenko.android.model.Place;

import static ua.sg.academy.havrulenko.android.ui.PlaceInfoActivity.KEY_PLACE_ID;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final String KEY_EMAIL = "email";
    private static final int CODE_CHECK_PERMISSIONS = 101;
    private static final LatLng DEFAULT_COORDINATES = new LatLng(51.494481515522786, 31.295225881040093);
    private static final float DEFAULT_ZOOM = 12;

    private GoogleMap mMap;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String email = getIntent().getStringExtra(KEY_EMAIL);
        account = SqLiteStorage.getInstance().getUserByEmail(email);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_COORDINATES));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));

        Collection<Place> places = account.getPlaces();
        Log.d(TAG, "onMapReady: " + places.size());
        for(Place p: places) {
            Log.d(TAG, "onMapReady: " + p.toString());
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getLatitude(), p.getLongitude()))
                    .snippet(String.valueOf(p.getId())));
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.card_place, null);
                ImageView imageView = (ImageView) v.findViewById(R.id.image_view);
                TextView tvName = (TextView) v.findViewById(R.id.tvName);
                TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);

                Place place = SqLiteStorage.getInstance().getPlaceById(Integer.parseInt(marker.getSnippet()));
                tvName.setText(String.valueOf(place.getTitle()));
                tvDescription.setText(String.valueOf(place.getDescription()));

                File imgFile = new  File(MyApplication.getImgPathPlaces(), String.valueOf(place.getImage()));
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }else {
                    imageView.setImageResource(R.drawable.ic_account_circle);
                }
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return getInfoWindow(marker);
            }
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            int placeId = Integer.parseInt(marker.getSnippet());
            Intent intent = new Intent(MapsActivity.this, PlaceInfoActivity.class);
            intent.putExtra(KEY_PLACE_ID, placeId);
            startActivity(intent);
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "onMapReady: NoPermissions");
            return;
        }
        mMap.setMyLocationEnabled(true);
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
