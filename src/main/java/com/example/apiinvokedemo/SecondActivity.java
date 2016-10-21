package com.example.apiinvokedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by thompson on 16-10-21.
 */

public class SecondActivity extends Activity {
    private ScrollView contentLayout;
    private ImageView constImage;
    private TextView today;
    private TextView week;
    private LinearLayout loadLayout;
//    private TextView mTextView;
    private int[] res = new int[]{R.drawable.num_1,R.drawable.num_2,R.drawable.num_3,R.drawable.num_4,
            R.drawable.num_5,R.drawable.num_6,R.drawable.num_7,R.drawable.num_8,
            R.drawable.num_9,R.drawable.num_10,R.drawable.num_11,R.drawable.num_12};
    private String[] name={"%e7%99%bd%e7%be%8a%e5%ba%a7","%e9%87%91%e7%89%9b%e5%ba%a7","%e5%8f%8c%e5%ad%90%e5%ba%a7","%e5%b7%a8%e8%9f%b9%e5%ba%a7",
            "%e7%8b%ae%e5%ad%90%e5%ba%a7","%e5%a4%84%e5%a5%b3%e5%ba%a7","%e5%a4%a9%e7%a7%a4%e5%ba%a7","%e5%a4%a9%e8%9d%8e%e5%ba%a7",
            "%e5%b0%84%e6%89%8b%e5%ba%a7","%e6%91%a9%e7%be%af%e5%ba%a7","%e6%b0%b4%e7%93%b6%e5%ba%a7","%e5%8f%8c%e9%b1%bc%e5%ba%a7"};
    String httpUrl = "http://apis.baidu.com/bbtapi/constellation/constellation_query";
    String httpArg = "?consName=";
    String httpArg1 = "&type=";
    private int position;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String[] as = (String[]) msg.obj;
            constImage.setImageResource(res[position]);
            today.setText(as[0]);
            week.setText(as[1]);
            contentLayout.setVisibility(VISIBLE);
            loadLayout.setVisibility(GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_second);
        //初始化内容组件
        contentLayout = (ScrollView) findViewById(R.id.container_content);
        constImage = (ImageView) findViewById(R.id.const_image);
        today = (TextView) findViewById(R.id.today);
        week = (TextView) findViewById(R.id.week);
        //初始化缓冲组件
        loadLayout = (LinearLayout) findViewById(R.id.fresh_content);
//        mTextView = (TextView) findViewById(R.id.text);
//        String s=getJson("http://apis.baidu.com/bbtapi/constellation/constellation_query?consName=山羊座&type=today");
//        mTextView.setText(s);
        Intent intent =getIntent();
        position = intent.getIntExtra(SecondFragment.EXTRA_WHAT,0);
        new Thread(new Runnable(){
            @Override
            public void run() {
                String http1=httpUrl+httpArg+name[position]+httpArg1+"today";
                String http2=httpUrl+httpArg+name[position]+httpArg1+"week";
                try {
                    JSONObject jsonObject_one = new JSONObject(getJson(http1));
                    String datetime ="日期："+jsonObject_one.getString("datetime");
                    String name ="星座："+jsonObject_one.getString("name");
                    String QFriend="速配："+jsonObject_one.getString("QFriend");
                    String health="健康指数："+jsonObject_one.getString("health");
                    String love="爱情指数："+jsonObject_one.getString("love");
                    String money="财运指数："+jsonObject_one.getString("money");
                    String work="工作指数："+jsonObject_one.getString("work");
                    String color="幸运色："+jsonObject_one.getString("color");
                    String number="幸运号："+jsonObject_one.getString("number");
                    String summary="总结："+jsonObject_one.getString("summary");
                    JSONObject jsonObject_two = new JSONObject(getJson(http2));
                    String heal=jsonObject_two.getString("health");
                    String job=jsonObject_two.getString("job");
                    String lover=jsonObject_two.getString("love");
                    String mone=jsonObject_two.getString("money");
                    String worker=jsonObject_two.getString("work");
                    String[] sa = {datetime+"\n"+name+"\n"+QFriend+"\n"+health+"\n"+love+"\n"+money+"\n"+work+"\n"+color+"\n"+number+"\n"+summary+"\n",
                            heal+"\n"+job+"\n"+mone+"\n"+lover+"\n"+worker+"\n"};
                    Message msg = Message.obtain();
                    msg.obj = sa;
                    mHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getJson(String str){
        StringBuilder sb = null;
        try {
            URL url = new URL(str);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", "b7ab2b71060688d2813b170d6f866e8b");
            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
            sb = new StringBuilder();
            String line="";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
