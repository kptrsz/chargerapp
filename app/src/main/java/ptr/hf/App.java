package ptr.hf;

import android.app.Application;

import ptr.hf.helper.BaseHelper;

public class App  extends Application{
    private static boolean activityVisible = false;


    @Override
    public void onCreate() {
        super.onCreate();

        BaseHelper.initialize(this);
    }
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

}
