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
    Call<ArrayList<Station>> getStations(
            @Query("compact") Boolean compact,
            @Query("verbose") Boolean verbose,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude,
            @Query("distance") Integer distance,
//            @Query("countrycode") String countryCode,
            @Query("maxresults") int maxResults);

    @GET("openchargemap/v2/poi/")
    Call<ArrayList<Station>> getStationsHU(
            @Query("countrycode") String countryCode,
            @Query("maxresults") int maxResults);

    @POST("reservation/add")
    Call<Void> postReservation(@Query("chargerId") String chargerId,
                               @Query("userId") String userId,
                               @Query("from") String from,
                               @Query("to") String to);
}
