package me.christiansoler.xa.app;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.adapters.GameListAdapter;
import me.christiansoler.xa.loaders.SearchResultLoader;
import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.misc.EndlessRecyclerViewScrollListener;
import me.christiansoler.xa.models.Game;

public class SearchableActivity extends AppCompatActivity {

    private final String BASE_URL = Common.BASE_URL + "/search.php";
    private ArrayList<Game> mData = new ArrayList<>();
    private GameListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.ab_search_result);
        }

        mAdapter = new GameListAdapter(mData);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        EndlessRecyclerViewScrollListener mScroller = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            }
        };

        RecyclerView mRvContent = (RecyclerView) findViewById(R.id.rv_search_result_list);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(mLinearLayoutManager);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            getSearchResult(query);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSearchResult(final String query) {
        LoaderManager.LoaderCallbacks loader = new LoaderManager.LoaderCallbacks<ArrayList<Game>>() {
            @Override
            public Loader<ArrayList<Game>> onCreateLoader(int id, Bundle args) {
                return new SearchResultLoader(getApplicationContext(), BASE_URL, query);
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
                mData.addAll(data);
                mAdapter.notifyItemRangeChanged(mAdapter.getItemCount(), mData.size());
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<Game>> loader) {

            }
        };

        getSupportLoaderManager().restartLoader(0, null, loader);
    }
}
