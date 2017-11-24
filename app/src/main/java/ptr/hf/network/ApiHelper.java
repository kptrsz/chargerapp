package ptr.hf.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
//                .addNetworkInterceptor(interceptor)   //TODO remove in production
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
//                .addInterceptor(new MyInterceptor())
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

    public void getStations(Double latitude, Double longitude, Integer distance, Integer maxResult, final IApiResultListener<ArrayList<Station>> resultListener) {
        chargerService
                .getStations(true, false, latitude, longitude, distance, maxResult)
                .enqueue(new RestCallback<>(resultListener));
    }

    public void postReservation(String chargerId, String userId, Integer from, Integer to, final IApiResultListener<ArrayList<ReservationResponse>> resultListener) {
        chargerService.postReservation(chargerId, userId, Integer.toString(from), Integer.toString(to)).enqueue(new RestCallback<>(resultListener));
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
