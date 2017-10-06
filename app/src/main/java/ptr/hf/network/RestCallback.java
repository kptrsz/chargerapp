package ptr.hf.network;

import android.util.Log;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Abstract class of Retrofit's Callback
 * Provides a generic failure branch
 */
public class RestCallback<T> implements Callback<T> {
    protected IApiFinishedListener finishedListener;
    protected IApiResultListener<T> resultListener;

    public RestCallback(IApiFinishedListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    public RestCallback(IApiResultListener<T> resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.d("Response", response.toString());
        if (response.code() == 204) {
            success(response.body());
            return;
        }
        if (response.isSuccessful() && response.body() != null && resultListener != null || response.isSuccessful() && finishedListener != null) {
            success(response.body());
        } else if (response.code() >= 400 && response.errorBody() != null) {
            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
            if (errorResponse != null)
                error(errorResponse);
            else
                fail();
        } else {
            fail();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        // log all errors to Crashlytics
//        Crashlytics.getInstance().core.logException(error);

        Log.e("RestCallback", "Rest failure!", t);
        t.printStackTrace();
        fail();
    }

    private void success(T responseBody) {
        if (resultListener != null)
            resultListener.success(responseBody);
        if (finishedListener != null)
            finishedListener.success();
    }

    private void error(ErrorResponse errorResponse) {
        ErrorHandler.printError(errorResponse);

        if (resultListener != null)
            resultListener.error(errorResponse);
        if (finishedListener != null)
            finishedListener.error(errorResponse);
    }

    private void fail() {
        if (resultListener != null)
            resultListener.fail();
        if (finishedListener != null)
            finishedListener.fail();
    }
}
