package com.evoxlk.sliitgo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppStart extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    //variables
    Animation topAnim, bottomAnim, bottomLate;
    ImageView image;
    TextView logo, logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_start);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        bottomLate = AnimationUtils.loadAnimation(this,R.anim.bottom_late_animation);

        //images and text
        image = findViewById(R.id.main);
        logo = findViewById(R.id.logo);
        logo2 = findViewById(R.id.logo2);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        logo2.setAnimation(bottomLate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AppStart.this, Login.class);
                startActivity(intent);
                finish();
            }


    } ,SPLASH_SCREEN);

    }
}