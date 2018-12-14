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
import me.christiansoler.xa.models.Game;


public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private ArrayList<Game> mData;

    public GameListAdapter(ArrayList<Game> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_browse_games, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Game item = mData.get(position);

		String platform = item.getPlatform() == null ? "" : item.getPlatform();
		
        holder.mTvTitle.setText(item.getTitle());
        holder.mTvAchCount.setText(String.format(Locale.US, "%s achievements", item.getAchCount()));
        holder.mTvGsCount.setText(String.format(Locale.US, "%s points", item.getGsCount()));
		holder.mTvPlatform.setText(String.format(Locale.US, "%s", platform));

        VolleySingleton
                .getImageLoader()
                .get(item.getArtwork(),
                        ImageLoader.getImageListener(holder.mIvImage, 0, 0));
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
        ImageView mIvImage;
        TextView mTvTitle;
        TextView mTvAchCount;
        TextView mTvGsCount;
		TextView mTvPlatform;
        private Context mContext;
        private ArrayList<Game> mData;

        public ViewHolder(View itemView, ArrayList<Game> data, Context context) {
            super(itemView);

            mContext = context;
            mData = data;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_games_cover);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_games_title);
            mTvAchCount = (TextView) itemView.findViewById(R.id.tv_games_ach);
            mTvGsCount = (TextView) itemView.findViewById(R.id.tv_games_gs_count);
			mTvPlatform = (TextView) itemView.findViewById(R.id.tv_games_platform);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View p1) {
            mContext.startActivity(new Intent(mContext, AchievementsActivity.class).putExtra("game_permalink", mData.get(getAdapterPosition()).getGamePermalink()));
        }
    }
}
