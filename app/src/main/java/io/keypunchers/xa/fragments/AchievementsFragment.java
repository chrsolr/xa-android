package io.keypunchers.xa.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.ImageListAdapter;
import io.keypunchers.xa.models.GameDetails;
import io.keypunchers.xa.adapters.AchievementsListAdapter;
import io.keypunchers.xa.views.ScaledNetworkImageView;
import com.android.volley.toolbox.NetworkImageView;
import android.widget.TextView;
import io.keypunchers.xa.misc.VolleySingleton;
import android.text.TextUtils;
import java.util.Locale;

public class AchievementsFragment extends Fragment {
    private GameDetails mData;

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

		setupUI(view);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private void setupUI(View view) {
        ScaledNetworkImageView mIvBanner = (ScaledNetworkImageView) view.findViewById(R.id.iv_game_achievements_banner);
        NetworkImageView mIvGameCover = (NetworkImageView) view.findViewById(R.id.iv_game_achievements_cover);
        TextView mTvGameTitle = (TextView) view.findViewById(R.id.tv_game_ach_title);
        TextView mTvGameGenres = (TextView) view.findViewById(R.id.tv_game_ach_genres);
        TextView mTvAchAmount = (TextView) view.findViewById(R.id.tv_game_ach_amount);

        mIvBanner.setImageUrl(mData.getBanner(), VolleySingleton.getImageLoader());
        mIvGameCover.setImageUrl(mData.getImageUrl(), VolleySingleton.getImageLoader());

        mTvGameTitle.setText(mData.getTitle());
        mTvGameGenres.setText(TextUtils.join("/", mData.getGenres()));
        mTvAchAmount.setText(String.format(Locale.US, "%s Achievements", mData.getAchievements().size()));
		
		AchievementsListAdapter mAdapter = new AchievementsListAdapter(getActivity(), mData.getAchievements(), R.layout.row_achievements_wide);
        RecyclerView mRvContent = (RecyclerView) view.findViewById(R.id.rv_achievements);
        mRvContent.setAdapter(mAdapter);
        mRvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
