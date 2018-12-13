package me.christiansoler.xa.app;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.fragments.AboutFragment;
import me.christiansoler.xa.fragments.ArticleListFragment;
import me.christiansoler.xa.fragments.GameListFragment;
import me.christiansoler.xa.fragments.LatestAchievementFragment;
import me.christiansoler.xa.fragments.LatestScreenshotsFragment;
import me.christiansoler.xa.fragments.SettingsFragment;
import me.christiansoler.xa.fragments.UpcomingGamesFragment;
import me.christiansoler.xa.loaders.LatestScreenshotsLoader;
import me.christiansoler.xa.misc.VolleySingleton;
import me.christiansoler.xa.models.LatestScreenshot;
import me.christiansoler.xa.misc.*;
import me.christiansoler.xa.models.*;
import android.text.*;
import me.christiansoler.xa.fragments.*;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<LatestScreenshot>> {
    private int mDrawerCurrentSelectedPosition = 0;
    private boolean mIsDrawerLearned;
	private boolean isLoggedIn = Singleton.getInstance().getUserProfile().isLogged();
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
			case R.id.nav_login:
                selectDrawerItem(9);
				item.setTitle(isLoggedIn ? "Logout" : "Login");
                break;
        }
		
		invalidateOptionsMenu();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.menu_item_search);
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint("Search Games");

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
				String FRAGMENT_TAG = "FRAGMENT_TAG_SEARCH";
				Bundle bundle = new Bundle();
				Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

				if (fragment == null) {
					bundle.putString("search_query", query);
					bundle.putString("ab_title", "Search Result");
					fragment = new SearchResultFragment();
					fragment.setArguments(bundle);
				}
				
				if (fragment != null) {
					getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
					getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.main_layout, fragment, FRAGMENT_TAG)
						.addToBackStack(null)
						.commit();
				}
				
				(menu.findItem(R.id.menu_item_search)).collapseActionView();
				
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        VolleySingleton
                .getImageLoader()
                .get(mBanners.get(0).getImageUrl(),
                        ImageLoader.getImageListener((ImageView) findViewById(R.id.iv_drawer_banner), 0, 0));

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
		
		Menu menu = mNavigationView.getMenu();
		MenuItem nav_login = menu.findItem(R.id.nav_login);
		nav_login.setTitle(isLoggedIn ? "Logout" : "Login");
		
		
		
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
            bundle.putString("url", Common.BASE_URL + "/archive/gaming-news/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_news_title));
            bundle.putParcelable("header", mBanners.get(1));

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 1 && fragment == null) {
            bundle.putString("url", Common.BASE_URL + "/news/previews/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_previews_title));
            bundle.putParcelable("header", mBanners.get(2));

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 2 && fragment == null) {
            bundle.putString("url", Common.BASE_URL + "/news/interviews/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_interviews_title));
            bundle.putParcelable("header", mBanners.get(3));

            fragment = new ArticleListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 3 && fragment == null) {
            bundle.putString("url", Common.BASE_URL + "/browsegames/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_games_title));

            fragment = new GameListFragment();
            fragment.setArguments(bundle);
        }

        if (position == 4 && fragment == null) {
            bundle.putString("url", Common.BASE_URL + "/archive/achievements/");
            bundle.putString("ab_title", getResources().getString(R.string.ab_latest_achievements_title));
            bundle.putParcelable("header", mBanners.get(4));

            fragment = new LatestAchievementFragment();
            fragment.setArguments(bundle);
        }

        if (position == 5 && fragment == null) {
            bundle.putString("ab_title", getResources().getString(R.string.ab_latest_screenshots_title));

            fragment = new LatestScreenshotsFragment();
            fragment.setArguments(bundle);
        }

        if (position == 6 && fragment == null) {
            bundle.putString("url", Common.BASE_URL + "/upcoming/");
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
		
		if (position == 9) {
			if (isLoggedIn) {
				Singleton.getInstance().destroyUserProfile();
				isLoggedIn = false;
			} else {
				setupUserCredentials();
			}
		}
		
		if (fragment != null) {
			getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

			getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment, FRAGMENT_TAG)
                .commit();
		}

        if (mIsDrawerLearned) {
            mDrawer.closeDrawer(GravityCompat.START);
        }

        if (!mIsDrawerLearned) {
            mIsDrawerLearned = true;
            mPrefs.edit().putBoolean(getString(R.string.DRAWER_LEARNED_TAG), true).apply();
			mDrawer.openDrawer(GravityCompat.START);
        }

        mDrawerCurrentSelectedPosition = position;
    }
	
	private void setupUserCredentials() {
        final UserProfile profile = Singleton.getInstance().getUserProfile();
		Resources mResources = this.getResources();
		final String XA_USERNAME = mResources.getString(R.string.XA_USERNAME);
        final String XA_PASSWORD = mResources.getString(R.string.XA_PASSWORD);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);

		View layout = this.getLayoutInflater().inflate(R.layout.dialog_credentials, null, false);

		final EditText mInputUsername = (EditText) layout.findViewById(R.id.et_username);
		final EditText mInputPassword = (EditText) layout.findViewById(R.id.et_password);

		mInputUsername.setText(profile.getUsername());
		mInputPassword.setText(profile.getPassword());

		builder.setView(layout);
		
		builder.setPositiveButton("Save",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});

		builder.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});

		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					String username = mInputUsername.getText().toString();
					String password = mInputPassword.getText().toString();

					if (username.equals(""))
						mInputUsername.setError("Username cannot be empty");
					else if (password.equals(""))
						mInputPassword.setError("Password cannot be empty");
					else {
						mPrefs.edit()
							.putString(XA_USERNAME, username)
							.putString(XA_PASSWORD, password)
							.apply();

						profile.setUsername(username);
						profile.setPassword(password);

						Singleton.getInstance().setUserProfile(profile);
						
						isLoggedIn = true;

						Menu menu = mNavigationView.getMenu();
						MenuItem nav_login = menu.findItem(R.id.nav_login);
						nav_login.setTitle(isLoggedIn ? "Logout" : "Login");
						
						
						dialog.dismiss();
					}

				}
			});

    }
}
