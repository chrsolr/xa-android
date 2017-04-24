package io.keypunchers.xa.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ArticleListAdapter;
import io.keypunchers.xa.loaders.ArticleListLoader;
import io.keypunchers.xa.misc.EndlessRecyclerViewScrollListener;
import io.keypunchers.xa.models.ArticleListItem;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ArticleListItem>> {
    private String BASE_URL;
    private ArrayList<ArticleListItem> mData = new ArrayList<>();
    private ArticleListAdapter mAdapter;
    private ImageView mIvBanner;
    private int LOADER_ID;
    private int mCurrentPage = 1;

    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final int mMaxItems = mPrefs.getInt("ENDLESS_SCROLLER_MAX_ITEMS", 50);

        mIvBanner = (ImageView) view.findViewById(R.id.iv_banner);

        mAdapter = new ArticleListAdapter(getActivity(), mData);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        EndlessRecyclerViewScrollListener mScroller = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount < mMaxItems) {
                    mCurrentPage = page + 1;
                    makeNetworkCall();
                }
            }
        };

        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_news);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(mLinearLayoutManager);
        mRvContent.addOnScrollListener(mScroller);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOADER_ID = getActivity().getResources().getInteger(R.integer.news_loader_id);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            String AB_TITLE = getArguments().getString("ab_title");
            String mHeaderImageUrl = getArguments().getString("header_image_url");

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);

            Picasso.with(getActivity()).load(mHeaderImageUrl).noFade().into(mIvBanner);
        }

        if (mData.isEmpty()) {
            makeNetworkCall();
        }
    }

    @Override
    public Loader<ArrayList<ArticleListItem>> onCreateLoader(int id, Bundle args) {
        return new ArticleListLoader(getActivity(), BASE_URL + mCurrentPage + "/");
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ArticleListItem>> loader, ArrayList<ArticleListItem> data) {
        mData.addAll(data);
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mData.size());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ArticleListItem>> loader) {
    }

    private void makeNetworkCall() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
