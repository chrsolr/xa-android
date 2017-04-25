package io.keypunchers.xa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.models.LatestScreenshot;
import io.keypunchers.xa.views.ScaledImageView;
import io.keypunchers.xa.misc.*;
import android.widget.*;
import java.util.*;


public class LatestScreenshotsAdapter extends RecyclerView.Adapter<LatestScreenshotsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<LatestScreenshot> mData;

    public LatestScreenshotsAdapter(Context context, ArrayList<LatestScreenshot> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_latest_screenshots, parent, false);

        return new ViewHolder(view, context, mData);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LatestScreenshot screenshot = mData.get(position);

        holder.mTvTitle.setText(screenshot.getTitle());
        holder.mTvSubTitle.setText(screenshot.getDateAdded());
        Picasso.with(mContext).load(screenshot.getImageUrl()).into(holder.mIvScreenshot);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private Context mContext;
		private ArrayList<LatestScreenshot> mData;

        TextView mTvTitle;
        TextView mTvSubTitle;
        ScaledImageView mIvScreenshot;

        public ViewHolder(View itemView, Context context, ArrayList<LatestScreenshot> data) {
            super(itemView);
			mContext = context;
			mData = data;

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_latest_screenshot_title);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_latest_screenshot_subtitle);
            mIvScreenshot = (ScaledImageView) itemView.findViewById(R.id.iv_latest_screenshot_image);
			
			itemView.setOnClickListener(this);
        }
		
		@Override
		public void onClick(View v)
		{
			String game_permalink = mData.get(getAdapterPosition()).getGamePermalink();
			String screenshots_url = Common.getGameScreenshotsUrlByPermalink(game_permalink);
			
			Toast.makeText(mContext, String.format(Locale.US, "Game Permalink: %s", game_permalink), Toast.LENGTH_LONG).show();
		}
    }
}
