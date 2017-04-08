package io.keypunchers.xa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.ArticleListItem;

public class NewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<ArticleListItem> mData;

    public NewsAdapter(Context context, List<ArticleListItem> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ArticleListItem getItem(int position) {
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
            convertView = inflater.inflate(R.layout.row_news, null);

            viewHolder = new ViewHolder();
            assert convertView != null;

            viewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            viewHolder.mTvSubTitle = (TextView) convertView.findViewById(R.id.tv_news_subtitle);
            viewHolder.mIvImage = (NetworkImageView) convertView.findViewById(R.id.iv_news_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ArticleListItem item = mData.get(position);

        viewHolder.mTvTitle.setText(item.getTitle());
        viewHolder.mTvSubTitle.setText(item.getDesc());
        viewHolder.mIvImage.setImageUrl(item.getImageUrl(), SingletonVolley.getImageLoader());

        return convertView;
    }

    private class ViewHolder {
        TextView mTvTitle;
        TextView mTvSubTitle;
        NetworkImageView mIvImage;
    }
}
