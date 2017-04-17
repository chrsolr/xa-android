package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.models.LatestAchievement;


public class LatestAchievementsListAdapter extends RecyclerView.Adapter<LatestAchievementsListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<LatestAchievement> mData;

    public LatestAchievementsListAdapter(Context context, ArrayList<LatestAchievement> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_latest_achievements, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LatestAchievement item = mData.get(position);

        Picasso.with(mContext).load(item.getImageUrl()).noFade().into(holder.mIvCover);

        holder.mTvTitle.setText(item.getTitle());
        holder.mTvAchCount.setText(item.getAchievementsCount());
        holder.mTvGsCount.setText(item.getGamerscoreCount());
        holder.mTvSubmittedBy.setText("Submitted By: " + item.getSubmittedBy());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvCover;
        TextView mTvTitle;
        TextView mTvAchCount;
        TextView mTvGsCount;
        TextView mTvSubmittedBy;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvCover = (ImageView) itemView.findViewById(R.id.iv_la_cover);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_la_title);
            mTvAchCount = (TextView) itemView.findViewById(R.id.tv_la_ach_count);
            mTvGsCount = (TextView) itemView.findViewById(R.id.tv_la_gs_count);
            mTvSubmittedBy = (TextView) itemView.findViewById(R.id.tv_la_submitted_by);
        }
    }
}