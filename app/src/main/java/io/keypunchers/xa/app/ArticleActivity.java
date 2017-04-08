package io.keypunchers.xa.app;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.XMLReader;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.ArticleLoader;
import io.keypunchers.xa.misc.SingletonVolley;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private String BASE_URL;
    private LinearLayout mLlContent;
    private JSONObject mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_frament);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().getThemedContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLlContent = (LinearLayout) findViewById(R.id.ll_article);

        if(getIntent().getExtras() != null)
            BASE_URL = getIntent().getExtras().getString("url");

        getData(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_item_open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this, BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        bindData(data);
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }

    private void bindData(JSONObject data) {
        try {
            mData = data;

            setArticleHeader(mData.getJSONObject("header"));
            setArticleBody(mData.getJSONObject("body"));
            setArticleImages(mData.getJSONObject("body"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setArticleHeader(JSONObject data) {
        try {

            ((NetworkImageView) findViewById(R.id.iv_article_author_avatar))
                    .setImageUrl(data.getString("profile_image"), SingletonVolley.getImageLoader());

            ((TextView) findViewById(R.id.tv_article_title))
                    .setText(data.getString("header_title"));

            ((TextView) findViewById(R.id.tv_article_date))
                    .setText(data.getString("header_date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setArticleBody(JSONObject data) {
        try {
            Spanned text;
            TextView mTvBody = (TextView) findViewById(R.id.tv_article_body);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text = Html.fromHtml(data.getString("body_text"), Html.FROM_HTML_MODE_LEGACY, null, new Html.TagHandler() {
                    @Override
                    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                        if (tag.equals("ul") && !opening) output.append("\n");
                        if (tag.equals("li") && opening) output.append("\n\t•<br>");
                    }
                });
            } else {
                text = Html.fromHtml(data.getString("body_text"), null, new Html.TagHandler() {
                    @Override
                    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                        if (tag.equals("ul") && !opening) output.append("\n");
                        if (tag.equals("li") && opening) output.append("\n\t•<br>");
                    }
                });
            }

            mTvBody.setText(text);
            mTvBody.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setArticleImages(JSONObject data) {
        try {
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLayoutParams.setMargins(px, px / 2, px, px / 2);

            ArrayList<String> images = (ArrayList<String>) data.get("body_images");

            for (String url : images) {
                NetworkImageView mImageView = new NetworkImageView(this);
                mImageView.setId(images.size());
                mImageView.setAdjustViewBounds(true);
                mImageView.setScaleType(NetworkImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(0, 0, 0, 0);
                mImageView.setBackgroundResource(R.color.color_primary);
                mImageView.setLayoutParams(mLayoutParams);

                mImageView.setImageUrl(url, SingletonVolley.getImageLoader());

                mLlContent.addView(mImageView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            try {
                mData = new JSONObject(savedInstanceState.getString("json"));
                bindData(mData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            int LOADER_ID = getResources().getInteger(R.integer.article_loader_id);

            if (getSupportLoaderManager().getLoader(LOADER_ID) == null) {
                getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, ArticleActivity.this);
            } else {
                getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, ArticleActivity.this);
            }
        }
    }
}
