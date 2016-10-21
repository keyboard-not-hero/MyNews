package com.example.apiinvokedemo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by thompson on 16-10-18.
 */
public class SecondFragment extends Fragment {
    private ListView mListView;
    private String[] array = {"白羊座:3月21日～4月20日",
            "金牛座:4月21日～5月20日",
            "双子座:5月21日～6月21日",
            "巨蟹座:6月22日～7月22日",
            "狮子座:7月23日～8月22日",
            "处女座:8月23日～9月22日",
            "天秤座:9月23日～10月23日",
            "天蝎座:10月24日～11月22日",
            "射手座:11月23日～12月21日",
            "摩羯座:12月22日～1月19日",
            "水瓶座:1月20日～2月18日",
            "双鱼座:2月19日～3月20日"};
    private Activity mActivity;

    public static final String EXTRA_WHAT = "com.example.apiinvokedemo";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        mListView = (ListView) view.findViewById(R.id.list_item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, SecondActivity.class);
                intent.putExtra(EXTRA_WHAT, position);
                startActivity(intent);
            }
        });
        return view;
    }
}
