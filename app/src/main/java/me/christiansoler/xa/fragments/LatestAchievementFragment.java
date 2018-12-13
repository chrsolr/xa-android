package me.christiansoler.xa.fragments;


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

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.adapters.LatestAchievementsListAdapter;
import me.christiansoler.xa.loaders.LatestAchievementsLoader;
import me.christiansoler.xa.misc.EndlessRecyclerViewScrollListener;
import me.christiansoler.xa.misc.VolleySingleton;
import me.christiansoler.xa.models.LatestAchievement;
import me.christiansoler.xa.models.LatestScreenshot;
import android.widget.TextView;
import android.animation.ObjectAnimator;

public class LatestAchievementFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<LatestAchievement>> {
    private LatestScreenshot mBanner;
    private ArrayList<LatestAchievement> mData = new ArrayList<>();
    private String BASE_URL;
    private LatestAchievementsListAdapter mAdapter;
    private ImageView mIvBanner;
    private TextView mTvBannerTitle;
    private int LOADER_ID;
    private int mCurrentPage = 1;

    public LatestAchievementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest_achievement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final int mMaxItems = mPrefs.getInt(getString(R.string.ENDLESS_SCROLLER_MAX_ITEMS_TAG), 50);

        mIvBanner = (ImageView) view.findViewById(R.id.iv_banner);
        mTvBannerTitle = (TextView) view.findViewById(R.id.tv_banner_title);

        mAdapter = new LatestAchievementsListAdapter(mData);

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

        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_la_achievements_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(mLinearLayoutManager);
        mRvContent.addOnScrollListener(mScroller);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOADER_ID = getActivity().getResources().getInteger(R.integer.latest_achievements_loader_id);

        if (getArguments() != null) {
            String AB_TITLE = getArguments().getString("ab_title");
            BASE_URL = getArguments().getString("url");
            mBanner = getArguments().getParcelable("header");

            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);

            mTvBannerTitle.setText(mBanner.getTitle());
            mTvBannerTitle.setAllCaps(true);

            ObjectAnimator.ofFloat(mTvBannerTitle, "translationY", 200, 0).setDuration(1000).start();

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
        bundle.putString(getString(R.string.LOCATION), LatestAchievementFragment.class.getSimpleName());
    }

    @Override
    public Loader<ArrayList<LatestAchievement>> onCreateLoader(int id, Bundle args) {
        return new LatestAchievementsLoader(getActivity(), BASE_URL + mCurrentPage + "/");
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<LatestAchievement>> loader, ArrayList<LatestAchievement> data) {
        mData.addAll(data);
        mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mData.size());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LatestAchievement>> loader) {

    }

    private void makeNetworkCall() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
