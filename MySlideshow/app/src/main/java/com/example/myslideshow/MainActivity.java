package com.example.myslideshow;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageSwitcher mImageSwitcher;
    int[] mImageResources = {R.drawable.slide00,R.drawable.slide01,
            R.drawable.slide02,R.drawable.slide03,
            R.drawable.slide04,R.drawable.slide05,
            R.drawable.slide06,R.drawable.slide07,
            R.drawable.slide08,R.drawable.slide09};
    int mPosition = 0;
    boolean mIsSlideshow = false;//スライドショーの状態区分
    MediaPlayer mMediaPlayer;

    public class MainTimerTask extends TimerTask{
        @Override
        public void run(){
            if (mIsSlideshow){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        movePosition(1);
                    }
                });
            }
        }
    }

    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView =
                        new ImageView(getApplicationContext());
                return imageView;
            }
        });
        mTimer.schedule(mTimerTask, 0, 5000);
        mMediaPlayer = MediaPlayer.create(this, R.raw.getdown);
        mMediaPlayer.setLooping(true);
    }



    private void movePosition(int move){
        mPosition = mPosition + move;
        if (mPosition >= mImageResources.length){
            mPosition = 0;
        }else if(mPosition < 0){
            mPosition = mImageResources.length - 1;
        }
        mImageSwitcher.setImageResource(mImageResources[mPosition]);
    }

    public void onPrevButtonTapped(View view){
        mImageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mImageSwitcher.setOutAnimation(this,android.R.anim.fade_out);
        movePosition(-1);
        findViewById(R.id.imageView).animate().setDuration(1000).alpha(0.0f);
    }

    public void onNextButtonTapped(View view){
        mImageSwitcher.setInAnimation(this,android.R.anim.slide_in_left);
        mImageSwitcher.setOutAnimation(this,android.R.anim.slide_out_right);
        movePosition(1);
        findViewById(R.id.imageView).animate().setDuration(1000).alpha(0.0f);
    }

    public void onSlideshowButtonTapped(View view){
        mIsSlideshow = !mIsSlideshow;

        if(mIsSlideshow){
            mMediaPlayer.start();
        }else{
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(0);
        }
    }

    public void onAnimationButtonTapped(View view){
//        float y = view.getY() + 100;
//        view.animate().setDuration(1000).setInterpolator(new BounceInterpolator()).y(y);

        //view.animate().setDuration(3000).rotation(360.0f * 5.0f);   //アニメーション時間を3秒に設定　回転数の設定(5回転)
        //view.animate().setDuration(3000).scaleX(2.5f).scaleY(2.5f);拡大
        //view.animate().setDuration(3000).scaleX(200.0f).scaleY(200.0f);超拡大
        //view.animate().setDuration(3000).alpha(-200);透過
    }
}
