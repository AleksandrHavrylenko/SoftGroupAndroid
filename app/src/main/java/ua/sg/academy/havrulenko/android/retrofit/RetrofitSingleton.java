package ua.sg.academy.havrulenko.android.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ua.sg.academy.havrulenko.android.retrofit.Const.BASE_URL;

public class RetrofitSingleton {
    private static IWeatherApi api;

    public static synchronized IWeatherApi getWeatherApi() {
        if (api == null) {
            api = init();
        }
        return api;
    }

    private static IWeatherApi init() {
        OkHttpClient client = new OkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(IWeatherApi.class);
    }

}
