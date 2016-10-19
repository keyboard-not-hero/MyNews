package com.example.apiinvokedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by thompson on 16-10-18.
 */
public class ContentActivity extends Activity {
    private TextView mTitle;
    private TextView mContent;
    private TextView mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_layout);

        mTitle = (TextView) findViewById(R.id.title);
        mContent = (TextView) findViewById(R.id.content);
        mTime = (TextView) findViewById(R.id.time);

        Intent intent=getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String time = intent.getStringExtra("time");
        mTitle.setText(title);
        mContent.setText(content);
        mTime.setText(time);
    }
}
