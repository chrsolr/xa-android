package io.keypunchers.xa.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GameListAdapter;
import io.keypunchers.xa.loaders.GamesListLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.GridLayoutItemOffsetDecoration;
import io.keypunchers.xa.models.Game;

public class GamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Game>> {
    private String BASE_URL;
    private ArrayList<Game> mData = new ArrayList<>();
    private GameListAdapter mAdapter;
    private ListView mLvAlphabet;
    private SlidingPaneLayout mSlidingPane;

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_games, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        String[] alphabet = getActivity().getResources().getStringArray(R.array.browse_game_alphabet);

        mSlidingPane = (SlidingPaneLayout) view.findViewById(R.id.slp_game_list);
        mSlidingPane.setParallaxDistance(30);

        mLvAlphabet = (ListView) view.findViewById(R.id.lv_alphabet_content);
        mLvAlphabet.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, alphabet));

        mAdapter = new GameListAdapter(getActivity(), mData);
        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_games_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRvContent.addItemDecoration(new GridLayoutItemOffsetDecoration(getActivity(), Common.convertDpToPx(16, getActivity())));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_games_title);

        int LOADER_ID = getActivity().getResources().getInteger(R.integer.games_loader_id);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url") + "a/";
            String AB_TITLE = getArguments().getString("ab_title");

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        if (mData.isEmpty()) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browse_games, menu);

        menu.removeItem(R.id.main_menu_donate);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_browse_games:
                if(mSlidingPane.isOpen())
                    mSlidingPane.closePane();
                else
                    mSlidingPane.openPane();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Game>> onCreateLoader(int id, Bundle args) {
        return new GamesListLoader(getActivity(), BASE_URL, mData);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
        mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mData.size());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Game>> loader) {

    }
}
