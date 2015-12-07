package com.example.youxian.doublertest;

/**
 * Created by youxian on 12/7/15.
 */

// This is used to map the JSON keys to the object by GSON
public class Question {
    private String title;
    private String link;

    @Override
    public String toString() {
        return title;
    }
}
