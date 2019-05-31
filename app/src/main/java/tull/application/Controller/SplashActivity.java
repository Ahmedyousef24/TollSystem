package tull.application.Controller;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import tull.application.R;

public class SplashActivity extends AppCompatActivity {

    ImageView AppLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppLogo = (ImageView) findViewById(R.id.mainLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                Pair pair = new Pair<View,String>(AppLogo,"imageTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pair);
                startActivity(myIntent,options.toBundle());


            }
        }, 1000);
    }
}
