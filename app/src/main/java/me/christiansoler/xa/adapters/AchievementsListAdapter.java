package me.christiansoler.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Locale;

import me.christiansoler.xa.R;
import me.christiansoler.xa.app.AchievementCommentsActivity;
import me.christiansoler.xa.misc.VolleySingleton;
import me.christiansoler.xa.models.Achievement;
import me.christiansoler.xa.views.ScaledImageView;

public class AchievementsListAdapter extends RecyclerView.Adapter<AchievementsListAdapter.ViewHolder> {
    private ArrayList<Achievement> mData;
    private int mResId = R.layout.row_achievements_wide;

    public AchievementsListAdapter(ArrayList<Achievement> data, int layoutId) {
        mData = data;
        mResId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(mResId, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Achievement item = mData.get(position);

        holder.mTvAchTitle.setText(item.getTitle());
        holder.mTvAchDesc.setText(item.getDescription());
        holder.mTvAchGamerscore.setText(item.getGamescoreAmount());
		holder.mTvAchComments.setText(String.format(Locale.US, "%s", item.getCommentAmount()).trim());

        VolleySingleton
                .getImageLoader()
                .get(item.getImageUrl(),
                        ImageLoader.getImageListener(holder.mIvAchImage, 0, R.drawable.promo_banner));
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
            Achievement ach = mData.get(getAdapterPosition());

            Intent intent = new Intent(mContext, AchievementCommentsActivity.class);
            intent.putExtra("ACHIEVEMENT", ach);
            mContext.startActivity(intent);
        }
    }
}
