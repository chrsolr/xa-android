package io.keypunchers.xa.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
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
import io.keypunchers.xa.fragments.ScreenshotsFragment;

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
			getGameAchievements();
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_article, menu);
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
