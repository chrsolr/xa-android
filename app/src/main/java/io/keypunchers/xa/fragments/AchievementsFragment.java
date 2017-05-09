package io.keypunchers.xa.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.AchievementsListAdapter;
import io.keypunchers.xa.models.Achievement;
import io.keypunchers.xa.models.GameDetails;
import io.keypunchers.xa.views.ScaledImageView;

public class AchievementsFragment extends Fragment {
    private GameDetails mData;
    private ScaledImageView mIvBanner;
    private ImageView mIvGameCover;
    private TextView mTvGameTitle;
    private TextView mTvGameGenres;
    private TextView mTvAchAmount;
    private RecyclerView mRvContent;
    private Target mTarget;

    public AchievementsFragment() {
    }

    public static Fragment newInstance(GameDetails data) {
        AchievementsFragment fragment = new AchievementsFragment();
        fragment.mData = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        mIvBanner = (ScaledImageView) view.findViewById(R.id.iv_game_achievements_banner);
        mIvGameCover = (ImageView) view.findViewById(R.id.iv_game_achievements_cover);
        mTvGameTitle = (TextView) view.findViewById(R.id.tv_game_ach_title);
        mTvGameGenres = (TextView) view.findViewById(R.id.tv_game_ach_genres);
        mTvAchAmount = (TextView) view.findViewById(R.id.tv_game_ach_amount);
        mRvContent = (RecyclerView) view.findViewById(R.id.rv_achievements);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Collections.sort(mData.getAchievements(), new Comparator<Achievement>() {
            @Override
            public int compare(Achievement a, Achievement b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        setupUI();
    }

    private void setupUI() {
        if (mData.getBanner() != null)
            Picasso.with(getActivity())
                    .load(mData.getBanner())
                    .noFade()
                    .error(R.drawable.promo_banner)
                    .into(mIvBanner);
        else
            mIvBanner.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.promo_banner));

        if (mData.getImageUrl() != null)
            Picasso.with(getActivity())
                    .load(mData.getImageUrl())
                    .noFade()
                    .into(mIvGameCover);
        else
            mIvGameCover.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.promo_banner));

        mTvGameTitle.setText(mData.getTitle());
        mTvGameGenres.setText(TextUtils.join("/", mData.getGenres()));
        mTvAchAmount.setText(String.format(Locale.US, "%s achievements", mData.getAchievements().size()));

        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap != null) {
                    AchievementsListAdapter mAdapter;

                    if (bitmap.getWidth() < 100)
                        mAdapter = new AchievementsListAdapter(getActivity(), mData.getAchievements(), R.layout.row_achievements_square);
                    else
                        mAdapter = new AchievementsListAdapter(getActivity(), mData.getAchievements(), R.layout.row_achievements_wide);

                    mRvContent.setAdapter(mAdapter);
                    mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mData.getAchievements().size());
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(getActivity())
                .load(mData.getAchievements().get(0).getImageUrl())
                .noFade()
                .into(mTarget);
    }
}
