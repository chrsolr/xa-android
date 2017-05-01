package io.keypunchers.xa.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.CommentListAdapter;
import io.keypunchers.xa.loaders.AchievementCommentsLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.Achievement;
import io.keypunchers.xa.models.Comment;

public class AchievementCommentsActivity extends AppCompatActivity {
    private Achievement mAchievement;
    private CommentListAdapter mAdapter;
    private RecyclerView mRvContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ach_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            mAchievement = getIntent().getExtras().getParcelable("ACHIEVEMENT");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mAchievement.getGameTitle());
        }

        ImageView mIvBanner = (ImageView) findViewById(R.id.iv_banner);
        VolleySingleton.getImageLoader().get(mAchievement.getImageUrl(), ImageLoader.getImageListener(mIvBanner, 0, 0));

        TextView mTvAchTitle = (TextView) findViewById(R.id.tv_achievement_title);
        mTvAchTitle.setText(mAchievement.getTitle());

        TextView mTvAchDesc = (TextView) findViewById(R.id.tv_achievement_desc);
        mTvAchDesc.setText(mAchievement.getDescription());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRvContent = (RecyclerView) findViewById(R.id.rv_ach_comments);
        mRvContent.setLayoutManager(mLayoutManager);

        getAchievementComments();
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

    private void getAchievementComments() {
        LoaderManager.LoaderCallbacks mAchievementCommentsLoader = new LoaderManager.LoaderCallbacks<ArrayList<Comment>>() {

            @Override
            public Loader<ArrayList<Comment>> onCreateLoader(int id, Bundle args) {
                return new AchievementCommentsLoader(getApplicationContext(), String.format(Locale.US, "%s%s", Common.BASE_URL, mAchievement.getCommentsPageUrl()));
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Comment>> loader, ArrayList<Comment> data) {
                mAdapter = new CommentListAdapter(getApplicationContext(), data);
                mRvContent.setAdapter(mAdapter);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Comment>> loader) {
            }
        };

        getSupportLoaderManager().restartLoader(0, null, mAchievementCommentsLoader);
    }
}
