package io.keypunchers.xa.adapters;
import android.support.v7.widget.RecyclerView;
import android.content.Context;

import io.keypunchers.xa.misc.VolleySingleton;
import io.keypunchers.xa.models.Achievement;
import java.util.ArrayList;
import android.view.View;
import io.keypunchers.xa.views.ScaledImageView;
import com.squareup.picasso.Picasso;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import io.keypunchers.xa.R;
import io.keypunchers.xa.views.ScaledNetworkImageView;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Achievement item = mData.get(position);
		
		holder.mTvAchTitle.setText(item.getTitle());
		holder.mTvAchDesc.setText(item.getDescription());

        holder.mIvAchImage.setImageUrl(item.getImageUrl(), VolleySingleton.getImageLoader());

//		Picasso.with(mContext)
//				.load(item.getImageUrl())
//				.noFade()
//				.into(holder.mIvAchImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private ArrayList<Achievement> mData;
		
		ScaledNetworkImageView mIvAchImage;
		TextView mTvAchTitle;
		TextView mTvAchDesc;

        public ViewHolder(View itemView, ArrayList<Achievement> data, Context context) {
            super(itemView);
            mData = data;
            mContext = context;
			
			mIvAchImage = (ScaledNetworkImageView) itemView.findViewById(R.id.iv_achievement_image);
			mTvAchTitle = (TextView) itemView.findViewById(R.id.tv_achievement_title);
			mTvAchDesc = (TextView) itemView.findViewById(R.id.tv_achievement_desc);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        
        }
    }
}
