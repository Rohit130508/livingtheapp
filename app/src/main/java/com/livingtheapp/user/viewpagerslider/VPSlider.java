package com.livingtheapp.user.viewpagerslider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import com.livingtheapp.user.R;
import com.livingtheapp.user.auth.LoginActivity;
import com.livingtheapp.user.auth.RegistrationActivity;
import com.livingtheapp.user.viewpagerslider.fragments.FifthFrag;
import com.livingtheapp.user.viewpagerslider.fragments.ForthFrag;
import com.livingtheapp.user.viewpagerslider.fragments.ScreenSlidePageFragment;
import com.livingtheapp.user.viewpagerslider.fragments.SecondScreen;
import com.livingtheapp.user.viewpagerslider.fragments.SeventhFrag;
import com.livingtheapp.user.viewpagerslider.fragments.SixthFrag;
import com.livingtheapp.user.viewpagerslider.fragments.ThirdFrag;

public class VPSlider extends AppCompatActivity {

    private static final int NUM_PAGES = 7;

    private ViewPager mPager;

    private int images_vp[] = {R.drawable.vp1, R.drawable.vp2, R.drawable.vp3,
            R.drawable.vp4, R.drawable.vp5, R.drawable.vp6,
            R.drawable.vp7};

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;
    private SliderPagerAdapter myCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeVP);
        setContentView(R.layout.activity_v_p_slider);

        mPager = findViewById(R.id.photos_viewpager);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        myCustomPagerAdapter = new SliderPagerAdapter(this, images_vp);
//        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(myCustomPagerAdapter);

        CircleIndicator indicator =  findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        findViewById(R.id.btnRegister).setOnClickListener(v -> getRegisterPage());
        findViewById(R.id.btnSignIn).setOnClickListener(v -> getLoginPage());


    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
                return new ScreenSlidePageFragment();
            else if(position == 1)
                return new SecondScreen();
            else if(position == 2)
                return new ThirdFrag();
            else if(position == 3)
                return new ForthFrag();
            else if(position == 4)
                return new FifthFrag();
            else if(position == 5)
                return new SixthFrag();
            else
                return new SeventhFrag();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }


    class SliderPagerAdapter extends PagerAdapter {
        Context context;
        int[] images;
        LayoutInflater layoutInflater;


        public SliderPagerAdapter(Context context, int[] images) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.vp_maincat_item, container, false);

            ImageView iv_vp_slider = itemView.findViewById(R.id.iv_vp_slider);
            iv_vp_slider.setImageResource(images[position]);

            container.addView(itemView);

            //listening to image click
            iv_vp_slider.setOnClickListener(v -> {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            });

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    void getRegisterPage()
    {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
    void getLoginPage()
    {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
