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

    @SerializedName("id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
