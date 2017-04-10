package io.keypunchers.xa.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.loaders.ScreenshotsLoader;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Screenshot;

public class ScreenshotsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Screenshot>> {
    private ListView mLvContent;
    private ArrayList<Screenshot> mData;
    private String TAG = ScreenshotsFragment.class.getSimpleName();
    private int LOADER_ID;
    private String BASE_URL;
    private String AB_TITLE;

    public ScreenshotsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screenshots, container, false);

        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelableArrayList(TAG);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOADER_ID = getActivity().getResources().getInteger(R.integer.latest_screenshots_loader_id);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            AB_TITLE = getArguments().getString("ab_title");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        getData(savedInstanceState);
    }

    @Override
    public Loader<ArrayList<Screenshot>> onCreateLoader(int id, Bundle args) {
        return new ScreenshotsLoader(getActivity(), BASE_URL, mData);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Screenshot>> loader, ArrayList<Screenshot> data) {
        mData = data;
        setupUI();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Screenshot>> loader) {
        //getLoaderManager().destroyLoader(LOADER_ID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG, mData);
    }

    private void setupUI() {
        mLvContent = (ListView) getActivity().findViewById(R.id.lv_latest_screenshot);
        mLvContent.setAdapter(new ScreenshotsAdapter(getActivity(), mData));
    }

    private void getData(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelableArrayList(TAG);
            setupUI();
        } else {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private class ScreenshotsAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Screenshot> mData;

        ScreenshotsAdapter(Context context, ArrayList<Screenshot> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Screenshot getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_screenshots, parent, false);

                viewHolder = new ViewHolder();
                assert convertView != null;
                viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_latest_screenshot_title);
                viewHolder.mTvSubTitle = (TextView) convertView.findViewById(R.id.tv_latest_screenshot_subtitle);
                viewHolder.mIvScreenshot = (NetworkImageView) convertView.findViewById(R.id.iv_latest_screenshot_image);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Screenshot screenshot = mData.get(position);

            viewHolder.mTvTitle.setText(screenshot.getTitle());
            viewHolder.mTvSubTitle.setText(screenshot.getSubtitle());
            viewHolder.mIvScreenshot.setImageUrl(screenshot.getImageUrl(), SingletonVolley.getImageLoader());

            return convertView;
        }

        private class ViewHolder {
            TextView mTvTitle;
            TextView mTvSubTitle;
            NetworkImageView mIvScreenshot;
        }
    }
}
