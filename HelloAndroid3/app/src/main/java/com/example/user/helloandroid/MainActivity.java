package com.example.user.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tapHere = (Button)findViewById(R.id.tapHere);
        tapHere.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v){
                TextView textView = (TextView)findViewById(R.id.textView3);
                textView.setText("ボタンがタップされました！");
            }
        });
    }



    //AndroidStudioの機能を使ったとき用のメソッド
//    public void onClickButton (View view){
//        TextView textView = (TextView)findViewById(R.id.textView3);
//        textView.setText("ボタンがタップされました！");
//    }
}
