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
import io.keypunchers.xa.views.ScaledImageView;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> mData;

    public ImageListAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_image_list, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String item = mData.get(position);

        Picasso.with(mContext)
			.load(item)
			.noFade()
			.error(R.drawable.ic_app_logo)
			.into(holder.mIvImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
	
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private Context mContext;
        private ArrayList<String> mData;

        ScaledImageView mIvImage;

        public ViewHolder(View itemView, ArrayList<String> data, Context context) {
            super(itemView);
			mData = data;
            mContext = context;

            mIvImage = (ScaledImageView) itemView.findViewById(R.id.iv_image_list);
			
			itemView.setOnClickListener(this);
        }
		
		@Override
        public void onClick(View v) {
        	Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(getAdapterPosition())));
			mContext.startActivity(mIntent);
        }
    }
}
