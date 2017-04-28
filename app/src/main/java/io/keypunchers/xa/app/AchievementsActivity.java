package io.keypunchers.xa.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.AchievementsListAdapter;
import io.keypunchers.xa.loaders.GameDetailsLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.Achievement;
import io.keypunchers.xa.models.GameDetails;
import io.keypunchers.xa.views.ScaledImageView;
import io.keypunchers.xa.views.ScaledNetworkImageView;
import com.squareup.picasso.Picasso.*;
import android.graphics.drawable.*;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import io.keypunchers.xa.adapters.ViewPagerAdapter;
import io.keypunchers.xa.fragments.AchievementsFragment;
import android.support.v4.content.AsyncTaskLoader;
import io.keypunchers.xa.models.Screenshots;
import io.keypunchers.xa.loaders.ScreenshotsLoader;
import io.keypunchers.xa.fragments.ImageListFragment;

public class AchievementsActivity extends AppCompatActivity {
    private GameDetails mData;
    private String BASE_URL;
	private String GAME_PERMALINK;

	private ViewPagerAdapter mAdapter;

	private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_achievements);
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
			getGameScreenshots();
			getGameAchievements();
		}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
	
	private void getGameAchievements(){
		LoaderManager.LoaderCallbacks mGameAchievementsLoader = new LoaderManager.LoaderCallbacks<GameDetails>(){

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
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLoaderReset(Loader<GameDetails> loader) {
			}
		};
		
		getSupportLoaderManager().restartLoader(0, null, mGameAchievementsLoader);
	}
	
	private void getGameScreenshots(){
		LoaderManager.LoaderCallbacks mGameScreenshotsLoader = new LoaderManager.LoaderCallbacks<Screenshots>(){

			@Override
			public Loader<Screenshots> onCreateLoader(int id, Bundle args) {
				return new ScreenshotsLoader(getApplicationContext(), Common.getGameScreenshotsUrlByPermalink(GAME_PERMALINK, 1));
			}

			@Override
			public void onLoadFinished(Loader<Screenshots> loader, Screenshots data) {
				mAdapter.addFragment(new ImageListFragment().newInstance(data.getImageUrls()), "Screenshots");
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLoaderReset(Loader<Screenshots> loader) {
			}
		};
		
		getSupportLoaderManager().restartLoader(1, null, mGameScreenshotsLoader);
	}
}
