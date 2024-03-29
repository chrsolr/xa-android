package me.christiansoler.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.misc.VolleySingleton;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mData;

    public VideoListAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_video_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String item = mData.get(position);

        final String url = Common.getYouTubeImage(item);
        final String yt_watch_link = Common.getYouTubeWatchLink(item);

        VolleySingleton.getImageLoader()
                .get(url, ImageLoader.getImageListener(holder.mIvImage, 0, 0));

        holder.mIvPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(yt_watch_link));
                mContext.startActivity(mIntent);
            }
        });

        holder.mIvShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check it out!");
                intent.putExtra(Intent.EXTRA_TEXT, yt_watch_link + "\n\nShared via XA App.");
                mContext.startActivity(Intent.createChooser(intent, "Share"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        ImageView mIvPlayIcon;
        ImageView mIvShareIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_video_list);
            mIvPlayIcon = (ImageView) itemView.findViewById(R.id.iv_video_list_play_icon);
            mIvShareIcon = (ImageView) itemView.findViewById(R.id.iv_video_list_share_icon);
        }
    }
}
