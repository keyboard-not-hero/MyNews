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
import android.util.Log;
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
    private static final String TAG = "FirstFragment";

    private ListView mListView;
    private List<NewsPager> newsList;
    private List<String> res;

    private static String httpUrl = "http://apis.baidu.com/showapi_open_bus/channel_news/search_news";
    private static String httpArg = "channelId=5572a10ab3cdc86cf39001ee"
            + "&channelName=%E5%9B%BD%E5%86%85%E6%9C%80%E6%96%B0"
            + "&title=%E4%B8%8A%E5%B8%82"
            + "&page=1"
            + "&needContent=1"
            + "&needHtml=0";
    private String getUrl=httpUrl+"?"+httpArg;
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase readableDatabase;
    private Cursor cursor;
    private Context mContext;
    private LinearLayout mLinearLayout;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            newsList = (List<NewsPager>) msg.obj;
            mListView.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
            initListView();

        }
    };

    private void initListView(){
        for(int i=0;i<newsList.size();i++){
            res.add(newsList.get(i).getTitle());
        }
        Log.d(TAG, "res="+res);
        Log.d(TAG, "getActivity="+getActivity());
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
        helper = new MySQLiteOpenHelper(getActivity(),"News.db",null,1);
        newsList = new ArrayList<NewsPager>();
        res = new ArrayList<String>();

        readableDatabase = helper.getWritableDatabase();
        cursor = readableDatabase.rawQuery("select * from launone",null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.loading);
        mListView = (ListView) view.findViewById(R.id.newstitle);

        if(cursor.moveToFirst()) {
            mListView.setVisibility(View.VISIBLE);
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
                    BufferedReader reader = null;
                    StringBuilder sb = null;
                    try {
                        URL url = new URL(getUrl);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setRequestProperty("apikey", "b7ab2b71060688d2813b170d6f866e8b");
                        InputStream inputStream = httpURLConnection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        sb = new StringBuilder();
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
                    List<NewsPager> pagerList = new ArrayList<NewsPager>();
                    try {
                        JSONObject jsonObject_level_one=new JSONObject(sb.toString());
                        JSONObject jsonObject_level_two = jsonObject_level_one.getJSONObject("showapi_res_body");
                        JSONObject jsonObject_level_three = jsonObject_level_two.getJSONObject("pagebean");
                        JSONArray jsonArray = jsonObject_level_three.getJSONArray("contentlist");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject contentObject=jsonArray.getJSONObject(i);
                            String title = contentObject.getString("title");
                            String content = contentObject.getString("content");
                            String time = contentObject.getString("pubDate");
                            readableDatabase.execSQL("insert into launone(title,time,content) values(?,?,?)", new String[]{title, time, content});
                            NewsPager np = new NewsPager(content, title, time);
                            pagerList.add(np);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Message msg = Message.obtain();
                    msg.obj = pagerList;
                    mHandler.sendMessage(msg);
                }
            }).start();
        }
        return view;
    }
}
