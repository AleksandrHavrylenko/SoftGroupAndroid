package ua.sg.academy.havrulenko.android.ui;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.sg.academy.havrulenko.android.MyApplication;
import ua.sg.academy.havrulenko.android.R;
import ua.sg.academy.havrulenko.android.dao.SqLiteStorage;
import ua.sg.academy.havrulenko.android.databinding.ActivityPlaceInfoBinding;
import ua.sg.academy.havrulenko.android.entity.Weather;
import ua.sg.academy.havrulenko.android.model.Place;
import ua.sg.academy.havrulenko.android.retrofit.Const;
import ua.sg.academy.havrulenko.android.retrofit.RetrofitSingleton;

public class PlaceInfoActivity extends AppCompatActivity {
    public static final String KEY_PLACE_ID = "place_id";
    private static final String TAG = PlaceInfoActivity.class.getSimpleName();
    private ActivityPlaceInfoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_place_info);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int placeId = getIntent().getIntExtra(KEY_PLACE_ID, -1);
        Place place = SqLiteStorage.getInstance().getPlaceById(placeId);

        mBinding.tvTitle.setText(String.valueOf(place.getTitle()));
        mBinding.tvLocation.setText(String.valueOf(place.getLatitude() + "\n" + place.getLongitude()));
        mBinding.tvPlaceDescription.setText(String.valueOf(place.getDescription()));
        File imgFile = new File(MyApplication.getImgPathPlaces(), String.valueOf(place.getImage()));
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mBinding.imageView.setImageBitmap(myBitmap);
        } else {
            mBinding.imageView.setImageResource(R.drawable.ic_account_circle);
        }

        RetrofitSingleton.getWeatherApi()
                .getWeather(Double.toString(place.getLatitude()), Double.toString(place.getLongitude()),
                        "metric", Const.APP_ID)
                .enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        if (response.isSuccessful()) {
                            Weather weather = response.body();
                            if (weather != null) {
                                double temp = weather.getMain().getTemp();
                                double wind = weather.getWind().getSpeed();
                                double clouds = weather.getClouds().getAll();
                                String icon = weather.getWeather().get(0).getIcon();

                                mBinding.tvWeather.setText(weather.getName() + " - " + weather.getWeather().get(0)
                                        .getMain());
                                mBinding.tvTemp.setText(getString(R.string.weather_temp, String.valueOf(temp)));
                                mBinding.tvWind.setText(getString(R.string.weather_wind, String.valueOf(wind)));
                                mBinding.tvClouds.setText(getString(R.string.weather_clouds, String.valueOf(clouds)));
                                Picasso.with(PlaceInfoActivity.this)
                                        .load("http://openweathermap.org/img/w/" + icon + ".png")
                                        .into(mBinding.imageViewWeather);
                                Log.d(TAG, "onResponse: " + temp + "°C wind: " + wind + " clouds: " + clouds + "%");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
