package io.keypunchers.xa.fragments;


import android.os.Bundle;
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

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ScreenshotsAdapter;
import io.keypunchers.xa.loaders.LatestScreenshotsLoader;
import io.keypunchers.xa.models.Screenshot;

public class ScreenshotsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Screenshot>> {
    private ArrayList<Screenshot> mData = new ArrayList<>();
    private ScreenshotsAdapter mAdapter;

    public ScreenshotsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_screenshots, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        mAdapter = new ScreenshotsAdapter(getActivity(), mData);
        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_latest_screenshot);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int LOADER_ID = getActivity().getResources().getInteger(R.integer.latest_screenshots_loader_id);

        if (mData.isEmpty() && getArguments() != null) {
            String AB_TITLE = getArguments().getString("ab_title");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        if (mData.isEmpty()) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<ArrayList<Screenshot>> onCreateLoader(int id, Bundle args) {
        return new LatestScreenshotsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Screenshot>> loader, ArrayList<Screenshot> data) {
		mData.addAll(data);
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), data.size());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Screenshot>> loader) {
    }
}
