package io.keypunchers.xa.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GameListAdapter;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.loaders.GamesListLoader;
import io.keypunchers.xa.misc.EndlessRecyclerViewScrollListener;
import io.keypunchers.xa.models.Game;

public class GameListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Game>> {
    private FirebaseAnalytics mFirebaseAnalytics;
    private String BASE_URL;
    private ArrayList<Game> mData = new ArrayList<>();
    private GameListAdapter mAdapter;
    private SlidingPaneLayout mSlidingPane;
    private String mSelectedPlatform;
    private String mCurrentSelectedLetter = "a";
    private int mCurrentPage = 1;
    private int LOADER_ID;
    private String[] mAlphabetTitles;
    private ActionBar mActionBar;

    public GameListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_games, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        setupSlidingPane(view);

        mAdapter = new GameListAdapter(mData);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        EndlessRecyclerViewScrollListener mScroller = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mCurrentPage = page + 1;
                makeNetworkCall();
            }
        };

        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_games_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(mLayoutManager);
        mRvContent.addOnScrollListener(mScroller);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        setHasOptionsMenu(true);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSelectedPlatform = mPrefs.getString(getString(R.string.DEFAULT_PLATFORM_TAG), "xbox-one");

        LOADER_ID = getActivity().getResources().getInteger(R.integer.games_loader_id);

        String[] titles = getActivity().getResources().getStringArray(R.array.spinner_platforms);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

            if (mActionBar != null)
                mActionBar.setTitle(titles[mPrefs.getInt(getString(R.string.DEFAULT_PLATFORM_POSITION_TAG), 0)]);
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
        String mAnalitysLabel = "";
        String mAnalitysCategory = "";

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_browse_games:
                if (mSlidingPane.isOpen())
                    mSlidingPane.closePane();
                else
                    mSlidingPane.openPane();
                mAnalitysCategory = "browse";
                mAnalitysLabel = "Browse Menu";
                break;
            case R.id.menu_browse_games_xone:
                mSelectedPlatform = "xbox-one";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("Xbox One");
                mAnalitysLabel = "Xbox One";
                cleanFetch();
                break;
            case R.id.menu_browse_games_x360:
                mSelectedPlatform = "retail";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("Xbox 360");
                mAnalitysLabel = "Xbox 360";
                cleanFetch();
                break;
            case R.id.menu_browse_games_arcade:
                mSelectedPlatform = "arcade";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("Arcade");
                mAnalitysLabel = "Arcade";
                cleanFetch();
                break;
            case R.id.menu_browse_games_japanese:
                mSelectedPlatform = "japanese";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("Japanese");
                mAnalitysLabel = "Japanese";
                cleanFetch();
                break;
            case R.id.menu_browse_games_gfwl:
                mSelectedPlatform = "pc";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("GFWL");
                mAnalitysLabel = "GFWL";
                cleanFetch();
                break;
            case R.id.menu_browse_games_mobile:
                mSelectedPlatform = "wp7";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("Mobile");
                mAnalitysLabel = "Mobile";
                cleanFetch();
                break;
            case R.id.menu_browse_games_win8:
                mSelectedPlatform = "win8";
                mCurrentSelectedLetter = "a";
                mActionBar.setTitle("Windows 8");
                mAnalitysLabel = "Windows 8";
                cleanFetch();
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.PLATFORM_SELECTION), mAnalitysCategory.equals("") ? mSelectedPlatform : mAnalitysCategory);
        mFirebaseAnalytics.logEvent(getString(R.string.ACTION), bundle);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.LOCATION), GameListFragment.class.getSimpleName());
        mFirebaseAnalytics.logEvent(getString(R.string.SCREEN), bundle);
    }

    @Override
    public Loader<ArrayList<Game>> onCreateLoader(int id, Bundle args) {
        return new GamesListLoader(getActivity(), String.format(Locale.US, "%s%s/%s/%d", BASE_URL, mSelectedPlatform, mCurrentSelectedLetter, mCurrentPage));
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
        mData.addAll(data);
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mData.size());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Game>> loader) {
    }

    private void makeNetworkCall() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void setupSlidingPane(View view) {
        mAlphabetTitles = getActivity().getResources().getStringArray(R.array.browse_game_alphabet);

        mSlidingPane = (SlidingPaneLayout) view.findViewById(R.id.slp_game_list);
        mSlidingPane.setParallaxDistance(50);

        ListView mLvAlphabet = (ListView) view.findViewById(R.id.lv_alphabet_content);
        mLvAlphabet.setAdapter(new GenericAdapter<>(getActivity(), Arrays.asList(mAlphabetTitles), new GenericAdapter.onSetGetView() {
            @Override
            public View onGetView(int position, View convertView, ViewGroup parent, Context context, List<?> data) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.row_alphabet, parent, false);

                    viewHolder = new ViewHolder();
                    assert convertView != null;

                    viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_alphabet_title);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.mTvTitle.setText(data.get(position).toString());

                return convertView;
            }
        }));

        mLvAlphabet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentSelectedLetter.equals(mAlphabetTitles[position].toLowerCase(Locale.US)))
                    return;

                if (position == 0)
                    mCurrentSelectedLetter = "-";
                else
                    mCurrentSelectedLetter = mAlphabetTitles[position].toLowerCase(Locale.US);

                cleanFetch();
            }
        });
    }

    private void cleanFetch() {
        mCurrentPage = 1;
        mAdapter.notifyItemRangeRemoved(0, mData.size());
        mData.clear();
        mSlidingPane.closePane();
        makeNetworkCall();
    }

    private class ViewHolder {
        TextView mTvTitle;
    }
}
