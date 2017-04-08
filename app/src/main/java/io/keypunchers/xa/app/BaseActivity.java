package io.keypunchers.xa.app;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.DrawerBannerLoader;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.DrawerBanner;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<DrawerBanner> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.main_layout, new NewsFragment()).commit();

        getData(savedInstanceState);

        navigationView.setCheckedItem(R.id.nav_news);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_news) {
            fragment = new NewsFragment();
        } else if (id == R.id.nav_games) {
            fragment = new GamesFragment();
        } else if (id == R.id.nav_latest_achievements) {
            fragment = new LatestAchievementFragment();
        } else if (id == R.id.nav_screenshots) {
            fragment = new LatestScreenshotsFragment();
        } else if (id == R.id.nav_upcoming_games) {
            fragment = new UpcomingGamesFragment();
        } else if (id == R.id.nav_setting) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment, fragment.getClass().getSimpleName())
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_menu_donate) {
            Toast.makeText(this, "Donate Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<DrawerBanner> onCreateLoader(int id, Bundle args) {
        return new DrawerBannerLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<DrawerBanner> loader, DrawerBanner data) {
        NetworkImageView mIvBannerImg = (NetworkImageView) findViewById(R.id.iv_drawer_banner);
        mIvBannerImg.setImageUrl(data.getImage(), SingletonVolley.getImageLoader());

        TextView mTvTitle = (TextView) findViewById(R.id.tv_banner_title);
        mTvTitle.setText(data.getTitle());
    }

    @Override
    public void onLoaderReset(Loader<DrawerBanner> loader) {

    }

    private void getData(Bundle savedInstanceState) {
        int LOADER_ID = getResources().getInteger(R.integer.navigation_drawer_loader_id);

        if (getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, BaseActivity.this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, BaseActivity.this);
        }
    }
}
