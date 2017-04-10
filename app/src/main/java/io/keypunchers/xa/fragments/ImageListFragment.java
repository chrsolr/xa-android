package io.keypunchers.xa.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Article;

public class ImageListFragment extends Fragment {
    private final String TAG = ImageListFragment.class.getSimpleName();
    private ArrayList<String> mData;

    public ImageListFragment() {
    }

    public static Fragment newInstance(ArrayList<String> data) {
        ImageListFragment fragment = new ImageListFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getStringArrayList(TAG);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        ImageListAdapter mAdapter = new ImageListAdapter(getActivity(), mData);
        ListView mLvContent = (ListView) getActivity().findViewById(R.id.lv_image_list);
        mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(TAG, mData);
    }

    private class ImageListAdapter extends BaseAdapter {
        private final Context mContext;
        private final ArrayList<String> mData;

        ImageListAdapter(Context context, ArrayList<String> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_image_list, parent, false);

                viewHolder = new ViewHolder();
                assert convertView != null;

                viewHolder.mIvImage = (NetworkImageView) convertView.findViewById(R.id.iv_image_list);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mIvImage.setImageUrl(mData.get(position), SingletonVolley.getImageLoader());
            viewHolder.mIvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position)));
                    startActivity(mIntent);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            NetworkImageView mIvImage;
        }
    }
}
