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
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.loaders.ScreenshotsLoader;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Screenshot;

public class ScreenshotsFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Screenshot>> {
    private ListView mLvContent;
    private ArrayList<Screenshot> mData = new ArrayList<>();
    private String TAG = ScreenshotsFragment.class.getSimpleName();
    private int LOADER_ID;
    private String BASE_URL;
    private String AB_TITLE;
    private GenericAdapter<Screenshot> mAdapter;

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

        setAdapter();

        mLvContent = (ListView) getActivity().findViewById(R.id.lv_latest_screenshot);
        mLvContent.setAdapter(mAdapter);
    }

    private void setAdapter() {
        mAdapter = new GenericAdapter<>(getActivity(), mData, new GenericAdapter.onSetGetView() {
            @Override
            public View onGetView(int position, View convertView, ViewGroup parent, Context context, ArrayList<?> data) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LOADER_ID = getActivity().getResources().getInteger(R.integer.latest_screenshots_loader_id);

        if (mData.isEmpty() && getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            AB_TITLE = getArguments().getString("ab_title");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        if (mData.isEmpty()) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<ArrayList<Screenshot>> onCreateLoader(int id, Bundle args) {
        return new ScreenshotsLoader(getActivity(), BASE_URL, mData);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Screenshot>> loader, ArrayList<Screenshot> data) {
        mData = data;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Screenshot>> loader) {
    }

    private class ViewHolder {
        TextView mTvTitle;
        TextView mTvSubTitle;
        NetworkImageView mIvScreenshot;
    }
}
