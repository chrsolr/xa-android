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

import io.keypunchers.xa.R;
import io.keypunchers.xa.fragments.ArticleListFragment;
import io.keypunchers.xa.fragments.ScreenshotsFragment;
import io.keypunchers.xa.loaders.DrawerBannerLoader;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.DrawerBanner;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<DrawerBanner> {
    private int mDrawerCurrentSelectedPosition = 0;
    private int LOADER_ID;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if(savedInstanceState != null) {
            mDrawerCurrentSelectedPosition = savedInstanceState.getInt("current_selected_position");
        }

        setupUI();

        getData(savedInstanceState);
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
            case R.id.nav_games:
                selectDrawerItem(3);
                break;
            case R.id.nav_latest_achievements:
                selectDrawerItem(4);
                break;
            case R.id.nav_screenshots:
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
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_selected_position", mDrawerCurrentSelectedPosition);
    }

    private void setupUI() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        selectDrawerItem(mDrawerCurrentSelectedPosition);
    }

    private void getData(Bundle savedInstanceState) {
        LOADER_ID = getResources().getInteger(R.integer.navigation_drawer_loader_id);

        if (getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, BaseActivity.this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, BaseActivity.this);
        }
    }

    private void selectDrawerItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        if (position == 0) {
            bundle.putString("url", "http://www.xboxachievements.com/archive/gaming-news/1/");
            bundle.putInt("counter", 3);
            bundle.putString("ab_title", getResources().getString(R.string.ab_news_title));

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 1) {
            bundle.putString("url", "http://www.xboxachievements.com/news/previews/1/");
            bundle.putInt("counter", 2);
            bundle.putString("ab_title", getResources().getString(R.string.ab_previews_title));

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 2) {
            bundle.putString("url", "http://www.xboxachievements.com/news/interviews/1/");
            bundle.putInt("counter", 2);
            bundle.putString("ab_title", getResources().getString(R.string.ab_interviews_title));

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 5) {
            bundle.putString("url", "http://www.xboxachievements.com/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_screenshots_title));

            fragment = new ScreenshotsFragment();
            fragment.setArguments(bundle);
        }

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment, fragment.getClass().getSimpleName())
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        mDrawerCurrentSelectedPosition = position;
    }
}
