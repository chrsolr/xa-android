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

public class AchievementsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<GameDetails> {
    private GameDetails mData = new GameDetails();
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

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AchievementsListAdapter(getApplicationContext(), mAchievements, R.layout.row_achievements_square);
        mRvContent = (RecyclerView) findViewById(R.id.rv_game_achievements);
        mRvContent.setLayoutManager(mLinearLayoutManager);
        mRvContent.setAdapter(mAdapter);

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

        Collections.sort(mAchievements, new Comparator<Achievement>() {
            @Override
            public int compare(Achievement a, Achievement b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        VolleySingleton.getImageLoader().get(mAchievements.get(0).getImageUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();

                if (bitmap != null) {
                    if (bitmap.getWidth() < 100)
                        mAdapter = new AchievementsListAdapter(getApplicationContext(), mAchievements, R.layout.row_achievements_square);
                    else
                        mAdapter = new AchievementsListAdapter(getApplicationContext(), mAchievements, R.layout.row_achievements_wide);

                    setupUI();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setupUI() {
        ScaledNetworkImageView mIvBanner = (ScaledNetworkImageView) findViewById(R.id.iv_game_achievements_banner);
        NetworkImageView mIvGameCover = (NetworkImageView) findViewById(R.id.iv_game_achievements_cover);
        TextView mTvGameTitle = (TextView) findViewById(R.id.tv_game_ach_title);
        TextView mTvGameGenres = (TextView) findViewById(R.id.tv_game_ach_genres);
        TextView mTvAchAmount = (TextView) findViewById(R.id.tv_game_ach_amount);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mData.getTitle());

        mIvBanner.setImageUrl(mData.getBanner(), VolleySingleton.getImageLoader());
        mIvGameCover.setImageUrl(mData.getImageUrl(), VolleySingleton.getImageLoader());

        mTvGameTitle.setText(mData.getTitle());
        mTvGameGenres.setText(TextUtils.join("/", mData.getGenres()));
        mTvAchAmount.setText(String.format(Locale.US, "%s Achievements", mData.getAchievements().size()));

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
