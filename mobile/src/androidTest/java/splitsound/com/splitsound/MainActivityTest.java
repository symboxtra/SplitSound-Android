package splitsound.com.splitsound;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.internal.ApiExceptionMapper;

// Common Espresso imports (Do not remove because auto-import does not work with Espresso)
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.contrib.DrawerActions.*;
import static android.support.test.espresso.action.ViewActions.click;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.DatagramSocket;
import java.util.concurrent.TimeoutException;

import jlibrtp.Participant;
import jlibrtp.RTPSession;
import splitsound.com.audio.controls.AudioTrackService;
import splitsound.com.net.RTCPReceiverTask;
import splitsound.com.net.RTPSessionTask;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<DrawerActivityTest> rule = new ActivityTestRule<>(DrawerActivityTest.class);


    // TODO: Fix this after new Google bug update
    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO);

    @Rule
    public final ServiceTestRule serviceRule = new ServiceTestRule();


    // Grant permissions
    @Before
    public void grantPhonePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector().text("ALLOW"));
            Log.d("Test", "SDK greater than 23");
            if (allowPermissions.exists()) {
                Log.d("Test", "I exist");
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.e(e.toString(), "There is no permissions dialog to interact with ");
                }
            }
        }
    }

    @Test
    public void perform_all_ui() throws InterruptedException, TimeoutException
    {

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject entry = device.findObject(new UiSelector().text("DONE"));
        if(entry.exists()) {
            try {
                entry.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Test settings tab
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.settings));

        // Test username dialog
        onData(anything()).inAdapterView(withId(R.id.userSettings)).atPosition(0).perform(click());
        //Thread.sleep(100);
        onView(isRoot()).perform(pressBack());
        onView(isRoot()).perform(pressBack());
        onView(isRoot()).perform(pressBack());
        //onView(withContentDescription("Navigate up")).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.home_button));

        // Test play button
        onView(withId(R.id.connect)).perform(click());
        Thread.sleep(100);
        onView(withId(R.id.connect)).perform(click());

        // Test user list swipe up
        onView(withId(R.id.sliding_layout)).perform(swipeUp());
        Thread.sleep(100);

        // Test user list swipe down
        onView(withId(R.id.sliding_layout)).perform(swipeDown());

        // Test available sessions tab
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.available_sessions));
        Thread.sleep(10000);

        // Test joining session
        onView(withId(R.id.server_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(100);
        onView(isRoot()).perform(pressBack());

        // Test refresh
        onView(withId(R.id.swipeRefreshLayout)).perform(swipeDown());
        Thread.sleep(5000);
        onView(withId(R.id.action_refresh)).perform(click());
        Thread.sleep(5000);

        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), AudioTrackService.class);

        IBinder bind = serviceRule.bindService(serviceIntent);

        AudioTrackService serv = ((AudioTrackService.LocalBinder)bind).getService();

        serv.playMedia();
        serv.pauseMedia();
        serv.resumeMedia();
        serv.stopMedia();

        serv.onAudioFocusChange(AudioManager.AUDIOFOCUS_GAIN);
        serv.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT);
        serv.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK);
        serv.onAudioFocusChange(AudioManager.AUDIOFOCUS_LOSS);

        serv.onDestroy();
    }

    @Test
    public void testSomething()
    {
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), AudioTrackService.class);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }
}
