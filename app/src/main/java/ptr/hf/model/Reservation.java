package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Reservation {
    @SerializedName("chargerId")
    String chargerId;

    @SerializedName("userId")
    String userId;

    @SerializedName("from")
    Date from;

    @SerializedName("to")
    Date to;

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
