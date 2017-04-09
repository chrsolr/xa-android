package io.keypunchers.xa.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.NewsAdapter;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Article;
import io.keypunchers.xa.models.ArticleListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageListFragment extends Fragment {
    private Article mData;
    private final String TAG = ImageListFragment.class.getSimpleName();
    private ListView mLvContent;
    private ImageListAdapter mAdapter;

    public ImageListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelable(TAG);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        mAdapter = new ImageListAdapter(getActivity(), mData.getImageUrls());
        mLvContent = (ListView) getActivity().findViewById(R.id.lv_image_list);
        mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TAG, mData);
    }

    public static Fragment newInstance(Article data) {
        ImageListFragment fragment = new ImageListFragment();
        fragment.mData = data;
        return fragment;
    }

    private class ImageListAdapter extends BaseAdapter {
        private final Context mContext;
        private final ArrayList<String> mData;

        public ImageListAdapter(Context context, ArrayList<String> data) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_image_list, null);

                viewHolder = new ViewHolder();
                assert convertView != null;

                viewHolder.mIvImage = (NetworkImageView) convertView.findViewById(R.id.iv_image_list);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mIvImage.setImageUrl(mData.get(position), SingletonVolley.getImageLoader());

            return convertView;
        }

        private class ViewHolder {
            NetworkImageView mIvImage;
        }
    }
}
