package io.keypunchers.xa.app;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.xml.sax.XMLReader;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.ArticleLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Article;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Article> {
    private String BASE_URL;
    private int LOADER_ID;
    private LinearLayout mLlContent;
    private Article mData;
    private String TAG = ArticleActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init(savedInstanceState);
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
                intent.putExtra(Intent.EXTRA_TEXT, "Check it out!: " + mData.getHeaderTitle() + "\n\n" + BASE_URL.replace(".html", "") + "\n\n" + "Shared via XA App.");
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
        processData(mData = data);
    }

    @Override
    public void onLoaderReset(Loader<Article> loader) {
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    private void init(Bundle savedInstanceState) {
        mLlContent = (LinearLayout) findViewById(R.id.ll_article);

        LOADER_ID = getResources().getInteger(R.integer.article_loader_id);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("url"))
            BASE_URL = getIntent().getExtras().getString("url");

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelable(TAG);
            processData(mData);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private void processData(Article data) {
        setArticleHeader(data);
        setArticleBody(data);

        if (data.getImageUrls().size() > 0)
            setArticleImages(data.getImageUrls());

        if (data.getVideoUrls().size() > 0)
            setArticleVideos(data.getVideoUrls());
    }

    private void setArticleHeader(Article data) {
        ((NetworkImageView) findViewById(R.id.iv_article_author_avatar))
                .setImageUrl(data.getAuthorProfileImageUrl(), SingletonVolley.getImageLoader());

        ((TextView) findViewById(R.id.tv_article_title))
                .setText(data.getHeaderTitle());

        ((TextView) findViewById(R.id.tv_article_date))
                .setText(data.getHeaderDate());
    }

    private void setArticleBody(Article data) {
        Spanned text;
        TextView mTvBody = (TextView) findViewById(R.id.tv_article_body);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(data.getBodyText(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM, null, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equals("ul") && !opening) output.append("\n");
                    if (tag.equals("li") && opening) output.append("\n\t•\t\t");
                }
            });
        } else {
            text = Html.fromHtml(data.getBodyText(), null, new Html.TagHandler() {
                @Override
                public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                    if (tag.equals("ul") && !opening) output.append("\n");
                    if (tag.equals("li") && opening) output.append("\n\t•\t\t");
                }
            });
        }

        mTvBody.setText(text);
        mTvBody.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setArticleImages(final ArrayList<String> links) {

        int px = Common.convertDpToPx(16, this);

        LinearLayout mLayoutContainer = (LinearLayout) findViewById(R.id.ll_article_images);

        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(px, px / 2, px, px / 2);

        for (int i = 0; i < links.size(); i++) {
            NetworkImageView mImageView = new NetworkImageView(this);
            mImageView.setId(links.size());
            mImageView.setAdjustViewBounds(true);
            mImageView.setScaleType(NetworkImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(0, 0, 0, 0);
            mImageView.setBackgroundResource(R.color.color_primary);
            mImageView.setLayoutParams(mLayoutParams);

            mImageView.setImageUrl(links.get(i), SingletonVolley.getImageLoader());

            final int finalI = i;
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(finalI)));
                    startActivity(mIntent);
                }
            });

            mLayoutContainer.addView(mImageView);
        }

        mLayoutContainer.setVisibility(View.VISIBLE);
    }

    private void setArticleVideos(final ArrayList<String> links) {
        LinearLayout mLayoutContainer = (LinearLayout) findViewById(R.id.ll_article_videos);

        int px = Common.convertDpToPx(16, this);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(px, px / 2, px, px / 2);

        for (int i = 0; i < links.size(); i++) {

            String video_id = links.get(i)
                    .replace("https://www.youtube.com/embed/", "")
                    .replace("?ecver=1", "");

            TextView mVideoLinks = new TextView(this);
            mVideoLinks.setId(links.size());
            mVideoLinks.setText("Video #" + (i + 1));
            mVideoLinks.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
            mVideoLinks.setPadding(px, px / 2, px, px / 2);

            NetworkImageView mImageView = new NetworkImageView(this);
            mImageView.setId(links.size());
            mImageView.setAdjustViewBounds(true);
            mImageView.setScaleType(NetworkImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(0, 0, 0, 0);
            mImageView.setBackgroundResource(R.color.color_primary);
            mImageView.setLayoutParams(mLayoutParams);

            mImageView.setImageUrl("https://i3.ytimg.com/vi/" + video_id + "/0.jpg", SingletonVolley.getImageLoader());

            final int finalI = i;
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(links.get(finalI).replace("embed/", "watch?v=")));
                    startActivity(mIntent);
                }
            });

            mLayoutContainer.addView(mVideoLinks);
            mLayoutContainer.addView(mImageView);
        }

        mLayoutContainer.setVisibility(View.VISIBLE);
    }
}
