package io.keypunchers.xa.app;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ViewPagerAdapter;
import io.keypunchers.xa.fragments.ArticleFragment;
import io.keypunchers.xa.fragments.CommentListFragment;
import io.keypunchers.xa.fragments.ImageListFragment;
import io.keypunchers.xa.fragments.VideoListFragment;
import io.keypunchers.xa.loaders.ArticleLoader;
import io.keypunchers.xa.loaders.SubmitArticleCommentLoader;
import io.keypunchers.xa.models.Article;
import android.app.ProgressDialog;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import io.keypunchers.xa.misc.Common;

public class ArticleActivity extends AppCompatActivity {
    private String BASE_URL;
    private Article mData;
    private String TAG = ArticleActivity.class.getSimpleName();

    private ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;

	private ProgressDialog mProgressDialog;

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

		mProgressDialog = new ProgressDialog(ArticleActivity.this);
		mProgressDialog.setCancelable(false);

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
                AlertDialog.Builder mDialog = new AlertDialog.Builder(ArticleActivity.this);
                mDialog.setTitle("Enter Comment");
				mDialog.setCancelable(false);

				final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                final EditText mInput = new EditText(ArticleActivity.this);
				mInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

				int padding = Common.convertDpToPx(16, ArticleActivity.this);
                mInput.setLayoutParams(mLayoutParams);
                mDialog.setView(mInput, padding, 0, padding, 0);


                mDialog.setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String comment = mInput.getText().toString();

                                if (!comment.equals("")) {
                                    imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);

                                    mProgressDialog.show(ArticleActivity.this, "Submitting", "Please wait while submitting your comment");

                                    postComment(comment);
                                } else {
                                    Common.makeSnackbar(ArticleActivity.this, "Comment cannot be empty", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        });

                mDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
								imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
                                dialog.cancel();
                            }
                        });

                mDialog.show();
				mInput.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

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
        LoaderManager.LoaderCallbacks loader = new LoaderManager.LoaderCallbacks<Boolean>() {

            @Override
            public Loader<Boolean> onCreateLoader(int id, Bundle args) {
                return new SubmitArticleCommentLoader(getApplicationContext(), BASE_URL, comment);
            }

            @Override
            public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
                mProgressDialog.dismiss();

                if (data) {
                    finish();
                    startActivity(getIntent());
                } else {
                    Common.makeSnackbar(ArticleActivity.this, "Something went wrong while posting comment.", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<Boolean> loader) {
            }
        };

        getSupportLoaderManager().restartLoader(1, null, loader);
    }

    private void getArticle() {
        LoaderManager.LoaderCallbacks loader = new LoaderManager.LoaderCallbacks<Article>() {

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
        };

        getSupportLoaderManager().restartLoader(0, null, loader);
    }
}
