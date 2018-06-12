package splitsound.com.splitsound;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;


import jp.wasabeef.blurry.Blurry;
import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.UserListAdapter;
import splitsound.com.net.RTPNetworking;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DrawerActivityTest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "DrawerActivityTest";

    private boolean avSess = false;
    private RecyclerView sessRV;
    private RecyclerView userRV;

    private SlidingUpPanelLayout slideUp;

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    View myView;
    boolean isUp = false;

    public static String helloWorld()
    {
        return "Hello World";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);
        
        // Start networking thread (RTPReciever, RTCPSender, RTCPReceiver)
        try {
            new Thread(new RTPNetworking(getBroadcastAddress())).start();
            Log.i("Broadcast Address", getBroadcastAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Create custom toolbar and drawer functions
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Ask Audio permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if(slideUp != null && (slideUp.getPanelState() == PanelState.EXPANDED || slideUp.getPanelState() == PanelState.ANCHORED))
            slideUp.setPanelState(PanelState.COLLAPSED);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
        if(f != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.test, f).addToBackStack("test_fragment").commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public static String getBroadcastAddress() throws SocketException
    {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for(Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); )
        {
            NetworkInterface nif = interfaces.nextElement();
            if( !nif.isLoopback() )
            {
                for( InterfaceAddress addr : nif.getInterfaceAddresses() )
                {
                    if(addr.getBroadcast() != null)
                        return addr.getBroadcast().toString().substring(1);
                }
            }
        }
        return "";
    }
    public boolean collapseBar(){
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        Log.d("HEIGHT", lp.height + "");
        lp.height = 0;
        appBarLayout.setLayoutParams(lp);
        return true;
    }

    public boolean unCollapseBar(){
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        Log.d("HEIGHT", lp.height + "");
        lp.height = -2;
        appBarLayout.setLayoutParams(lp);
        return false;
    }

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
}
