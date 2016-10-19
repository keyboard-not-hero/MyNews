package com.example.apiinvokedemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
    private ImageButton fir_bt;
    private ImageButton sec_bt;
    private ImageButton thr_bt;
    private MySQLiteOpenHelper helper;
    //标注目前选中的按钮
    private static int cnt=1;
    //上次点击back键的时间，本次点击后如果未关闭，实时更新（注意类型为long）
    private static long currentTime=0;

    //back键方法重写
    @Override
    public void onBackPressed() {
        //System.currentTimeMillis为当前时间距离1970-1-1的毫秒数
        if(System.currentTimeMillis()-currentTime>2000)
        {
            Toast.makeText(this,"再次点击back退出！",Toast.LENGTH_SHORT).show();
            currentTime = System.currentTimeMillis();
        }else{
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        helper = new MySQLiteOpenHelper(this, "News.db", null, 1);

        fir_bt = (ImageButton) findViewById(R.id.first);
        sec_bt = (ImageButton) findViewById(R.id.second);
        thr_bt = (ImageButton) findViewById(R.id.third);

        fir_bt.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

        MyOnClickListener listener = new MyOnClickListener();
        fir_bt.setOnClickListener(listener);
        sec_bt.setOnClickListener(listener);
        thr_bt.setOnClickListener(listener);
    }

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch(v.getId())
            {
                case R.id.first:
                    if(cnt != 1)
                    {
                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        FirstFragment fragment_one=new FirstFragment();
                        transaction.replace(R.id.frame_manager,fragment_one);
                        transaction.commit();
                        if(cnt == 2){
                            sec_bt.setImageResource(R.drawable.two);
                            sec_bt.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }else {
                            thr_bt.setImageResource(R.drawable.third);
                            thr_bt.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }

                        fir_bt.setImageResource(R.drawable.first_);
                        fir_bt.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                        cnt=1;
                    }
                    break;
                case R.id.second:

                    if(cnt != 2){
                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        SecondFragment fragment_two=new SecondFragment();
                        transaction.replace(R.id.frame_manager,fragment_two);
                        transaction.commit();
                        if(cnt == 1){
                            fir_bt.setImageResource(R.drawable.first);
                            fir_bt.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }else{
                            thr_bt.setImageResource(R.drawable.third);
                            thr_bt.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }

                        sec_bt.setImageResource(R.drawable.two_);
                        sec_bt.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                        cnt=2;
                    }
                    break;
                case R.id.third:

                    if(cnt != 3){
                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        ThirdFragment fragment_third=new ThirdFragment();
                        transaction.replace(R.id.frame_manager,fragment_third);
                        transaction.commit();
                        if(cnt == 1){
                            fir_bt.setImageResource(R.drawable.first);
                            fir_bt.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }else {
                            sec_bt.setImageResource(R.drawable.two);
                            sec_bt.setBackgroundColor(getResources().getColor(android.R.color.white));
                        }
                        thr_bt.setImageResource(R.drawable.third_);
                        thr_bt.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                        cnt=3;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from launone");
        db.execSQL("delete from launtwo");
        db.execSQL("delete from launthree");

    }
}
