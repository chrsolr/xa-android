package io.keypunchers.xa.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.CommentListAdapter;
import io.keypunchers.xa.loaders.AchievementCommentsLoader;
import io.keypunchers.xa.loaders.SubmitCommentLoader;
import io.keypunchers.xa.misc.ApplicationClass;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.Enums;
import io.keypunchers.xa.misc.Singleton;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.Achievement;
import io.keypunchers.xa.models.Comment;

public class AchievementCommentsActivity extends AppCompatActivity {
    private Achievement mAchievement;
    private CommentListAdapter mAdapter;
    private RecyclerView mRvContent;
    private Snackbar mSnackbar;
    private Tracker mTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ach_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ApplicationClass application = (ApplicationClass) getApplication();
        mTracker = application.getDefaultTracker();

        if (getIntent().getExtras() != null) {
            mAchievement = getIntent().getExtras().getParcelable("ACHIEVEMENT");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mAchievement.getGameTitle());
        }

        ImageView mIvBanner = (ImageView) findViewById(R.id.iv_banner);

        VolleySingleton.getImageLoader()
                .get(mAchievement.getImageUrl(), ImageLoader.getImageListener(mIvBanner, 0, 0));

        TextView mTvAchTitle = (TextView) findViewById(R.id.tv_achievement_title);
        mTvAchTitle.setText(mAchievement.getTitle());

        TextView mTvAchDesc = (TextView) findViewById(R.id.tv_achievement_desc);
        mTvAchDesc.setText(mAchievement.getDescription());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRvContent = (RecyclerView) findViewById(R.id.rv_ach_comments);
        mRvContent.setLayoutManager(mLayoutManager);

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_ach_comments);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AchievementCommentsActivity.this);
                builder.setCancelable(true);

                @SuppressLint("InflateParams")
                View layout = getLayoutInflater().inflate(R.layout.dialog_comment, null, false);

                final EditText mInputComment = (EditText) layout.findViewById(R.id.et_comment);

                builder.setView(layout);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String comment = mInputComment.getText().toString();

                        if (comment.equals(""))
                            mInputComment.setError("Comment cannot be empty.");
                        else {
                            dialog.dismiss();
                            postComment(comment);
                            mSnackbar.setText("Submitting...");
                            mSnackbar.show();
                        }
                    }
                });
            }
        });

        if (!Singleton.getInstance().getUserProfile().isLogged()) {
            mFab.setVisibility(View.GONE);
        }

        mSnackbar = Common.makeSnackbar(this, mFab, null, Snackbar.LENGTH_INDEFINITE);

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

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(AchievementCommentsActivity.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void getAchievementComments() {
        LoaderManager.LoaderCallbacks mAchievementCommentsLoader = new LoaderManager.LoaderCallbacks<ArrayList<Comment>>() {

            @Override
            public Loader<ArrayList<Comment>> onCreateLoader(int id, Bundle args) {
                return new AchievementCommentsLoader(getApplicationContext(), String.format(Locale.US, "%s%s", Common.BASE_URL, mAchievement.getCommentsPageUrl()));
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Comment>> loader, ArrayList<Comment> data) {
                mAdapter = new CommentListAdapter(data);
                mRvContent.setAdapter(mAdapter);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Comment>> loader) {
            }
        };

        getSupportLoaderManager().restartLoader(0, null, mAchievementCommentsLoader);
    }

    private void postComment(final String comment) {
        getSupportLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Pair<Boolean, String>>() {

            @Override
            public Loader<Pair<Boolean, String>> onCreateLoader(int id, Bundle args) {
                return new SubmitCommentLoader(getApplicationContext(), Common.getAchievementId(mAchievement.getCommentsPageUrl()), comment, Enums.PostType.ACHIEVEMENTS);
            }

            @Override
            public void onLoadFinished(Loader<Pair<Boolean, String>> loader, Pair<Boolean, String> data) {
                if (data.first) {
                    mSnackbar.dismiss();
                    finish();
                    startActivity(getIntent());
                } else if (!data.second.equals("Cached")) {
                    mSnackbar.setText(data.second);
                    mSnackbar.setDuration(Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }
            }

            @Override
            public void onLoaderReset(Loader<Pair<Boolean, String>> loader) {
            }
        });
    }
}
