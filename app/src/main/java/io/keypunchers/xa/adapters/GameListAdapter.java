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
import io.keypunchers.xa.models.Game;


public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Game> mData;

    public GameListAdapter(Context context, ArrayList<Game> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_games, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Game item = mData.get(position);

        Picasso.with(mContext)
                .load(item.getArtwork())
                .noFade()
                .into(holder.mIvImage);

        holder.mTvTitle.setText(item.getTitle());
        holder.mTvAchCount.setText(item.getAchCount() + " Achievements");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        TextView mTvTitle;
        TextView mTvAchCount;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_games_cover);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_games_title);
            mTvAchCount = (TextView) itemView.findViewById(R.id.tv_games_ach);
        }
    }
}