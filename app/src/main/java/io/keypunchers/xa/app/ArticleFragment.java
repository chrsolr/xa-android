package io.keypunchers.xa.app;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.NewsAdapter;
import io.keypunchers.xa.loaders.ArticleLoader;
import io.keypunchers.xa.misc.SingletonVolley;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment implements LoaderManager.LoaderCallbacks<JSONObject> {
    private String BASE_URL;

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_frament, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        BASE_URL = getArguments().getString("url");

        getData(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (menu != null) {
            menu.removeItem(R.id.main_menu_donate);
        }

        inflater.inflate(R.menu.menu_article, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.menu_item_open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL));
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(getActivity(), BASE_URL);
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        try {
            setArticleHeader(data.getJSONObject("header"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }

    private void setArticleHeader(JSONObject data) {
        try {

            ((NetworkImageView) getActivity().findViewById(R.id.iv_article_author_avatar))
                    .setImageUrl(data.getString("profile_image"), SingletonVolley.getImageLoader());

            ((TextView) getActivity().findViewById(R.id.tv_article_title))
                    .setText(data.getString("header_title"));

            ((TextView) getActivity().findViewById(R.id.tv_article_date))
                    .setText(data.getString("header_date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData(Bundle savedInstanceState) {
        int LOADER_ID = getActivity().getResources().getInteger(R.integer.article_loader_id);

        if (getActivity().getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, ArticleFragment.this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, ArticleFragment.this);
        }
    }
}
