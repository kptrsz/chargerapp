package ptr.hf.model;

import com.google.gson.annotations.SerializedName;

public class ReservationResponse {
    @SerializedName("success")
    Boolean success;

    @SerializedName("data")
    ReservationData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ReservationData getData() {
        return data;
    }

    public void setData(ReservationData data) {
        this.data = data;
    }
}
