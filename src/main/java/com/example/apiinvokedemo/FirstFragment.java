package com.example.apiinvokedemo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thompson on 16-10-18.
 */
public class FirstFragment extends Fragment {
    private static final int INIT_STATUS = 0;

    private ListView mListView;
    private List<NewsPager> newsList;
    private List<String> res;
    private static String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news?channelId=";
    private static String httpArg = "&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0"
            + "&title=%E4%B8%8A%E5%B8%82"
            + "&page=1"
            + "&needContent=1"
            + "&needHtml=0";
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase readableDatabase;
    private Cursor cursor;
    private Context mContext;
    private LinearLayout mLinearLayout;
    private LinearLayout mListContent;
    private String[] selection;
    public int mPosition;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            newsList = (List<NewsPager>) msg.obj;
            mListContent.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
            initListView();
        }
    };

    private void initListView(){
        for(int i=0;i<newsList.size();i++){
            res.add(newsList.get(i).getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,res);


        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("title",newsList.get(position).getTitle());
                intent.putExtra("content", newsList.get(position).getContent());
                intent.putExtra("time", newsList.get(position).getTime());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new MySQLiteOpenHelper(getActivity(),"News.db",null,2);
        newsList = new ArrayList<NewsPager>();
        res = new ArrayList<String>();

        selection = getResources().getStringArray(R.array.news_address);
        readableDatabase = helper.getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.loading01);
        mListView = (ListView) view.findViewById(R.id.newstitle01);
        mListContent = (LinearLayout) view.findViewById(R.id.list_content01);
        cursor = readableDatabase.rawQuery("select * from launone where type=?",new String[]{String.valueOf(mPosition)});

        if(cursor.moveToFirst()) {
            mListContent.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
            do{
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                NewsPager np = new NewsPager(content, title, time);
                newsList.add(np);
            }while(cursor.moveToNext());
            initListView();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String jsonData = getJson(httpUrl + selection[mPosition] + httpArg);
                    List<NewsPager> pagerList = new ArrayList<NewsPager>();
                    try {
                        JSONObject jsonObject_level_one=new JSONObject(jsonData);
                        JSONObject jsonObject_level_two = jsonObject_level_one.getJSONObject("showapi_res_body");
                        JSONObject jsonObject_level_three = jsonObject_level_two.getJSONObject("pagebean");
                        JSONArray jsonArray = jsonObject_level_three.getJSONArray("contentlist");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject contentObject=jsonArray.getJSONObject(i);
                            String title = contentObject.getString("title");
                            String content = contentObject.getString("content");
                            String time = contentObject.getString("pubDate");
                            readableDatabase.execSQL("insert into launone(title,time,content,type) values(?,?,?,?)", new String[]{title, time, content,String.valueOf(mPosition)});
                            NewsPager np = new NewsPager(content, title, time);
                            pagerList.add(np);

                        }
                        Message msg = Message.obtain();
                        msg.obj = pagerList;
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return view;
    }
    //下载json数据
    public String getJson(String str){
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(str);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("apikey", "b7ab2b71060688d2813b170d6f866e8b");
            InputStream inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        String address=sb.toString();
        return address;
    }
}
