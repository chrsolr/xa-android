package me.christiansoler.xa.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.christiansoler.xa.R;
import me.christiansoler.xa.adapters.ViewPagerAdapter;
import me.christiansoler.xa.fragments.AchievementsFragment;
import me.christiansoler.xa.fragments.ScreenshotsFragment;
import me.christiansoler.xa.loaders.GameDetailsLoader;
import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.models.GameDetails;
import android.support.design.widget.*;

public class AchievementsActivity extends AppCompatActivity {
    private GameDetails mData;
    private String BASE_URL;
    private String GAME_PERMALINK;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_achievements);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_achievements);
        tabLayout.setupWithViewPager(mViewPager);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            GAME_PERMALINK = getIntent().getExtras().getString("game_permalink");
            BASE_URL = Common.getGameAchievementsUrlByPermalink(GAME_PERMALINK);
        }

        if (mData == null) {
            mData = new GameDetails();
            getGameAchievements();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_detials, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_item_open_in_browser:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                startActivity(intent);
                break;
            case R.id.menu_item_share:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check it out!");
                intent.putExtra(Intent.EXTRA_TEXT, "Check it out! " + mData.getTitle() + "\n\n" + Common.getGameAchievementsUrlByPermalink(GAME_PERMALINK) + "\n\n" + "Shared via XA App.");
                startActivity(Intent.createChooser(intent, "Share"));
				break;
			case R.id.menu_item_add_to_favorites:
				Common.makeSnackbar(this, "Added to favorites!!!", Snackbar.LENGTH_SHORT).show();
				break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOCATION), AchievementsActivity.class.getSimpleName());
    }

    private void getGameAchievements() {
        LoaderManager.LoaderCallbacks mGameAchievementsLoader = new LoaderManager.LoaderCallbacks<GameDetails>() {

            @Override
            public Loader<GameDetails> onCreateLoader(int id, Bundle args) {
                return new GameDetailsLoader(getApplicationContext(), BASE_URL);
            }

            @Override
            public void onLoadFinished(Loader<GameDetails> loader, GameDetails data) {
                mData = data;

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(mData.getTitle());

                mAdapter.addFragment(new AchievementsFragment().newInstance(mData), "Achievements");

                if (mData.hasScreenshots())
                    mAdapter.addFragment(new ScreenshotsFragment().newInstance(GAME_PERMALINK), "Screenshots");

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<GameDetails> loader) {
            }
        };

        getSupportLoaderManager().restartLoader(0, null, mGameAchievementsLoader);
    }
}
