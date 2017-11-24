package ptr.hf.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.DirectionsResult;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import ptr.hf.R;
import ptr.hf.helper.BaseHelper;
import ptr.hf.helper.ResourceHelper;
import ptr.hf.model.Reservation;
import ptr.hf.model.ReservationResponse;
import ptr.hf.model.Stations;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ptr.hf.helper.ResourceHelper.getStringResources;

public enum ApiHelper {
    INSTANCE;
    ChargerService chargerService;

    private RestService restService;

    ApiHelper() {
        //TODO remove in production (image_upload)
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        Interceptor stethoInterceptor = (Interceptor) new StethoInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-35-157-242-61.eu-central-1.compute.amazonaws.com:3000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        restService = retrofit.create(RestService.class);
        chargerService = retrofit.create(ChargerService.class);
    }

    public void getStations(Double latitude, Double longitude, Integer distance, Integer maxResult, final IApiResultListener<List<Station>> resultListener) {
        chargerService
//                .getStations(new StationRequest(true, false, latitude, longitude, distance, maxResult))
                .getStations(true, false, latitude, longitude, distance, maxResult)
                .enqueue(new RestCallback<>(resultListener));
    }

    public void postReservation(String chargerId, String userId, Integer from, Integer to, final IApiResultListener<ArrayList<ReservationResponse>> resultListener) {
        chargerService.postReservation(new ReservationRequest(chargerId, userId, Integer.toString(from), Integer.toString(to)))
                .enqueue(new RestCallback<>(resultListener));
    }

    public void getReservation(String chargerId, String userId, Integer from, Integer to, final IApiResultListener<ArrayList<ReservationResponse>> resultListener) {
        chargerService.postReservation(new ReservationRequest(chargerId, userId, Integer.toString(from), Integer.toString(to)))
                .enqueue(new RestCallback<>(resultListener));
    }

    public void deleteReservation(String chargerId, String userId, Integer from, Integer to, final IApiResultListener<ArrayList<ReservationResponse>> resultListener) {
        chargerService.postReservation(new ReservationRequest(chargerId, userId, Integer.toString(from), Integer.toString(to)))
                .enqueue(new RestCallback<>(resultListener));
    }

    public void putReservation(String chargerId, String userId, Integer from, Integer to, final IApiResultListener<ArrayList<ReservationResponse>> resultListener) {
        chargerService.postReservation(new ReservationRequest(chargerId, userId, Integer.toString(from), Integer.toString(to)))
                .enqueue(new RestCallback<>(resultListener));
    }

    public void getRoute(final IApiResultListener<DirectionsResult> resultListener) {
        Gson gson = new Gson();
        chargerService.getRoute((new RouteRequest(100000, new Start(47.4734695, 19.0595492), new Start(47.952203, 21.720380))))
                .enqueue(new RestCallback<>(resultListener));
    }

    public void getSettings(String userId, final IApiResultListener<UserSettings> resultListener) {
        chargerService.getSettings(userId).enqueue(new RestCallback<>(resultListener));
    }

    public void postSettings(UserSettings userSettings, final IApiResultListener<UserSettings> resultListener) {
        chargerService.postSettings(userSettings).enqueue(new RestCallback<>(resultListener));
    }


//    public void getStations(final IApiResultListener<ArrayList<Station>> resultListener) {
//        chargerService
//                .getStations("HU", 1000)
//                .enqueue(new RestCallback<>(resultListener));
//    }

//    public void postReservation(String chargerId, String userId, Integer from, Integer to, final IApiResultListener<ReservationResponse> resultListener) {
//        chargerService
//                .postReservation(chargerId, userId, from, to)
//                .enqueue(new RestCallback<>(resultListener));
//    }


}
