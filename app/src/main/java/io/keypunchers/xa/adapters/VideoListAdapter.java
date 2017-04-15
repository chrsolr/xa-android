package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.SingletonVolley;


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

        String videoId = item.replace("https://www.youtube.com/embed/", "");

        holder.mIvImage.setImageUrl("https://i3.ytimg.com/vi/" + videoId + "/0.jpg", SingletonVolley.getImageLoader());
        holder.mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).replace("embed/", "watch?v=")));
                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView mIvImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvImage = (NetworkImageView) itemView.findViewById(R.id.iv_video_list);
        }
    }
}