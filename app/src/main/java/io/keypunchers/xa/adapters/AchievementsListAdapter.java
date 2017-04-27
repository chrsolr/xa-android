package io.keypunchers.xa.adapters;
import android.support.v7.widget.*;
import android.content.*;
import io.keypunchers.xa.models.*;
import java.util.*;
import android.view.*;
import io.keypunchers.xa.*;
import io.keypunchers.xa.views.*;
import com.squareup.picasso.*;

public class AchievementsListAdapter extends RecyclerView.Adapter<AchievementsListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Achievement> mData;

    public AchievementsListAdapter(Context context, ArrayList<Achievement> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_achievements, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Achievement item = mData.get(position);
		
		Picasso.with(mContext)
				.load(item.getImageUrl())
				.noPlaceholder()
				.into(holder.mIvAchImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private ArrayList<Achievement> mData;
		
		ScaledImageView mIvAchImage;

        public ViewHolder(View itemView, ArrayList<Achievement> data, Context context) {
            super(itemView);
            mData = data;
            mContext = context;
			
			mIvAchImage = (ScaledImageView) itemView.findViewById(R.id.iv_achievement_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        
        }
    }
}
