package io.keypunchers.xa.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ArrayList<UpcomingGame> mNTSCData;
    private ArrayList<UpcomingGame> mPALData;
    private ArrayList<UpcomingGame> mArcadeData;
    private ArrayList<UpcomingGame> mXoneData;
    private ArrayList<UpcomingGame> mX360Data;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().findViewById(R.id.apl_main).setElevation(0);
        }

        if (mData.isEmpty() && getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("ab_title"));
        }

        makeNetworkCall();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().findViewById(R.id.apl_main).setElevation(12);
        }
    }

    @Override
    public Loader<ArrayList<UpcomingGame>> onCreateLoader(int id, Bundle args) {
        AsyncTaskLoader loader = null;
        switch (id) {
            case 0:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL, mNTSCData);
                break;
            case 1:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "PAL/", mPALData);
                break;
            case 2:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "Arcade/", mArcadeData);
                break;
            case 3:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "xbox-one/", mXoneData);
                break;
            case 4:
                loader = new UpcomingGamesLoader(getActivity(), BASE_URL + "xbox-360/", mX360Data);
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
