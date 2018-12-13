package me.christiansoler.xa.app;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import me.christiansoler.xa.R;
import me.christiansoler.xa.adapters.ViewPagerAdapter;
import me.christiansoler.xa.fragments.ArticleFragment;
import me.christiansoler.xa.fragments.CommentListFragment;
import me.christiansoler.xa.fragments.ImageListFragment;
import me.christiansoler.xa.fragments.VideoListFragment;
import me.christiansoler.xa.loaders.ArticleLoader;
import me.christiansoler.xa.loaders.SubmitCommentLoader;
import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.misc.Enums;
import me.christiansoler.xa.misc.Singleton;
import me.christiansoler.xa.models.Article;

public class ArticleActivity extends AppCompatActivity {
    private String BASE_URL;
    private Article mData;
    private String TAG = ArticleActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.vp_article);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tl_article);
        mTabLayout.setupWithViewPager(mViewPager);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.ab_article);
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("url"))
            BASE_URL = getIntent().getExtras().getString("url");

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_article);
        mFab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);
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
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
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

                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.POST_COMMENT), Singleton.getInstance().getUserProfile().getUsername());
                    }
                });
            }
        });

        if (!Singleton.getInstance().getUserProfile().isLogged()) {
            mFab.setVisibility(View.GONE);
        }

        mSnackbar = Common.makeSnackbar(this, mFab, null, Snackbar.LENGTH_INDEFINITE);

        getArticle();
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
                intent.putExtra(Intent.EXTRA_TEXT, "Check it out! " + mData.getHeaderTitle() + "\n\n" + BASE_URL.replace(".html", "") + "\n\n" + "Shared via XA App.");
                startActivity(Intent.createChooser(intent, "Share"));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TAG, mData);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOCATION), ArticleActivity.class.getSimpleName());
    }

    private void setupUI() {
        mAdapter.addFragment(new ArticleFragment().newInstance(mData), "Article");

        if (mData.getImageUrls().size() > 1)
            mAdapter.addFragment(new ImageListFragment().newInstance(mData.getImageUrls()), "Images");

        if (mData.getVideoUrls().size() > 0)
            mAdapter.addFragment(new VideoListFragment().newInstance(mData.getVideoUrls()), "Videos");

        if (mData.getComments().size() > 0)
            mAdapter.addFragment(new CommentListFragment().newInstance(mData.getComments()), "Comments");

        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void postComment(final String comment) {
        getSupportLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Pair<Boolean, String>>() {

            @Override
            public Loader<Pair<Boolean, String>> onCreateLoader(int id, Bundle args) {
                return new SubmitCommentLoader(getApplicationContext(), Common.getNewsCommentstId(BASE_URL), comment, Enums.PostType.ARTICLE);
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

    private void getArticle() {
        getSupportLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<Article>() {

            @Override
            public Loader<Article> onCreateLoader(int id, Bundle args) {
                return new ArticleLoader(getApplicationContext(), BASE_URL);
            }

            @Override
            public void onLoadFinished(Loader<Article> loader, Article data) {
                mData = data;
                setupUI();
            }

            @Override
            public void onLoaderReset(Loader<Article> loader) {
            }
        });
    }
}
