package ua.sg.academy.havrulenko.android.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.Collection;

import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.model.Account;
import ua.sg.academy.havrulenko.android.model.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static String KEY_EMAIL = "email";

    private GoogleMap mMap;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String email = getIntent().getStringExtra(KEY_EMAIL);
        account = SqLiteStorage.getInstance().getUserByEmail(email);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);


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

                File imgFile = new  File(String.valueOf(place.getImage()));
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
                TextView textView = new TextView(MapsActivity.this);
                textView.setText("AfdF");
                return textView;
            }
        });


    }
}
