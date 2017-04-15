package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.app.ArticleActivity;
import io.keypunchers.xa.misc.SingletonVolley;
import io.keypunchers.xa.models.ArticleListItem;


public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private ArrayList<ArticleListItem> mData;

    public ArticleListAdapter(ArrayList<ArticleListItem> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_article_list, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArticleListItem item = mData.get(position);

        holder.mTvTitle.setText(item.getTitle());
        holder.mTvSubTitle.setText(item.getDesc());
        holder.mIvImage.setImageUrl(item.getImageUrl(), SingletonVolley.getImageLoader());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context mContext;
        ArrayList<ArticleListItem> mData;

        TextView mTvTitle;
        TextView mTvSubTitle;
        NetworkImageView mIvImage;

        public ViewHolder(View itemView, ArrayList<ArticleListItem> data, Context context) {
            super(itemView);
            mData = data;
            mContext = context;

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_news_subtitle);
            mIvImage = (NetworkImageView) itemView.findViewById(R.id.iv_news_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String url = mData.get(getAdapterPosition()).getPageUrl();
            mContext.startActivity(new Intent(mContext, ArticleActivity.class).putExtra("url", url));
        }
    }
}