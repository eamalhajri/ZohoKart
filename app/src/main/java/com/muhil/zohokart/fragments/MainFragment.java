package com.muhil.zohokart.fragments;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.muhil.zohokart.R;
import com.muhil.zohokart.adapters.BannerPagerAdapter;
import com.muhil.zohokart.models.PromotionBanner;
import com.muhil.zohokart.utils.DpToPxConverter;
import com.muhil.zohokart.utils.ZohokartDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment
{
    View rootView;
    ViewPager bannerViewPager;
    BannerPagerAdapter bannerPagerAdapter;
    List<PromotionBanner> promotionBanners;
    List<ImageView> pageIndicators;

    public static MainFragment getInstance(List<PromotionBanner> promotionBanners)
    {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("promotion_banners", (Serializable) promotionBanners);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    public MainFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if (rootView != null)
        {
            return rootView;
        }
        else
        {

            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            promotionBanners = (List<PromotionBanner>) getArguments().getSerializable("promotion_banners");

            bannerViewPager = (ViewPager) rootView.findViewById(R.id.banner_viewpager);
            bannerPagerAdapter = new BannerPagerAdapter(getActivity().getSupportFragmentManager(), promotionBanners);
            bannerViewPager.setAdapter(bannerPagerAdapter);
            addDots();
            bannerViewPager.addOnPageChangeListener(
                    new ViewPager.OnPageChangeListener()
                    {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                        {

                        }

                        @Override
                        public void onPageSelected(int position)
                        {
                            selectDot(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state)
                        {

                        }
                    }
            );

            return rootView;

        }
    }

    public void addDots()
    {
        pageIndicators = new ArrayList<>();
        LinearLayout pageIndicatorHolder = (LinearLayout) rootView.findViewById(R.id.pageIndicator_holder);

        for (int i = 0; i < promotionBanners.size(); i++)
        {
            ImageView pageIndicator = new ImageView(getActivity());
            pageIndicator.setImageResource(R.mipmap.ic_panorama_fish_eye_black_18dp);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DpToPxConverter.dpToPx(10), DpToPxConverter.dpToPx(10));
            pageIndicatorHolder.addView(pageIndicator, params);
            pageIndicators.add(pageIndicator);
        }
        pageIndicators.get(0).setImageResource(R.mipmap.ic_lens_black_18dp);
    }

    public void selectDot(int selectedPage) {
        for(int i = 0; i < promotionBanners.size(); i++) {
            int drawableId = (i==selectedPage)?(R.mipmap.ic_lens_black_18dp):(R.mipmap.ic_panorama_fish_eye_black_18dp);
            pageIndicators.get(i).setImageResource(drawableId);
        }
    }

}
