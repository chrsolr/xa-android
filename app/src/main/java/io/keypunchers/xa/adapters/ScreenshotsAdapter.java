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
import io.keypunchers.xa.models.Screenshot;


public class ScreenshotsAdapter extends RecyclerView.Adapter<ScreenshotsAdapter.ViewHolder> {
    private ArrayList<Screenshot> mData;

    public ScreenshotsAdapter(ArrayList<Screenshot> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_screenshots, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Screenshot screenshot = mData.get(position);

        holder.mTvTitle.setText(screenshot.getTitle());
        holder.mTvSubTitle.setText(screenshot.getSubtitle());
        holder.mIvScreenshot.setImageUrl(screenshot.getImageUrl(), SingletonVolley.getImageLoader());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        TextView mTvSubTitle;
        NetworkImageView mIvScreenshot;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_latest_screenshot_title);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_latest_screenshot_subtitle);
            mIvScreenshot = (NetworkImageView) itemView.findViewById(R.id.iv_latest_screenshot_image);
        }
    }
}