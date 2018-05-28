package splitsound.com.splitsound;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        assertEquals(4, 2 + 2);
    }
}