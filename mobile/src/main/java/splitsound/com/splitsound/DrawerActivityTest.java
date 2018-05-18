package splitsound.com.splitsound;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.UserListAdapter;


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

        sessRV = findViewById(R.id.server_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        sessRV.setHasFixedSize(true);

        // use a linear layout manager
        sessRV.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        sessRV.setAdapter(new RecyclerAdapter());
        sessRV.setVisibility(View.GONE);


        userRV = findViewById(R.id.user_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        userRV.setHasFixedSize(true);

        // use a linear layout manager
        userRV.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        userRV.setAdapter(new UserListAdapter());

        // Setup sliding panel action listeners
        slideUp = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slideUp.addPanelSlideListener(new PanelSlideListener(){
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

                ImageView arrowIcon = (ImageView)findViewById(R.id.arrow_icon);
                TextView swipe = (TextView)findViewById(R.id.swipe);

                if(newState == PanelState.EXPANDED)
                {
                    arrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    swipe.setText(R.string.close_drawer);
                }
                else if(newState == PanelState.COLLAPSED)
                {
                    arrowIcon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    swipe.setText(R.string.open_drawer);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else if(slideUp != null && (slideUp.getPanelState() == PanelState.EXPANDED || slideUp.getPanelState() == PanelState.ANCHORED))
            slideUp.setPanelState(PanelState.COLLAPSED);
        else if(avSess)
        {
            NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
            onNavigationItemSelected(nav.getMenu().getItem(0));
            avSess = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_activity_test, menu);
        return true;
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
        int id = item.getItemId();
        System.out.println(id);
        if (id == R.id.available_sessions) {
            avSess = true;
            View b = findViewById(R.id.connect);
            b.setVisibility(View.GONE);
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
