package io.keypunchers.xa.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.NewsAdapter;
import io.keypunchers.xa.app.ArticleActivity;
import io.keypunchers.xa.loaders.ArticleListLoader;
import io.keypunchers.xa.models.ArticleListItem;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ArticleListItem>>, AdapterView.OnItemClickListener {
    private final String BASE_URL = "http://www.xboxachievements.com/archive/gaming-news/1/";
    private final String TAG = ArticleListFragment.class.getSimpleName();
    private int LOADER_ID;
    private ArrayList<ArticleListItem> mData;
    private NewsAdapter mAdapter;

    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_news_title);

        setRetainInstance(true);

        LOADER_ID = getActivity().getResources().getInteger(R.integer.news_loader_id);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelable(TAG);
        }

        getData(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG, mData);
    }

    @Override
    public Loader<ArrayList<ArticleListItem>> onCreateLoader(int id, Bundle args) {
        return new ArticleListLoader(getActivity(), BASE_URL, mData);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ArticleListItem>> loader, ArrayList<ArticleListItem> data) {
        mData = data;
        setupUI();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ArticleListItem>> loader) {
        getLoaderManager().destroyLoader(LOADER_ID);
    }

    private void getData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelable(TAG);
            setupUI();
        } else {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private void setupUI() {
        ListView mLvContent = (ListView) getActivity().findViewById(R.id.lv_news);
        mLvContent.setOnItemClickListener(this);

        mAdapter = new NewsAdapter(getActivity(), mData);
        mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = mAdapter.getItem(position).getPageUrl();
        startActivity(new Intent(getActivity(), ArticleActivity.class).putExtra("url", url));
    }
}
