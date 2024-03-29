package me.christiansoler.xa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import me.christiansoler.xa.R;
import me.christiansoler.xa.misc.VolleySingleton;
import me.christiansoler.xa.models.Comment;
import java.util.Locale;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private ArrayList<Comment> mData;

    public CommentListAdapter(ArrayList<Comment> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_comment_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment item = mData.get(position);

        holder.mTitle.setText(item.getTitle());
        holder.mDate.setText(item.getDate());
        holder.mText.setText(item.getText());
		holder.mCount.setText(String.format(Locale.US, "Comment #%s", position + 1));

        VolleySingleton
                .getImageLoader()
                .get(item.getImageUrl(),
                        ImageLoader.getImageListener(holder.mIvImage, R.drawable.x360a_comments_notag, R.drawable.x360a_comments_notag));
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
        TextView mTitle;
        TextView mDate;
		TextView mCount;
        TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_comment_image);
            mTitle = (TextView) itemView.findViewById(R.id.tv_comment_title);
            mDate = (TextView) itemView.findViewById(R.id.tv_comment_date);
			mCount = (TextView) itemView.findViewById(R.id.tv_comment_count);
            mText = (TextView) itemView.findViewById(R.id.tv_comment_text);
        }
    }
}
