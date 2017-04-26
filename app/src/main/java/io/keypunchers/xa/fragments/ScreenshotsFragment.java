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
import java.util.Map;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ImageListAdapter;
import io.keypunchers.xa.loaders.ScreenshotsLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.EndlessRecyclerViewScrollListener;
import io.keypunchers.xa.models.Screenshots;

public class ScreenshotsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Screenshots> {
    private ArrayList<String> mData = new ArrayList<>();
    private String GAME_PERMALINK;
    private int mCurrentPage = 1;
    private ImageListAdapter mAdapter;


    public ScreenshotsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        mAdapter = new ImageListAdapter(getActivity(), mData);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        EndlessRecyclerViewScrollListener mScroller = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mCurrentPage = page + 1;
                makeNetworkCall();
            }
        };

        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_image_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(mLinearLayoutManager);
        mRvContent.addOnScrollListener(mScroller);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("game_permalink")) {
            GAME_PERMALINK = getArguments().getString("game_permalink");
        }

        if (mData.isEmpty()) {
            makeNetworkCall();
        }
    }

    @Override
    public Loader<Screenshots> onCreateLoader(int id, Bundle args) {
        return new ScreenshotsLoader(getActivity(), Common.getGameScreenshotsUrlByPermalink(GAME_PERMALINK, mCurrentPage));
    }

    @Override
    public void onLoadFinished(Loader<Screenshots> loader, Screenshots data) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(data.getTitle());

        mData.addAll(data.getImageUrls());
        mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), data.getImageUrls().size());
    }

    @Override
    public void onLoaderReset(Loader<Screenshots> loader) {

    }

    private void makeNetworkCall() {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }
}
