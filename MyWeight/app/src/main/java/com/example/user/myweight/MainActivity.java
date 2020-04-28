package com.example.user.myweight;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.math.BigDecimal;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private BigDecimal mWeight = new BigDecimal("50.0"); //体重変数
    private Calendar mCal = Calendar.getInstance();//日付変数

    public static class MainDialogFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setTitle("保存処理");
            builder.setMessage("保存します");
            builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){
                    //保存処理
                }
            });
            return builder.create();
        }
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    ((MainActivity)getActivity())
                            .setCalendar(year, monthOfYear, dayOfMonth);

                }
            }, year, month, day);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showData();
        findViewById(R.id.setting).
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,
                                            SettingActivity.class));
                }
            });
    }

    /*
    アラートダイアログを表示
     */
    public void onSaveButtonTapped(View view){
        DialogFragment newFragment = new MainDialogFragment();
        newFragment.show(getFragmentManager(),"MainFragmentDialog");
    }

    public void onDateButtonTapped(View view){
        DialogFragment picker = new DatePickerFragment();
        picker.show(getFragmentManager(),"datePicker");
    }
    //設定した日付を取得してdateボタンに表示するための処理
    public void setCalendar(int year, int month, int day){
        mCal.set(year, month, day);
        showData();
    }
    //体重と日付を表示する処理
    private void showData(){
        Button weight = (Button) findViewById(R.id.weight);
        weight.setText(String.valueOf(mWeight));

        String today = mCal.get(Calendar.YEAR)
                + "/"
                + (mCal.get(Calendar.MONTH)) + 1
                + "/"
                + mCal.get(Calendar.DATE);
        Button date = (Button) findViewById(R.id.date);
        date.setText(today);
    }
    //体重の増減を管理する処理
    public void onChangeWeightButtonTapped(View view){
        switch (view.getId()){
            case R.id.plus_weight:
                mWeight = mWeight.add(new BigDecimal("0.1"));
                break;
            case R.id.minus_weight:
                mWeight = mWeight.add(new BigDecimal("-0.1"));
                break;
        }
        showData();
    }

    //日付の増減を管理する処理
    public void onChangeDateButtonTapped(View view){
        switch (view.getId()){
            case R.id.plus_date:
                mCal.add(Calendar.DATE, 1);
                break;
            case R.id.minus_date:
                mCal.add(Calendar.DATE, -1);
                break;
        }
        showData();
    }
}
