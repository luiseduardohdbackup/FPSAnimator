package com.daasuu.FPSAnimator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daasuu.library.FPSTextureView;
import com.daasuu.library.callback.AnimCallBack;
import com.daasuu.library.parabolicmotion.ParabolicMotionBitmap;
import com.daasuu.library.parabolicmotion.ParabolicMotionText;
import com.daasuu.library.util.Util;

import java.util.Timer;
import java.util.TimerTask;

public class ParabolicMotionSampleActivity extends AppCompatActivity {

    private FPSTextureView mFPSTextureView;
    private Timer mTimer;
    private Bitmap mBitmap;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ParabolicMotionSampleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parabolic_motion);
        mFPSTextureView = (FPSTextureView) findViewById(R.id.animation_texture_view);

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        paint.setTextSize(Util.convertDpToPixel(20, this));
        final ParabolicMotionText parabolicMotionText = new ParabolicMotionText("Text", paint);
        parabolicMotionText
                .transform(800, 800)
                .initialVelocityY(-40);

        mFPSTextureView.addChild(parabolicMotionText);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

    }

    private void createParabolicMotionBitmap() {

        final ParabolicMotionBitmap parabolicMotionBitmap = new ParabolicMotionBitmap(mBitmap);
        parabolicMotionBitmap
                .transform(0, mFPSTextureView.getHeight())
                .dpSize(this)
                .reboundBottom(false)
                .accelerationX((float) (15 + Math.random() * 7))
                .initialVelocityY((float) (-65 + Math.random() * 15))
                .bottomHitCallback(new AnimCallBack() {
                    @Override
                    public void call() {
                        mFPSTextureView.removeChild(parabolicMotionBitmap);
                    }
                });

        mFPSTextureView.addChild(parabolicMotionBitmap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFPSTextureView.tickStart();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    createParabolicMotionBitmap();
                }
            }
        }, 0, 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFPSTextureView.tickStop();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
