package splitsound.com.splitsound;

import android.app.Application;
import android.content.Context;

/**
 * Created by Neel on 6/12/2018.
 */

public class SplitSoundApplication extends Application
{
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
