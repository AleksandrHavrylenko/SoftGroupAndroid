package ua.sg.academy.havrulenko.android.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ua.sg.academy.havrulenko.android.entity.Weather;

@SuppressWarnings("all")
public interface IWeatherApi {

    @GET("weather")
    Call<Weather> getWeather(@Query("lat") String lat, @Query("lon") String lon,
                             @Query("units") String units, @Query("appid") String appId);
}
