package splitsound.com.splitsound;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.common.api.internal.ApiExceptionMapper;

// Common Espresso imports (Do not remove because auto-import does not work with Espresso)
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.contrib.DrawerActions.*;
import static android.support.test.espresso.action.ViewActions.click;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.DatagramSocket;

import jlibrtp.Participant;
import jlibrtp.RTPSession;
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


    @Test
    public void perform_all_ui() throws InterruptedException
    {
        // Test settings tab
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.settings));

        // Test username dialog
        //onData(anything()).inAdapterView(withId(R.id.userSettings)).atPosition(0).perform(click());
        //Thread.sleep(100);
        onView(isRoot()).perform(pressBack());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.home_button));

        // Test user list
        onView(withId(R.id.sliding_layout)).perform(swipeUp());
        //Thread.sleep(100);
        //onView(withId(R.id.sliding_layout)).perform(swipeDown());

        // Test available sessions tab
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.available_sessions));
        Thread.sleep(10000);

        // Test settings tab
        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        //onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.settings));

        // Test username dialog
        //onData(anything()).inAdapterView(withId(R.id.userSettings)).atPosition(0).perform(click());
        //Thread.sleep(100);
        //onView(isRoot()).perform(pressBack());
    }

    /*
    @Test
    public void test_quick_maths()
    {
        assertEquals(2 + 2, 4);
    }


    @Test
    public void check_play_button()
    {
        // Check if main play button exists
        onView(withId(R.id.main_play_button)).check(matches(isDisplayed()));
    }

    @Test
    public void test_settings_activity()
    {
        // Perform activity change using navigation drawer

        // Open drawer and Settings activity
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.settings));
    }

    @Test
    public void swipe_test_user_list()
    {
        // Swipe up to open user list
        onView(withId(R.id.sliding_layout)).perform(swipeUp());
        onView(withId(R.id.sliding_layout)).perform(swipeDown());
    }

    @Test
    public void check_available_sessions() throws InterruptedException
    {
        //Open available sessions tab
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.available_sessions));
        Thread.sleep(100);
        onView(isRoot()).perform(pressBack());
    }

    @Test
    public void getPackets() throws Exception
    {
        // Create datagram ports for RTP and RTCP communication
        DatagramSocket rtpSocket = null;
        DatagramSocket rtcpSocket = null;
        try {
            rtpSocket = new DatagramSocket(8001);
            rtcpSocket = new DatagramSocket(6001);
        }catch(Exception e)
        {
            Log.e("Datagram Socket", "RTPSession failed to obtain port");
            Log.e("Error: ", e.toString());
        }

        RTPSessionTask sessionTask = new RTPSessionTask();

        // Create the RTP session and setup RTP and RTCP channels
        RTPSession sess = new RTPSession(rtpSocket, rtcpSocket);
        sess.naivePktReception(true);
        sess.RTPSessionRegister(sessionTask,null, null);
        Participant p = new Participant("127.0.0.1", 8000, 6005);
        sess.addParticipant(p);

        for(int i = 0;i < 5;i++)
        {
            sess.sendData("Test Hi".getBytes());
        }
    }
   */
}
