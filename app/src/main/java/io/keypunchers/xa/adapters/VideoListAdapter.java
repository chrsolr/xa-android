package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.keypunchers.xa.R;


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
        String item = mData.get(position);

        if (item.contains("?")) {
            item = item.substring(0, item.indexOf("?"));
        }

        final String videoId = item.replace("https://www.youtube.com/embed/", "");

        Picasso.with(mContext).load("https://i3.ytimg.com/vi/" + videoId + "/0.jpg").noFade().into(holder.mIvImage);
        holder.mIvPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                mContext.startActivity(mIntent);
            }
        });

        holder.mIvShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check it out!");
                intent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + videoId + "\n\nShared via XA App.");
                mContext.startActivity(Intent.createChooser(intent, "Share"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
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