package io.keypunchers.xa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.Comment;

public class CommentListFragment extends Fragment {
    private ArrayList<Comment> mData;
    private GenericAdapter<Comment> mAdapter;

    public CommentListFragment() {
    }

    public static Fragment newInstance(ArrayList<Comment> data) {
        CommentListFragment fragment = new CommentListFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        setAdapter();

        ListView mLvContent = (ListView) view.findViewById(R.id.lv_comment_list);
        mLvContent.setAdapter(mAdapter);
    }

    private void setAdapter() {
        mAdapter = new GenericAdapter<>(getActivity(), mData, new GenericAdapter.onSetGetView() {
            @Override
            public View onGetView(int position, View convertView, ViewGroup parent, Context context, ArrayList<?> data) {
                ViewHolder viewHolder;

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        });
    }

    private class ViewHolder {
        ImageView mIvImage;
        TextView mTitle;
        TextView mDate;
        TextView mText;
    }
}
