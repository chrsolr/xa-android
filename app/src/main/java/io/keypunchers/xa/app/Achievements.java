package io.keypunchers.xa.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.GameDetailsLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.models.GameDetails;
import io.keypunchers.xa.views.ScaledImageView;
import android.support.v7.widget.*;
import io.keypunchers.xa.adapters.*;
import java.util.*;
import io.keypunchers.xa.models.*;

import com.squareup.picasso.*;
import android.graphics.*;
import android.graphics.drawable.*;

public class Achievements extends AppCompatActivity implements LoaderManager.LoaderCallbacks<GameDetails> {
    private GameDetails mData = new GameDetails();
	private ArrayList<Achievement> mAchievements = new ArrayList<>();
    private String BASE_URL;
    private ScaledImageView mIvBanner;

	private RecyclerView mRvContent;

	private AchievementsListAdapter mAdapter;

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

		LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

		mRvContent = (RecyclerView) findViewById(R.id.rv_game_achievements);
		mRvContent.setAdapter(mAdapter);
		mRvContent.setLayoutManager(mLinearLayoutManager);

		if (mAchievements.isEmpty())
        	makeNetworkCall();
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
		mAchievements.addAll(data.getAchievements());
		
		Picasso.with(this)
			.load(mAchievements.get(0).getImageUrl())
			.into(new Target() {
				@Override
				public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
					
					if (bitmap.getWidth() < 100)
						mAdapter = new AchievementsListAdapter(getApplicationContext(), mAchievements, R.layout.row_achievements_square);
					else
						mAdapter = new AchievementsListAdapter(getApplicationContext(), mAchievements, R.layout.row_achievements_wide);
		
					setupUI();
				}

				@Override
				public void onBitmapFailed(Drawable errorDrawable) {

				}

				@Override
				public void onPrepareLoad(Drawable placeHolderDrawable) {

				}
			});
    }

	private void setupUI() {
		ScaledImageView mIvBanner = (ScaledImageView) findViewById(R.id.iv_game_achievements_banner);
        ImageView mIvGameCover = (ImageView) findViewById(R.id.iv_game_achievements_cover);
        TextView mTvGameTitle = (TextView) findViewById(R.id.tv_game_ach_title);
        TextView mTvGameGenres = (TextView) findViewById(R.id.tv_game_ach_genres);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mData.getTitle());

		Picasso.with(this)
			.load(mData.getBanner())
			.placeholder(R.drawable.promo_banner)
			.noFade()
			.into(mIvBanner);
		
		Picasso.with(this)
			.load(mData.getImageUrl())
			.noFade()
			.into(mIvGameCover);

        mTvGameTitle.setText(mData.getTitle());
        mTvGameGenres.setText(TextUtils.join("/", mData.getGenres()));
		
		mRvContent.setAdapter(mAdapter);
		mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mAchievements.size());
	}

    @Override
    public void onLoaderReset(Loader<GameDetails> loader) {

    }

    private void makeNetworkCall() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }
}
