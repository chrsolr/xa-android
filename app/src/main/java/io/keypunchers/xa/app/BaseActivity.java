package io.keypunchers.xa.app;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.NavigationDrawerAdapter;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String PREF_USER_LEARNED_DRAWER = "USER_LEARNED_DRAWER";
    private final String STATE_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private boolean mUserLearnedDrawer = false;
    private int mCurrentSelectedPosition = 2;
    private String[] mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean displayMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().getThemedContext();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = prefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (savedInstanceState != null) {
            //TODO: Select fragment
        }


        showOrHideMenu(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void showOrHideMenu(boolean display) {
        displayMenu = display;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
