package io.keypunchers.xa.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Comment;

public class CommentListFragment extends Fragment {
    private final String TAG = CommentListFragment.class.getSimpleName();
    private ArrayList<Comment> mData;

    public CommentListFragment() {
    }

    public static Fragment newInstance(ArrayList<Comment> data) {
        CommentListFragment fragment = new CommentListFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);

        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.containsKey(TAG)) {
            mData = savedInstanceState.getParcelableArrayList(TAG);
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
        ListView mLvContent = (ListView) getActivity().findViewById(R.id.lv_comment_list);
        mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TAG, mData);
    }

    private class ImageListAdapter extends BaseAdapter {
        private final Context mContext;
        private final ArrayList<Comment> mData;

        ImageListAdapter(Context context, ArrayList<Comment> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Comment getItem(int position) {
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
                convertView = inflater.inflate(R.layout.row_comment_list, parent, false);

                viewHolder = new ViewHolder();
                assert convertView != null;

                viewHolder.mIvImage = (ImageView) convertView.findViewById(R.id.iv_comment_list);
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_comment_title);
                viewHolder.mDate = (TextView) convertView.findViewById(R.id.tv_comment_date);
                viewHolder.mText = (TextView) convertView.findViewById(R.id.tv_comment_text);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.mTitle.setText(mData.get(position).getTitle());
            viewHolder.mDate.setText(mData.get(position).getDate());
            viewHolder.mText.setText(mData.get(position).getText());

            ImageLoader mImageLoader = SingletonVolley.getImageLoader();
            mImageLoader.get(mData.get(position).getImageUrl(), ImageLoader.getImageListener(viewHolder.mIvImage, R.drawable.x360a_comments_notag, R.drawable.x360a_comments_notag));

            return convertView;
        }

        private class ViewHolder {
            ImageView mIvImage;
            TextView mTitle;
            TextView mDate;
            TextView mText;
        }
    }
}
