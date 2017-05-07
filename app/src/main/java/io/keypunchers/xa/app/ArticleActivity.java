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
import android.support.v4.util.Pair;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import io.keypunchers.xa.misc.Singleton;
import android.app.Dialog;

public class ArticleActivity extends AppCompatActivity {
    private String BASE_URL;
    private Article mData;
    private String TAG = ArticleActivity.class.getSimpleName();

    private ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private FloatingActionButton mFab;

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
			
        mFab = (FloatingActionButton) findViewById(R.id.fab_article);
        mFab.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ArticleActivity.this);
					builder.setCancelable(true);

					View layout = getLayoutInflater().inflate(R.layout.dialog_comment, null, false);
					
					final EditText mInputComment = (EditText) layout.findViewById(R.id.et_comment);
					
					builder.setView(layout);
					builder.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

					builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

					final AlertDialog dialog = builder.create();
					dialog.show();
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener(){
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
        getSupportLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<Pair<Boolean, String>>() {

				@Override
				public Loader<Pair<Boolean, String>> onCreateLoader(int id, Bundle args) {
					return new SubmitArticleCommentLoader(getApplicationContext(), BASE_URL, comment);
				}

				@Override
				public void onLoadFinished(Loader<Pair<Boolean, String>> loader, Pair<Boolean, String> data) {
					if (data.first) {
						mSnackbar.dismiss();
						finish();
						startActivity(getIntent());
					} else if (!data.first && data.second.equals("Cached")) {
						
					} else {
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
