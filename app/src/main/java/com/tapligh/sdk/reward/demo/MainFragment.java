package com.tapligh.sdk.reward.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tapligh.sdk.adview.Tapligh;
import com.tapligh.sdk.adview.adutils.ADResultListener;
import com.tapligh.sdk.adview.adutils.AdLoadListener;

/**
 * CREATED BY Javadroid FOR `Tapligh Reward Sample` PROJECT
 * AT: 2019/Nov/13 10:45
 */
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String KEY_AD_TYPE = "AD_TYPE";

    public static final int AD_TYPE_REWARD = 1;
    public static final int AD_TYPE_BANNER = 2;

    private static final String VIDEO_REWARD_CLOSABLE = "8b0a15ff-7bfb-487b-b44f-beeba13d7f4c";
    private static final String VIDEO_REWARD_SKIPPABLE = "eb8ee154-5fcf-4fe4-beb3-add7332130a2";
    private static final String VIDEO_REWARD_FORCELINK = "452f45c4-9398-4e25-8d0f-6bff30d2d5f8";
    private static final String VIDEO_INTERSITIAL_TIMEBASED_SKIPPABLE_FORCELINK = "9d1d973b-15c7-4fca-83b3-d4cda8bf53ca";
    private static final String VIDEO_INTERSTITIAL_REPEAT_SKIPPABLE_FORCELINK = "536c54b7-9f65-46d1-8ba0-659ef6aff2ab";

    private static final String BANNER_ON_REQUEST_SHOW = "faba3a93-6618-479e-8c47-c6459bb7f578";
    private static final String BANNER_INTERSTITIAL_TIMEBASED = "85d83e5a-042c-44c7-8d1c-65c5ac2b9f90";
    private static final String BANNER_INTESTITIAL_REPEAT = "3b757912-7b49-4af0-9de3-a85c53293ab1";

    private ProgressBar progress;
    private Tapligh tapligh;


    private int adType;

    public static MainFragment getInstance(int type) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_AD_TYPE, type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            adType = getArguments() != null ? getArguments().getInt(KEY_AD_TYPE) : 0;
        } else {
            adType = savedInstanceState.getInt(KEY_AD_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (adType == AD_TYPE_REWARD) {
            return inflater.inflate(R.layout.fragment_reward, container, false);
        } else if (adType == AD_TYPE_BANNER) {
            return inflater.inflate(R.layout.fragment_banner, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_empty, container, false);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tapligh = Tapligh.newInstance(requireActivity());

        progress = view.findViewById(R.id.progressbar);

        if (adType == AD_TYPE_REWARD) {
            initialReward(view);
        } else if (adType == AD_TYPE_BANNER) {
            initialBanner(view);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            adType = savedInstanceState.getInt(KEY_AD_TYPE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_AD_TYPE, adType);
        super.onSaveInstanceState(outState);
    }


    private void initialReward(View view) {
        view.findViewById(R.id.video_reward_closable).setOnClickListener(l -> loadAd(VIDEO_REWARD_CLOSABLE, true));
        view.findViewById(R.id.video_reward_skippable).setOnClickListener(l -> loadAd(VIDEO_REWARD_SKIPPABLE, true));
        view.findViewById(R.id.video_reward_forcelink).setOnClickListener(l -> loadAd(VIDEO_REWARD_FORCELINK, true));
        view.findViewById(R.id.video_interstitial_time).setOnClickListener(l -> loadAd(VIDEO_INTERSITIAL_TIMEBASED_SKIPPABLE_FORCELINK, true));
        view.findViewById(R.id.video_interstitial_repeat).setOnClickListener(l -> loadAd(VIDEO_INTERSTITIAL_REPEAT_SKIPPABLE_FORCELINK, true));

    }

    private void initialBanner(View view) {
        view.findViewById(R.id.banner_on_request).setOnClickListener(l -> loadAd(BANNER_ON_REQUEST_SHOW, true));
        view.findViewById(R.id.banner_interstitial_time).setOnClickListener(l -> loadAd(BANNER_INTERSTITIAL_TIMEBASED, true));
        view.findViewById(R.id.banner_interstitial_repeat).setOnClickListener(l -> loadAd(BANNER_INTESTITIAL_REPEAT, true));
    }


    private void loadAd(final String unitCode, final boolean showImmediately) {
        progress.setVisibility(View.VISIBLE);

        tapligh.loadAd(unitCode, new AdLoadListener() {
            @Override
            public void onAdReady(String unitCode, String token) {
                Log.d(TAG, "onAdReady() called with: s = [" + unitCode + "], s1 = [" + token + "]");

                if (showImmediately) {
                    showAd(unitCode);
                } else {
                    progress.setVisibility(View.INVISIBLE);
                    showToast(unitCode + "-> AVAILABLE");
                }
            }

            @Override
            public void onLoadError(String unitCode, LoadErrorStatus loadErrorStatus) {
                Log.d(TAG, "onLoadError() called with: s = [" + unitCode + "], loadErrorStatus = [" + loadErrorStatus + "]");
                progress.setVisibility(View.INVISIBLE);

                String text = "";

                if (loadErrorStatus == LoadErrorStatus.adUnitNotReady) {
                    if (VIDEO_INTERSTITIAL_REPEAT_SKIPPABLE_FORCELINK.equals(unitCode))
                        text = "interstitial ad, Not Ready. Try one more click" ;

                    else if (BANNER_INTESTITIAL_REPEAT.equals(unitCode))
                        text = "interstitial ad, Not Ready. Try one more click" ;
                    else
                        text = unitCode + "-> ERROR: " + loadErrorStatus.name();
                } else {
                    text = unitCode + "-> ERROR: " + loadErrorStatus.name();
                }

                showToast(text);
            }
        });


    }

    private void showAd(final String unitCode) {
        tapligh.showAd(unitCode, new ADResultListener() {
            @Override
            public void onAdResult(ADResult adResult, String s) {
                progress.setVisibility(View.INVISIBLE);
                showToast(unitCode + "-> RESULT:" + adResult.name());
            }

            @Override
            public void onRewardReady(String s) {
                showToast(unitCode + "-> reward: " + s);
            }
        });
    }


    private void showToast(String text) {
        if (getContext() != null && isAdded()) {
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        }
    }


}
