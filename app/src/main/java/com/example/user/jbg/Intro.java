package com.example.user.jbg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Intro extends AppCompatActivity {

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final ImageView iv = (ImageView)findViewById(R.id.intro);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation
                        (getApplicationContext(), // 현재화면 제어권자
                                R.anim.alpha_anim);      // 에니메이션 설정한 파일
                iv.startAnimation(anim);
            }
        },1800);



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv.setImageDrawable(null); //기존 이미지 없애기


                Intent intent = new Intent(Intro.this, Login.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();;

            }
        },2000);
    }
}
