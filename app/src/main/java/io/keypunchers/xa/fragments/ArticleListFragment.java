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

import com.android.volley.toolbox.ImageLoader;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ArticleListAdapter;
import io.keypunchers.xa.loaders.ArticleListLoader;
import io.keypunchers.xa.misc.EndlessRecyclerViewScrollListener;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.ArticleListItem;
import io.keypunchers.xa.models.LatestScreenshot;
import android.widget.TextView;

public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<ArticleListItem>> {
    private FirebaseAnalytics mFirebaseAnalytics;
	private LatestScreenshot mBanner;
    private String BASE_URL;
    private ArrayList<ArticleListItem> mData = new ArrayList<>();
    private ArticleListAdapter mAdapter;
    private ImageView mIvBanner;
	private TextView mTvBannerTitle;
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
        final int mMaxItems = mPrefs.getInt(getString(R.string.ENDLESS_SCROLLER_MAX_ITEMS_TAG), 50);

        mIvBanner = (ImageView) view.findViewById(R.id.iv_banner);
		mTvBannerTitle = (TextView) view.findViewById(R.id.tv_banner_title);

        mAdapter = new ArticleListAdapter(mData);

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        LOADER_ID = getActivity().getResources().getInteger(R.integer.news_loader_id);

        if (getArguments() != null) {
			String AB_TITLE = getArguments().getString("ab_title");
            BASE_URL = getArguments().getString("url");
            mBanner = getArguments().getParcelable("header");

            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
				
			mTvBannerTitle.setText(mBanner.getTitle());
			mTvBannerTitle.setAllCaps(true);

            VolleySingleton
                    .getImageLoader()
                    .get(mBanner.getImageUrl(),
                            ImageLoader.getImageListener(mIvBanner, 0, 0));
        }

        if (mData.isEmpty()) {
            makeNetworkCall();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOCATION), ArticleListFragment.class.getSimpleName());
        mFirebaseAnalytics.logEvent(getString(R.string.SCREEN), bundle);
    }

    @Override
    public Loader<ArrayList<ArticleListItem>> onCreateLoader(int id, Bundle args) {
        return new ArticleListLoader(getActivity(), String.format(Locale.US, "%s%d/", BASE_URL, mCurrentPage));
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
