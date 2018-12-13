package me.christiansoler.xa.fragments;

import android.view.*;
import me.christiansoler.xa.*;
import android.support.v4.app.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.annotation.*;
import android.widget.*;
import android.content.*;
import android.preference.*;
import android.support.v7.widget.*;
import me.christiansoler.xa.misc.*;
import me.christiansoler.xa.adapters.*;
import me.christiansoler.xa.models.*;
import java.util.*;
import android.support.v4.content.Loader;
import me.christiansoler.xa.loaders.*;

public class SearchResultFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Game>>
{
	private String BASE_URL;
	private String QUERY;
	private int LOADER_ID = 0;
	private int mCurrentPage = 1;
	private ArrayList<Game> mData = new ArrayList<>();
	private GameListAdapter mAdapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }
	
	@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final int mMaxItems = mPrefs.getInt(getString(R.string.ENDLESS_SCROLLER_MAX_ITEMS_TAG), 50);

        
        mAdapter = new GameListAdapter(mData);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        EndlessRecyclerViewScrollListener mScroller = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
				
//                if (totalItemsCount < mMaxItems) {
//                    mCurrentPage = page + 1;
//                    makeNetworkCall();
//                }
            }
        };

        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_search_result_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(mLinearLayoutManager);
        //mRvContent.addOnScrollListener(mScroller);
    }
	
	@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            String AB_TITLE = getArguments().getString("ab_title");
			QUERY = getArguments().getString("search_query");
            BASE_URL = Common.BASE_URL + "/search.php";

            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
				
			if (mData.isEmpty() && !QUERY.equals("")) {
				makeNetworkCall();
			}
        }
    }
	
	@Override
	public Loader<ArrayList<Game>> onCreateLoader(int p1, Bundle args)
	{
		return new SearchResultLoader(getActivity(), BASE_URL, QUERY);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Game>> loader, ArrayList<Game> data)
	{
		mData.addAll(data);
        mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mData.size());
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Game>> loader)
	{
		
	}
	
	private void makeNetworkCall() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }
}
