package splitsound.com.splitsound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import splitsound.com.ui.adapters.RecyclerAdapter;
import splitsound.com.ui.adapters.UserListAdapter;


public class DrawerActivityTest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean avSess = false;
    private RecyclerView mRecyclerView;
    private RecyclerView uRecyclerView;

    View myView;
    boolean isUp = false;

    public static String helloWorld(){
        return "Hello World";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.server_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(new RecyclerAdapter());
        mRecyclerView.setVisibility(View.GONE);


        uRecyclerView = findViewById(R.id.user_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        uRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        uRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        uRecyclerView.setAdapter(new UserListAdapter());
        uRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(avSess)
        {
            NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
            onNavigationItemSelected(nav.getMenu().getItem(0));
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
            uRecyclerView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else if(id == R.id.settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            startActivity(startSettings);
        }
        else if(id == R.id.home_button){
            mRecyclerView.setVisibility(View.GONE);
            View b = findViewById(R.id.connect);
            b.setVisibility(View.VISIBLE);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clicked(View v)
    {
        if (isUp) {
            slideDown(uRecyclerView);
        } else {
            slideUp(uRecyclerView);
        }
        isUp = !isUp;
    }

    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(5000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(5000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}
