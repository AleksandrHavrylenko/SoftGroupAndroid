package ua.sg.academy.havrulenko.android.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.File;
import java.text.DecimalFormat;

import ua.sg.academy.havrulenko.android.MyApplication;
import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.Utils;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.ActivityAddPlaceBinding;
import ua.sg.academy.havrulenko.android.model.Account;
import ua.sg.academy.havrulenko.android.model.Place;

public class AddPlaceActivity extends AppCompatActivity {
    private static final String TAG = AddPlaceActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SELECT_POINT = 0;
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_IMAGE_PATH = "image";

    public static final String KEY_EMAIL = "email";
    private ActivityAddPlaceBinding mBinding;
    private String imagePath;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_place);
        String email = getIntent().getStringExtra(KEY_EMAIL);
        Account account = SqLiteStorage.getInstance().getUserByEmail(email);
        if (savedInstanceState != null) {
            latitude = savedInstanceState.getDouble(KEY_LATITUDE, Double.NaN);
            longitude = savedInstanceState.getDouble(KEY_LONGITUDE, Double.NaN);
            imagePath = savedInstanceState.getString(KEY_IMAGE_PATH);
            Log.d(TAG, "onCreate: " + latitude + "l " + longitude);
            File imgFile = new  File(MyApplication.getImgPathPlaces(), String.valueOf(imagePath));
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mBinding.imageView.setImageBitmap(myBitmap);
            }
            if (latitude != 0 && longitude != 0) {
                DecimalFormat df = new DecimalFormat("#.0000");
                String msg = df.format(latitude) + " " + df.format(longitude);
                mBinding.buttonOpenMap.setText(msg);
            }
        }

        mBinding.buttonSave.setOnClickListener(v -> {
            String title = mBinding.editTextTitle.getText().toString();
            String description = mBinding.editTextDescription.getText().toString();

            if (!title.isEmpty() && !description.isEmpty() && latitude != 0 && longitude != 0 &&
                    imagePath != null) {
                Place place = new Place();
                place.setTitle(title);
                place.setImage(imagePath);
                place.setLatitude(latitude);
                place.setLongitude(longitude);
                place.setDescription(description);
                SqLiteStorage.getInstance().addPlaceForUser(account, place);
                finish();
            } else {
                Toast.makeText(AddPlaceActivity.this, "Fill all fields", Toast.LENGTH_LONG).show();
            }
        });

        mBinding.imageView.setOnClickListener(v ->
                PickImageDialog.build(new PickSetup().setWidth(512).setHeight(512))
                        .setOnPickResult(r -> {
                            mBinding.imageView.setImageBitmap(r.getBitmap());
                            imagePath = Utils.savePlaceImage(r.getBitmap());
                        }).show(getSupportFragmentManager()));
        mBinding.buttonOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectPointOnMapActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_POINT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_POINT) {
            if(resultCode == RESULT_OK) {
                latitude = data.getDoubleExtra(SelectPointOnMapActivity.KEY_LATITUDE, 0);
                longitude = data.getDoubleExtra(SelectPointOnMapActivity.KEY_LONGITUDE, 0);
                DecimalFormat df = new DecimalFormat("#.0000");
                String msg = df.format(latitude) + " " + df.format(longitude);
                mBinding.buttonOpenMap.setText(msg);
                Log.d(TAG, "onActivityResult: x: " + latitude + " y:" +longitude);
            } else {
                Log.d(TAG, "onActivityResult: " + resultCode);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(KEY_LATITUDE, latitude);
        outState.putDouble(KEY_LONGITUDE, longitude);
        outState.putString(KEY_IMAGE_PATH, imagePath);
    }

}
