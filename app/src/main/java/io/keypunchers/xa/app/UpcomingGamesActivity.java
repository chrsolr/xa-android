package io.keypunchers.xa.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import io.keypunchers.xa.R;
import io.keypunchers.xa.fragments.UpcomingGamesFragment;
import io.keypunchers.xa.loaders.UpcomingGamesLoader;
import io.keypunchers.xa.models.UpcomingGame;

public class UpcomingGamesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<UpcomingGame>> {
    private String BASE_URL;
    private String TAG = UpcomingGamesActivity.class.getSimpleName();
    private Map<String, ArrayList<UpcomingGame>> mData = new HashMap<>();
    private int mLoaderCounter = 0;
    private ArrayList<UpcomingGame> mNTSCData;
    private ArrayList<UpcomingGame> mPALData;
    private ArrayList<UpcomingGame> mArcadeData;
    private ArrayList<UpcomingGame> mXoneData;
    private ArrayList<UpcomingGame> mX360Data;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_games);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.ab_upcoming_games_title);

        mViewPager = (ViewPager) findViewById(R.id.vp_upcoming_games);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_upcoming_games);
        tabLayout.setupWithViewPager(mViewPager);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("url"))
            BASE_URL = getIntent().getExtras().getString("url");

        makeNetworkCall(savedInstanceState);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<ArrayList<UpcomingGame>> onCreateLoader(int id, Bundle args) {
        AsyncTaskLoader loader = null;
        switch (id) {
            case 0:
                loader = new UpcomingGamesLoader(this, BASE_URL, mNTSCData);
                break;
            case 1:
                loader = new UpcomingGamesLoader(this, BASE_URL + "PAL/", mPALData);
                break;
            case 2:
                loader = new UpcomingGamesLoader(this, BASE_URL + "Arcade/", mArcadeData);
                break;
            case 3:
                loader = new UpcomingGamesLoader(this, BASE_URL + "xbox-one/", mXoneData);
                break;
            case 4:
                loader = new UpcomingGamesLoader(this, BASE_URL + "xbox-360/", mX360Data);
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
                key = "Xbox One";
                break;
            case 4:
                key = "Xbox 360";
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

    private void makeNetworkCall(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mNTSCData = savedInstanceState.getParcelableArrayList("NTSC");
            mPALData = savedInstanceState.getParcelableArrayList("PAL");
            mArcadeData = savedInstanceState.getParcelableArrayList("ARCADE");
            mXoneData = savedInstanceState.getParcelableArrayList("XONE");
            mX360Data = savedInstanceState.getParcelableArrayList("X360");
            //setupUI();
        } else {
            getSupportLoaderManager().restartLoader(0, null, this);
            getSupportLoaderManager().restartLoader(1, null, this);
            getSupportLoaderManager().restartLoader(2, null, this);
            getSupportLoaderManager().restartLoader(3, null, this);
            getSupportLoaderManager().restartLoader(4, null, this);
        }
    }

    private void setupUI() {
        if (!mData.get("Xbox One").isEmpty())
            mAdapter.addFragment(new UpcomingGamesFragment().newInstance(mData.get("Xbox One")), "Xbox One");
        if (!mData.get("Xbox 360").isEmpty())
            mAdapter.addFragment(new UpcomingGamesFragment().newInstance(mData.get("Xbox 360")), "Xbox 360");
        if (!mData.get("NTSC").isEmpty())
            mAdapter.addFragment(new UpcomingGamesFragment().newInstance(mData.get("NTSC")), "NTSC");
        if (!mData.get("PAL").isEmpty())
            mAdapter.addFragment(new UpcomingGamesFragment().newInstance(mData.get("PAL")), "PAL");
        if (!mData.get("Arcade").isEmpty())
            mAdapter.addFragment(new UpcomingGamesFragment().newInstance(mData.get("Arcade")), "Arcade");

        mViewPager.setAdapter(mAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
