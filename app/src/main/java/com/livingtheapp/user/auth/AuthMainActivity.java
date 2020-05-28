package com.livingtheapp.user.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.livingtheapp.user.MainActivity;
import com.livingtheapp.user.R;

public class AuthMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_auth_main);
        
        initView();


    }
    
    void initView()
    {
        findViewById(R.id.txtGuestAcc).setOnClickListener(v -> getGuestAccount());
    }

    void getGuestAccount()
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
