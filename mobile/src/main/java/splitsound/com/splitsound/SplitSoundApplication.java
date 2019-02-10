package splitsound.com.splitsound;

import android.app.Application;
import android.content.Context;

/**
 * SplitSound Context Class
 *
 * @version 0.0.1
 * @author Neel
 */
public class SplitSoundApplication extends Application
{
    private static Context context;

    /**
     * Executed when the application starts
     * the view is created
     */
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * Returns the context of this application
     */
    public static Context getAppContext() {
        return context;
    }
}
