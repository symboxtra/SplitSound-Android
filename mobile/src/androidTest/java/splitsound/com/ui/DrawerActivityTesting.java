package splitsound.com.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import splitsound.com.main.DrawerActivityTest;
import splitsound.com.main.R;

// Common Espresso imports (Do not remove because auto-import does not work with Espresso)
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DrawerActivityTesting {

    @Rule
    public ActivityTestRule<DrawerActivityTest> rule = new ActivityTestRule<>(DrawerActivityTest.class);

    @Test
    public void scan_checkSwitch()
    {
        onView(withId(R.id.server_list_recycler_view)).check(matches(not(isDisplayed())));
    }
}
