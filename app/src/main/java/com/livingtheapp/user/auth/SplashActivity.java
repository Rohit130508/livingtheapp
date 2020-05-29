package com.livingtheapp.user.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.livingtheapp.user.MainActivity;
import com.livingtheapp.user.R;
import com.livingtheapp.user.viewpagerslider.VPSlider;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{

    Animation downtoup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_splash);

        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        ImageView imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setAnimation(downtoup);

        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            Intent i = new Intent(SplashActivity.this, VPSlider.class);
            startActivity(i);
            finish();
        }, 5000);
    }
}
