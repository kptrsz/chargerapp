package ptr.hf.network;

import java.util.ArrayList;
import java.util.Date;

import ptr.hf.model.Reservation;
import ptr.hf.model.ReservationResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChargerService {
    @GET("openchargemap/v2/poi/")
    Call<ArrayList<Station>> getStations(@Query("countrycode") String countryCode,
                                         @Query("maxresults") int maxResults);

    @POST("reservation/add")
    Call<ReservationResponse> postReservation(@Query("chargerId") String chargerId,
                                              @Query("userId") String userId,
                                              @Query("from") Integer from,
                                              @Query("to") Integer to);
}
