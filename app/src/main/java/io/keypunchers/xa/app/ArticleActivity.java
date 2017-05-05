package io.keypunchers.xa.app;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ViewPagerAdapter;
import io.keypunchers.xa.fragments.ArticleFragment;
import io.keypunchers.xa.fragments.CommentListFragment;
import io.keypunchers.xa.fragments.ImageListFragment;
import io.keypunchers.xa.fragments.VideoListFragment;
import io.keypunchers.xa.loaders.ArticleLoader;
import io.keypunchers.xa.models.Article;
import android.support.design.widget.FloatingActionButton;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.content.DialogInterface;
import io.keypunchers.xa.loaders.SubmitArticleCommentLoader;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Article> {
    private String BASE_URL;
    private int LOADER_ID;
    private Article mData;
    private String TAG = ArticleActivity.class.getSimpleName();

	private ViewPager mViewPager;

	private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.ab_article);
        }

        LOADER_ID = getResources().getInteger(R.integer.article_loader_id);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("url"))
            BASE_URL = getIntent().getExtras().getString("url");

		mViewPager = (ViewPager) findViewById(R.id.vp_article);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_article);
        tabLayout.setupWithViewPager(mViewPager);

		FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab_article);
		mFab.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					AlertDialog.Builder mDialog = new AlertDialog.Builder(ArticleActivity.this);
					mDialog.setTitle("Enter Comment");

					final EditText mInput = new EditText(ArticleActivity.this);
					LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);

					mInput.setLayoutParams(mLayoutParams);
					mDialog.setView(mInput);

					mDialog.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								String comment = mInput.getText().toString();
								postComment(comment);
							}
						});

					mDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});

					mDialog.show();
				}
			});

        makeNetworkCall(savedInstanceState);
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
    public Loader<Article> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, BASE_URL, mData);
    }

    @Override
    public void onLoadFinished(Loader<Article> loader, Article data) {
        mData = data;
        setupUI();
    }

    @Override
    public void onLoaderReset(Loader<Article> loader) {
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    private void makeNetworkCall(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelable(TAG);
            setupUI();
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private void setupUI() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
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
        LoaderManager.LoaderCallbacks mSubmitCommentLoader = new LoaderManager.LoaderCallbacks<Boolean>() {

			@Override
			public Loader<Boolean> onCreateLoader(int id, Bundle args) {
				return new SubmitArticleCommentLoader(getApplicationContext(), BASE_URL, comment);
			}

			@Override
			public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
				if (data) {
					getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
				}
			}

			@Override
			public void onLoaderReset(Loader<Boolean> loader) {
			}
        };

        getSupportLoaderManager().restartLoader(200, null, mSubmitCommentLoader);
    }
}
