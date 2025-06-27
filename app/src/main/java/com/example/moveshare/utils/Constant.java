package com.example.moveshare.utils;


import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.bean.SubjectBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 记录项目常量
 */
public class Constant {

    //服务器地址
    static String ip = "10.0.2.2";
    public static final String API = "http://" + ip + ":8084";
    //电影列表（主页）
    public static final String IN_THEATERS = "/MovieAPI/movie/in_theaters";
    //电影详情
    public static final String SUBJECT = "/MovieAPI/movie/subject/";
    //即将上映
    public static final String COMING_SOON = "/MovieAPI/movie/coming_soon";
    //欧美排行榜（Top25）
    public static final String US_BOX = "/MovieAPI/movie/us_box";
    //最新上映
    public static final String NEW_MOVIES = "/MovieAPI/movie/new_movies";
    public static final Type subType = new TypeToken<SubjectBean>() {
    }.getType();
    public static final Type simpleSubTypeList = new TypeToken<List<SimpleSubjectBean>>() {
    }.getType();

}
