package io.keypunchers.xa.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.fragments.AboutFragment;
import io.keypunchers.xa.fragments.ArticleListFragment;
import io.keypunchers.xa.fragments.GameListFragment;
import io.keypunchers.xa.fragments.LatestAchievementFragment;
import io.keypunchers.xa.fragments.LatestScreenshotsFragment;
import io.keypunchers.xa.fragments.SettingsFragment;
import io.keypunchers.xa.fragments.UpcomingGamesFragment;
import io.keypunchers.xa.loaders.LatestScreenshotsLoader;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.LatestScreenshot;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<LatestScreenshot>> {
    private int mDrawerCurrentSelectedPosition = 0;
    private boolean mIsDrawerLearned;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private SharedPreferences mPrefs;
    private ArrayList<LatestScreenshot> mBanners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mIsDrawerLearned = mPrefs.getBoolean(getString(R.string.DRAWER_LEARNED_TAG), false);

        mDrawerCurrentSelectedPosition = mPrefs.getInt(getString(R.string.DEFAULT_HOME_POSITION_TAG), 0);

        if (savedInstanceState != null) {
            mDrawerCurrentSelectedPosition = savedInstanceState.getInt("current_selected_position");
        }

        setupUI();

        makeNetworkCall(savedInstanceState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        mNavigationView.setCheckedItem(id);

        switch (id) {
            case R.id.nav_news:
                selectDrawerItem(0);
                break;
            case R.id.nav_previews:
                selectDrawerItem(1);
                break;
            case R.id.nav_interviews:
                selectDrawerItem(2);
                break;
            case R.id.nav_browse_games:
                selectDrawerItem(3);
                break;
            case R.id.nav_latest_achievements:
                selectDrawerItem(4);
                break;
            case R.id.nav_latest_screenshots:
                selectDrawerItem(5);
                break;
            case R.id.nav_upcoming_games:
                selectDrawerItem(6);
                break;
            case R.id.nav_setting:
                selectDrawerItem(7);
                break;
            case R.id.nav_about:
                selectDrawerItem(8);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.removeItem(R.id.main_menu_donate);
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
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<ArrayList<LatestScreenshot>> onCreateLoader(int id, Bundle args) {
        return new LatestScreenshotsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<LatestScreenshot>> loader, ArrayList<LatestScreenshot> data) {
        mBanners = data;

        Picasso.with(this)
                .load(mBanners.get(0).getImageUrl())
                .noFade()
                .into((ImageView) findViewById(R.id.iv_drawer_banner));

        TextView mTvTitle = (TextView) findViewById(R.id.tv_banner_title);
        mTvTitle.setText(mBanners.get(0).getTitle());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                selectDrawerItem(mDrawerCurrentSelectedPosition);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LatestScreenshot>> loader) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_selected_position", mDrawerCurrentSelectedPosition);
    }

    private void setupUI() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void makeNetworkCall(Bundle savedInstanceState) {
        int LOADER_ID = getResources().getInteger(R.integer.navigation_drawer_loader_id);

        if (getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, BaseActivity.this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, BaseActivity.this);
        }
    }

    private void selectDrawerItem(int position) {
        String FRAGMENT_TAG = "FRAGMENT_TAG_" + position;
        Bundle bundle = new Bundle();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        if (position == 0 && fragment == null) {
            bundle.putString("url", "http://www.xboxachievements.com/archive/gaming-news/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_news_title));
            bundle.putString("header_image_url", mBanners.get(1).getImageUrl());

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 1 && fragment == null) {
            bundle.putString("url", "http://www.xboxachievements.com/news/previews/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_previews_title));
            bundle.putString("header_image_url", mBanners.get(2).getImageUrl());

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 2 && fragment == null) {
            bundle.putString("url", "http://www.xboxachievements.com/news/interviews/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_interviews_title));
            bundle.putString("header_image_url", mBanners.get(3).getImageUrl());

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 3 && fragment == null) {
            bundle.putString("url", "http://www.xboxachievements.com/browsegames/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_games_title));

            fragment = new GameListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 4 && fragment == null) {
            bundle.putString("url", "http://www.xboxachievements.com/archive/achievements/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_latest_achievements_title));
            bundle.putString("header_image_url", mBanners.get(4).getImageUrl());

            fragment = new LatestAchievementFragment();
            fragment.setArguments(bundle);
        }

        if (position == 5 && fragment == null) {
            bundle.putString("ab_title", getResources().getString(R.string.ab_latest_screenshots_title));

            fragment = new LatestScreenshotsFragment();
            fragment.setArguments(bundle);
        }

        if (position == 6 && fragment == null) {
            bundle.putString("url", "http://www.xboxachievements.com/upcoming/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_upcoming_games_title));

            fragment = new UpcomingGamesFragment();
            fragment.setArguments(bundle);
        }

        if (position == 7 && fragment == null) {
            fragment = new SettingsFragment();
        }

        if (position == 8 && fragment == null) {
            fragment = new AboutFragment();
            fragment.setArguments(bundle);
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment, FRAGMENT_TAG)
                .commit();

        if (mIsDrawerLearned) {
            mDrawer.closeDrawer(GravityCompat.START);
        }

        if (!mIsDrawerLearned) {
            mDrawer.openDrawer(GravityCompat.START);
            mIsDrawerLearned = true;
            mPrefs.edit().putBoolean(getString(R.string.DRAWER_LEARNED_TAG), true).apply();
        }

        mDrawerCurrentSelectedPosition = position;
    }
}
