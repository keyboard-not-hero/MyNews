package com.example.apiinvokedemo;

/**
 * Created by thompson on 16-10-18.
 */
public class NewsPager {
    private String title;
    private String content;
    private String time;

    public NewsPager(String content, String title, String time) {
        this.content = content;
        this.time=time;
        this.title = title;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }

}
