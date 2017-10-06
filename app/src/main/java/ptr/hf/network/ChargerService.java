package ptr.hf.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChargerService {
    @GET("poi/")
    Call<ArrayList<Station>> getStations(@Query("countrycode") String countryCode,
                                         @Query("maxresults") int maxResults);
}
