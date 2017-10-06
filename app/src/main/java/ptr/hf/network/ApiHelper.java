package ptr.hf.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import ptr.hf.R;
import ptr.hf.helper.BaseHelper;
import ptr.hf.helper.ResourceHelper;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ApiHelper {
    INSTANCE;
    ChargerService chargerService;

    private RestService restService;

    ApiHelper() {
        //TODO remove in production (image_upload)
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)   //TODO remove in production
//                .addInterceptor(new MyInterceptor())
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ResourceHelper.getStringResources(R.string.config_rest_api_url))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        restService = retrofit.create(RestService.class);
    }


//    public void login(final boolean showDialog, final LoginRequest loginRequest) {
//        showProgressDialog(showDialog);
//        eCarService.login(loginRequest).enqueue(new RestCallback<>(UserController.getInstance().getLoginResultListener()));
//    }

    public void getStations() {
        chargerService.getStations("HU", 1000).enqueue(new RestCallback<>(MapController.getInstance().getStationResultListener()));
    }
}
