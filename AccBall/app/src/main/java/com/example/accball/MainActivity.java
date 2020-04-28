package com.example.accball;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
            implements SensorEventListener, SurfaceHolder.Callback {
    SensorManager mSensorManager;
    Sensor mAccSensor;
    SurfaceHolder mHolder;
    int mSurfaceWidth;   //サーフェスビューの幅
    int mSurfaceHeight;   //サーフェスビューの高さ

    static final float RADIUS = 50.0f;  //ボールを描画するときの半径を表す定数
    static final float COEF = 1000.0f;  //ボール移動量を調整するための係数

    float mBallX;   //ボールの現在のx座標
    float mBallY;   //ボールの現在のy座標
    float mVX;      //ボールのx軸方向への速度
    float mVY;      //ボールのy軸方向への速度

    long mFrom;     //前回、センサーから加速度を取得した時間
    long mTo;       //今回、センサーから加速度を取得した時間

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //画面を縦方向にロック
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);//管理マネージャークラスの生成
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//加速度センサーインスタンス生成
        SurfaceView surfaceView =
                (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);//サーフェスビューが変更、破棄された際のイベントリスナーの登録
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.d("MainActivity",
                    "x=" + String.valueOf(event.values[0]) +      //x軸の加速度
                            "y=" + String.valueOf(event.values[1]) +    //y軸の加速度
                            "z=" + String.valueOf(event.values[2]));    //z軸の加速度
            float x = -event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            mTo = System.currentTimeMillis();
            float t = (float) (mTo - mFrom);

            t = t / 1000.0f;
            float dx = mVX * t + x * t * t / 2.0f;//x,y軸の移動距離を算出
            float dy = mVY * t + y * t * t / 2.0f;
            mBallX = mBallX + dx * COEF;//移動後のx,y座標を取得
            mBallY = mBallY + dy * COEF;
            mVX = mVX + x * t;//次回に加速度センサーから値を受け取った際に速度を保存
            mVY = mVY + y * t;

            if(mBallX - RADIUS < 0 && mVX < 0){
                mVX = -mVX / 1.5f;
                mBallX = RADIUS;
            }else if(mBallX + RADIUS > mSurfaceWidth && mVX > 0){
                mVX = -mVX / 1.5f;
                mBallX = mSurfaceWidth - RADIUS;
            }
            if(mBallY - RADIUS < 0 && mVY < 0){
                mVY = -mVY / 1.5f;
                mBallY = RADIUS;
            }else if(mBallY + RADIUS > mSurfaceHeight && mVY > 0){
                mVY = -mVY / 1.5f;
                mBallY = mSurfaceHeight - RADIUS;
            }

            mFrom = System.currentTimeMillis();//ボール移動後の現在時間の取得
            drawCanvas();
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccSensor,
                SensorManager.SENSOR_DELAY_GAME);//コールバックを受けるリスナー設定
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mFrom = System.currentTimeMillis();
        mSensorManager.registerListener(this,mAccSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        mBallX = width / 2;//画面の初期位置を画面中央に
        mBallY = height / 2;
        mVX = 0;    //初期化
        mVY = 0;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSensorManager.unregisterListener(this);
    }
    private void drawCanvas() {
        Canvas c = mHolder.lockCanvas();
        c.drawColor(Color.YELLOW);
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        c.drawCircle(mBallX,mBallY,RADIUS,paint);
        mHolder.unlockCanvasAndPost(c); //画面のアンロック
    }
}
