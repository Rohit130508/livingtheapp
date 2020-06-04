package com.livingtheapp.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.livingtheapp.adapter.TabsPagerAdapter;
import com.livingtheapp.user.R;

public class CategoryDescription extends AppCompatActivity {

    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_category_description);

        initView();

        TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void initView()
    {
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("name"));
    }



}
