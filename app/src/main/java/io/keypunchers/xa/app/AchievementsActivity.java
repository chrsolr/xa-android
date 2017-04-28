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

public class AchievementsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<GameDetails> {
    private GameDetails mData;
    private ArrayList<Achievement> mAchievements = new ArrayList<>();
    private String BASE_URL;
    private RecyclerView mRvContent;
    private AchievementsListAdapter mAdapter;
    private Target mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            String gamePermalink = getIntent().getExtras().getString("game_permalink");
            BASE_URL = Common.getGameAchievementsUrlByPermalink(gamePermalink);
        }
		
		if (mData == null) {
			mData = new GameDetails();
			makeNetworkCall();
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

    @Override
    public Loader<GameDetails> onCreateLoader(int id, Bundle args) {
        return new GameDetailsLoader(this, BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<GameDetails> loader, GameDetails data) {
        mData = data;

        Collections.sort(mData.getAchievements(), new Comparator<Achievement>() {
				@Override
				public int compare(Achievement a, Achievement b) {
					return a.getTitle().compareTo(b.getTitle());
				}
			});

		setupUI();
    }



    @Override
    public void onLoaderReset(Loader<GameDetails> loader) {

    }

    private void makeNetworkCall() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

	private void setupUI() {
		if (getSupportActionBar() != null)
			getSupportActionBar().setTitle(mData.getTitle());
		
		ViewPager mViewPager = (ViewPager) findViewById(R.id.vp_achievements);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_achievements);
        tabLayout.setupWithViewPager(mViewPager);

		ViewPagerAdapter mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mAdapter.addFragment(new AchievementsFragment().newInstance(mData), "Achievements");
		
		mViewPager.setAdapter(mAdapter);
	}
}
