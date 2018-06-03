package splitsound.com.splitsound;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;


import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.UserListAdapter;

import splitsound.com.net.RTPNetworking;

import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import jlibrtp.*;


public class DrawerActivityTest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DrawerActivityTest";

    private boolean avSess = false;
    private RecyclerView sessRV;
    private RecyclerView userRV;

    private SlidingUpPanelLayout slideUp;

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
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            Log.e("Test", getBroadcastAddress());
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if(slideUp != null && (slideUp.getPanelState() == PanelState.EXPANDED || slideUp.getPanelState() == PanelState.ANCHORED))
            slideUp.setPanelState(PanelState.COLLAPSED);
        else {
            super.onBackPressed();
        }
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
        System.out.println(id);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.test, f).commit();

        /*
        if (id == R.id.available_sessions) {
            avSess = true;
            View b = findViewById(R.id.connect);
            b.setVisibility(View.GONE);
            View play_button = findViewById(R.id.main_play_button);
            play_button.setVisibility(View.GONE);
            sessRV.setVisibility(View.VISIBLE);
        }
        else if(id == R.id.settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            startActivity(startSettings);
        }
        else if(id == R.id.home_button){
            sessRV.setVisibility(View.GONE);
            View b = findViewById(R.id.connect);
            b.setVisibility(View.VISIBLE);
            View play_button = findViewById(R.id.main_play_button);
            play_button.setVisibility(View.VISIBLE);
        }
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
