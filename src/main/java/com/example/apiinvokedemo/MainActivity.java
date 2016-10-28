package com.example.apiinvokedemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.apiinvokedemo.R.id.frame_manager;

public class MainActivity extends Activity{
    private RadioGroup mRadioGroup;
    private RadioButton fir_bt;
    private RadioButton sec_bt;
    private RadioButton thr_bt;
    private MySQLiteOpenHelper helper;
    private TextView mTitle;
    private Spinner mSeletor;
    private String[] name;


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
            Toast.makeText(this,"再次点击退出",Toast.LENGTH_SHORT).show();
            currentTime = System.currentTimeMillis();
        }else{
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        helper = new MySQLiteOpenHelper(this, "News.db", null, 2);
        initRadioGrp();
        initTitleBar();
    }

    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.title);
        mSeletor = (Spinner) findViewById(R.id.spinner_list);

        name = getResources().getStringArray(R.array.news_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSeletor.setAdapter(adapter);
        mSeletor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();
                FirstFragment fragment = new FirstFragment();
                FirstFragment.mPosition = position;
                manager.beginTransaction().replace(R.id.frame_manager,fragment).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRadioGrp(){
        mRadioGroup= (RadioGroup) findViewById(R.id.group);
        mRadioGroup.check(R.id.first);
        fir_bt = (RadioButton) findViewById(R.id.first);
        sec_bt = (RadioButton) findViewById(R.id.second);
        thr_bt = (RadioButton) findViewById(R.id.third);


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
                        mSeletor.setVisibility(VISIBLE);
                        mTitle.setText("科技最新");
                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        FirstFragment fragment_one=new FirstFragment();
                        transaction.replace(frame_manager,fragment_one);
                        transaction.commit();

                        cnt=1;
                    }
                    break;

                case R.id.second:
                    if(cnt != 2){
                        mSeletor.setVisibility(GONE);
                        mTitle.setText("星座运势");
                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        SecondFragment fragment_two=new SecondFragment();
                        transaction.replace(frame_manager,fragment_two);
                        transaction.commit();
                        cnt=2;
                    }
                    break;

                case R.id.third:
                    if(cnt != 3){
                        mSeletor.setVisibility(GONE);
                        mTitle.setText("开心一刻");
                        FragmentManager fragmentManager=getFragmentManager();
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        ThirdFragment fragment_third=new ThirdFragment();
                        transaction.replace(frame_manager,fragment_third);
                        transaction.commit();
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
//        player.release();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from launone");
        db.execSQL("delete from launtwo");
        db.execSQL("delete from launthree");

    }
}
