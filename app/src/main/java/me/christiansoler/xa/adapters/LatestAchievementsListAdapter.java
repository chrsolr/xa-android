package me.christiansoler.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Locale;

import me.christiansoler.xa.R;
import me.christiansoler.xa.app.AchievementsActivity;
import me.christiansoler.xa.misc.VolleySingleton;
import me.christiansoler.xa.models.LatestAchievement;


public class LatestAchievementsListAdapter extends RecyclerView.Adapter<LatestAchievementsListAdapter.ViewHolder> {
    private ArrayList<LatestAchievement> mData;

    public LatestAchievementsListAdapter(ArrayList<LatestAchievement> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_latest_achievements, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final LatestAchievement item = mData.get(position);

        holder.mTvTitle.setText(item.getTitle());
        holder.mTvAchCount.setText(item.getAchievementsCount());
        holder.mTvGsCount.setText(item.getGamerscoreCount());
        holder.mTvSubmittedBy.setText(String.format(Locale.US, "Submitted By: %s", item.getSubmittedBy()));

        VolleySingleton
                .getImageLoader()
                .get(item.getImageUrl(),
                        ImageLoader.getImageListener(holder.mIvCover, 0, 0));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvCover;
        TextView mTvTitle;
        TextView mTvAchCount;
        TextView mTvGsCount;
        TextView mTvSubmittedBy;
        private Context mContext;
        private ArrayList<LatestAchievement> mData;

        public ViewHolder(View itemView, ArrayList<LatestAchievement> data, Context context) {
            super(itemView);

            mData = data;
            mContext = context;

            mIvCover = (ImageView) itemView.findViewById(R.id.iv_la_cover);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_la_title);
            mTvAchCount = (TextView) itemView.findViewById(R.id.tv_la_ach_count);
            mTvGsCount = (TextView) itemView.findViewById(R.id.tv_la_gs_count);
            mTvSubmittedBy = (TextView) itemView.findViewById(R.id.tv_la_submitted_by);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mContext.startActivity(new Intent(mContext, AchievementsActivity.class).putExtra("game_permalink", mData.get(getAdapterPosition()).getGamePermalink()));
        }
    }
}
