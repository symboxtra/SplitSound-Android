package splitsound.com.splitsound;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.util.Log;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import splitsound.com.net.RTPNetworking;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Main Initial Activity
 *
 * @version 0.0.1
 * @author Emanuel, Neel
 * */
public class DrawerActivityTest extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "DrawerActivityTest";

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    public SlidingUpPanelLayout slideUp;

    private MediaBrowserCompat mediaBrowser;

    /**
     * Test function for unit testing purposes
     *
     * @return "Hello World"
     */
    public static String helloWorld()
    {
        return "Hello World";
    }

    /**
     * Executed when the application starts
     * the view is created
     *
     * @param savedInstanceState No clue what this is! ;P
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);

        SharedPreferences sp = getSharedPreferences("firstBool",MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroActivity.class); // Call the AppIntro java class
            startActivity(intent);
        }

        // Start main networking thread (RTPReciever, RTCPSender, RTCPReceiver)
        new Thread(new RTPNetworking(getBroadcastAddress())).start();
        Log.i("Broadcast Address", getBroadcastAddress());

        // Create custom toolbar and drawer functions
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Create the whole navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Ask Audio permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
    }


    /**
     * Executed when back button on device is pressed
     */
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if(slideUp != null && (slideUp.getPanelState() == PanelState.EXPANDED || slideUp.getPanelState() == PanelState.ANCHORED))
            slideUp.setPanelState(PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }

    /**
     * Executed when options on the toolbar are pressed
     *
     * @param item The item that is pressed on the toolbar
     * @return boolean based on successful actions performed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Executed when one of the navigation items are pressed
     *
     * @param item The item that was pressed
     * @return boolean based on successful actions performed
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        Fragment f = null;
        int id = item.getItemId();

        switch (id)
        {
            case R.id.available_sessions:
                f = new SessionsActivity();
                break;
            case R.id.settings:
                Intent startSettings = new Intent(this, SettingsActivity.class);
                startActivity(startSettings);
                break;
            case R.id.home_button:
                f = new HomeActivity();
                break;
        }

        // Open associating fragment/activity
        if(f != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.test, f).addToBackStack("test_fragment").commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Calculate the broadcast address of the network
     *
     * @return the broadcast address in String format
     */
    @NonNull
    public static String getBroadcastAddress()
    {
        try {
            System.setProperty("java.net.preferIPv4Stack", "true");
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface nif = interfaces.nextElement();
                if (!nif.isLoopback())
                    for (InterfaceAddress addr : nif.getInterfaceAddresses())
                        if (addr.getBroadcast() != null)
                            return addr.getBroadcast().toString().substring(1);


            }
        }catch(SocketException e) {}

        return "";
    }

    /**
     * Collapses the application toolbar for the slideup menu
     *
     * @return boolean whether successful
     */
    public boolean collapseBar(){
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        Log.d("HEIGHT", lp.height + "");
        lp.height = 0;
        appBarLayout.setLayoutParams(lp);
        return true;
    }

    /**
     * Adds back the application toolbar for the slideup menu
     *
     * @return boolean whether successful
     */
    public boolean unCollapseBar(){
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        Log.d("HEIGHT", lp.height + "");
        lp.height = -2;
        appBarLayout.setLayoutParams(lp);
        return false;
    }

    /**
     * Requests the user for approving permissions
     *
     * @param requestCode permission code
     * @param permissions List of permissions requested
     * @param grantResults Results provided for the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // do nothing and wait
                }
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
}
