package io.keypunchers.xa.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ViewPagerAdapter;
import io.keypunchers.xa.loaders.UpcomingGamesLoader;
import io.keypunchers.xa.models.UpcomingGame;

public class UpcomingGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<UpcomingGame>> {
    private String BASE_URL;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private Map<String, ArrayList<UpcomingGame>> mData = new HashMap<>();
    private int mLoaderCounter = 0;
    private FirebaseAnalytics mFirebaseAnalytics;

    public UpcomingGamesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upcoming_games, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_upcoming_games);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tl_upcoming_games);
        tabLayout.setupWithViewPager(mViewPager);

        mAdapter = new ViewPagerAdapter(getChildFragmentManager());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        if (mData.isEmpty() && getArguments() != null) {
            BASE_URL = getArguments().getString("url");
        }

        makeNetworkCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOCATION), UpcomingGamesFragment.class.getSimpleName());
        mFirebaseAnalytics.logEvent(getString(R.string.SCREEN), bundle);
    }

    @Override
    public Loader<ArrayList<UpcomingGame>> onCreateLoader(int id, Bundle args) {
        AsyncTaskLoader loader = null;
        switch (id) {
            case 0:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL);
                break;
            case 1:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "PAL/");
                break;
            case 2:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "Arcade/");
                break;
            case 3:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "xbox-one/");
                break;
            case 4:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "xbox-360/");
                break;
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<UpcomingGame>> loader, ArrayList<UpcomingGame> data) {
        String key = "";

        int id = loader.getId();

        switch (id) {
            case 0:
                key = "NTSC";
                break;
            case 1:
                key = "PAL";
                break;
            case 2:
                key = "Arcade";
                break;
            case 3:
                key = "XOne";
                break;
            case 4:
                key = "X360";
                break;
        }

        mData.put(key, data);

        mLoaderCounter++;

        if (mLoaderCounter == 5) {
            setupUI();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<UpcomingGame>> loader) {
    }

    private void makeNetworkCall() {
        if (!mData.isEmpty()) {
            setupUI();
        } else {
            getActivity().getSupportLoaderManager().restartLoader(0, null, this);
            getActivity().getSupportLoaderManager().restartLoader(1, null, this);
            getActivity().getSupportLoaderManager().restartLoader(2, null, this);
            getActivity().getSupportLoaderManager().restartLoader(3, null, this);
            getActivity().getSupportLoaderManager().restartLoader(4, null, this);
        }
    }

    private void setupUI() {
        if (!mData.get("XOne").isEmpty())
            mAdapter.addFragment(new UpcomingGamesChildFragment().newInstance(mData.get("XOne")), "Xbox One");
        if (!mData.get("X360").isEmpty())
            mAdapter.addFragment(new UpcomingGamesChildFragment().newInstance(mData.get("X360")), "Xbox 360");
        if (!mData.get("NTSC").isEmpty())
            mAdapter.addFragment(new UpcomingGamesChildFragment().newInstance(mData.get("NTSC")), "NTSC");
        if (!mData.get("PAL").isEmpty())
            mAdapter.addFragment(new UpcomingGamesChildFragment().newInstance(mData.get("PAL")), "PAL");
        if (!mData.get("Arcade").isEmpty())
            mAdapter.addFragment(new UpcomingGamesChildFragment().newInstance(mData.get("Arcade")), "Arcade");

        mViewPager.setAdapter(mAdapter);
    }
}
