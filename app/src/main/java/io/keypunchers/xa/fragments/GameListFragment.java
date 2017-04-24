package io.keypunchers.xa.fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GameListAdapter;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.loaders.GamesListLoader;
import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.GridLayoutItemOffsetDecoration;
import io.keypunchers.xa.models.Game;
import java.util.*;
import android.preference.*;
import android.content.*;
import android.widget.*;
import io.keypunchers.xa.misc.*;

public class GameListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Game>> {
    private String BASE_URL;
    private ArrayList<Game> mData = new ArrayList<>();
    private GameListAdapter mAdapter;
    private ListView mLvAlphabet;
    private SlidingPaneLayout mSlidingPane;
	private String mSelectedPlatform;
	private String mCurrentSelectedLetter = "a";
	private int mCurrentPage = 1;
	private SharedPreferences mPrefs;

	private int LOADER_ID;

	private String[] mAlphabetTitles;

    public GameListFragment() {
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

        setupSlidingPane(view);

        mAdapter = new GameListAdapter(getActivity(), mData);

		GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
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
        mRvContent.addItemDecoration(new GridLayoutItemOffsetDecoration(getActivity(), Common.convertDpToPx(4, getActivity())));
		mRvContent.addOnScrollListener(mScroller);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSelectedPlatform = mPrefs.getString("DEFAULT_PLATFORM", "xbox-one");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_games_title);

        LOADER_ID = getActivity().getResources().getInteger(R.integer.games_loader_id);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url");
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
                if (mSlidingPane.isOpen())
                    mSlidingPane.closePane();
                else
                    mSlidingPane.openPane();
				break;
			case R.id.menu_browse_games_xone:
				mSelectedPlatform = "xbox-one";
				cleanFetch();
				break;
			case R.id.menu_browse_games_x360:
				mSelectedPlatform = "retail";
				cleanFetch();
				break;
			case R.id.menu_browse_games_arcade:
				mSelectedPlatform = "arcade";
				cleanFetch();
				break;
			case R.id.menu_browse_games_japanese:
				mSelectedPlatform = "japanese";
				cleanFetch();
				break;
			case R.id.menu_browse_games_gfwl:
				mSelectedPlatform = "pc";
				cleanFetch();
				break;
			case R.id.menu_browse_games_mobile:
				mSelectedPlatform = "wp7";
				cleanFetch();
				break;
			case R.id.menu_browse_games_win8:
				mSelectedPlatform = "win8";
				cleanFetch();
				break;
        }
		
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Game>> onCreateLoader(int id, Bundle args) {
        return new GamesListLoader(getActivity(), String.format("%s%s/%s/%d", BASE_URL, mSelectedPlatform, mCurrentSelectedLetter, mCurrentPage));
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

	private void setupSlidingPane(View view)
	{
		mAlphabetTitles = getActivity().getResources().getStringArray(R.array.browse_game_alphabet);

        mSlidingPane = (SlidingPaneLayout) view.findViewById(R.id.slp_game_list);
        mSlidingPane.setParallaxDistance(50);

        mLvAlphabet = (ListView) view.findViewById(R.id.lv_alphabet_content);
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
					if (mCurrentSelectedLetter.equals(mAlphabetTitles[position].toLowerCase()))
						return;
						
					if (position == 0)
						mCurrentSelectedLetter = "-";
					else
						mCurrentSelectedLetter = mAlphabetTitles[position].toLowerCase();
					
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
