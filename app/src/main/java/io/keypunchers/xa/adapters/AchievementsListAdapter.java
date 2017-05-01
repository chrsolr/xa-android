package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.app.AchievementCommentsActivity;
import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.Achievement;
import io.keypunchers.xa.views.ScaledImageView;

public class AchievementsListAdapter extends RecyclerView.Adapter<AchievementsListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Achievement> mData;
    private int mResId = R.layout.row_achievements_wide;

    public AchievementsListAdapter(Context context, ArrayList<Achievement> data, int layoutId) {
        mContext = context;
        mData = data;
        mResId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(mResId, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Achievement item = mData.get(position);

        holder.mTvAchTitle.setText(item.getTitle());
        holder.mTvAchDesc.setText(item.getDescription());
        holder.mTvAchComments.setText(String.format(Locale.US, "%s %s", item.getCommentAmount(), holder.mTvAchComments.getText()).trim());
        holder.mTvAchGamerscore.setText(item.getGamescoreAmount());

        VolleySingleton
                .getImageLoader()
                .get(item.getImageUrl(),
                        ImageLoader.getImageListener(holder.mIvAchImage, 0, 0));
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
        ScaledImageView mIvAchImage;
        TextView mTvAchTitle;
        TextView mTvAchDesc;
        TextView mTvAchComments;
        TextView mTvAchGamerscore;
        private Context mContext;
        private ArrayList<Achievement> mData;

        public ViewHolder(View itemView, ArrayList<Achievement> data, Context context) {
            super(itemView);
            mData = data;
            mContext = context;

            mIvAchImage = (ScaledImageView) itemView.findViewById(R.id.iv_achievement_image);
            mTvAchTitle = (TextView) itemView.findViewById(R.id.tv_achievement_title);
            mTvAchDesc = (TextView) itemView.findViewById(R.id.tv_achievement_desc);
            mTvAchComments = (TextView) itemView.findViewById(R.id.tv_achievement_comments);
            mTvAchGamerscore = (TextView) itemView.findViewById(R.id.tv_achievement_gs);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(mContext, AchievementCommentsActivity.class).putExtra("comments_url", mData.get(getAdapterPosition()).getAchievementsPageUrl()));
        }
    }
}
