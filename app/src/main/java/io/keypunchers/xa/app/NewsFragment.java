package io.keypunchers.xa.app;


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

import java.util.List;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.NewsAdapter;
import io.keypunchers.xa.loaders.NewsLoader;
import io.keypunchers.xa.models.ArticleListItem;

public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ArticleListItem>>, AdapterView.OnItemClickListener {
    private ListView mLvContent;
    private NewsAdapter mAdapter;
    private String TAG = NewsFragment.class.getSimpleName();

    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_news_title);

        setRetainInstance(true);

        mLvContent = (ListView) getActivity().findViewById(R.id.lv_news);
        mLvContent.setOnItemClickListener(this);

        getData(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<ArticleListItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ArticleListItem>> loader, List<ArticleListItem> data) {
        displayData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<ArticleListItem>> loader) {

    }

    private void getData(Bundle savedInstanceState) {
        int LOADER_ID = getActivity().getResources().getInteger(R.integer.news_loader_id);

        if (getActivity().getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, NewsFragment.this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, NewsFragment.this);
        }
    }

    private void displayData(List<ArticleListItem> data) {
        mAdapter = new NewsAdapter(getActivity(), data);
        mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = mAdapter.getItem(position).getPageUrl();
        startActivity(new Intent(getActivity(), ArticleActivity.class).putExtra("url", url));
    }
}