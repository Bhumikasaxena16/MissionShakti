package com.upicon.app.BasicActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.upicon.app.Agent.AgentDashboard;
import com.upicon.app.AppController.BaseURL;
import com.upicon.app.AppController.SessionManager;
import com.upicon.app.Dashboards.DashBoard;
import com.upicon.app.R;
import com.upicon.app.UtilsMethod.UtilsMethod;
import java.util.HashMap;


public class SplashScreen extends AppCompatActivity {

    SessionManager sessionManager;
    HashMap<String, String> user;
    ImageView iv_logo;
    TextView tv_app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        changeStatusBarColor();
        initialization();

    }

    private void initialization() {

        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        iv_logo=(ImageView)findViewById(R.id.iv_logo);
        tv_app_name = (TextView) findViewById(R.id.tv_app_name);

        Animation img = new TranslateAnimation(Animation.ABSOLUTE,Animation.ABSOLUTE, 150,Animation.ABSOLUTE);
        img.setDuration(1000);
        img.setFillAfter(true);
        tv_app_name.startAnimation(img);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cycle_7);
        iv_logo.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(BaseURL.isOnline(getApplicationContext())){
                    StartActivity();
                }
                else{
                    UtilsMethod.INSTANCE.errorToast(SplashScreen.this,"Please turn on data connection");
                }
            }
        },1000);

    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void StartActivity() {
        if(sessionManager.isLoggedIn()){
            Intent intent=new Intent(getApplicationContext(), DashBoard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
        else {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}