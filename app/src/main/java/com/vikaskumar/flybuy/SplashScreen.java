package com.vikaskumar.flybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView background, logo;
    TextView appName, slogan;
    private static int Splash_screen  = 4000;
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        background = findViewById(R.id.background_img);
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.textView);
        slogan = findViewById(R.id.app_name);

        logo.setAnimation(topAnim);
        background.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        appName.setAnimation(animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent intent = new Intent(SplashScreen.this, RegisterActivity.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] =new Pair<View, String>(logo,"logo_image");
                    pairs[1] =new Pair<View, String>(logo,"logo_text");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    }
            }
        },Splash_screen);



    }
}