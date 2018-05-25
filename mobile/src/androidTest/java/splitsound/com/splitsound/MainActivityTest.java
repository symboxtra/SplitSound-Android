package splitsound.com.splitsound;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

// Common Espresso imports (Do not remove because auto-import does not work with Espresso)
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
}
