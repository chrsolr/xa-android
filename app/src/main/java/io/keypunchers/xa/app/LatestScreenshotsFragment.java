package io.keypunchers.xa.app;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.LatestScreenshotsAdapter;
import io.keypunchers.xa.loaders.LatestScreenshotsLoader;

public class LatestScreenshotsFragment extends Fragment implements LoaderManager.LoaderCallbacks<JSONArray> {
    private ListView mLvContent;

    public LatestScreenshotsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest_screenshots, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_latest_screenshots_title);

        mLvContent = (ListView) getActivity().findViewById(R.id.lv_latest_screenshot);

        getData(savedInstanceState);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
        return new LatestScreenshotsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        displayData(data);
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

    private void getData(Bundle savedInstanceState) {
        int LOADER_ID = getActivity().getResources().getInteger(R.integer.latest_screenshots_loader_id);

        if (getActivity().getSupportLoaderManager().getLoader(LOADER_ID) == null){
            getActivity().getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, LatestScreenshotsFragment.this);
        } else {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, savedInstanceState, LatestScreenshotsFragment.this);
        }
    }

    private void displayData(JSONArray data) {
        mLvContent.setAdapter(new LatestScreenshotsAdapter(getActivity(), data));
    }
}
