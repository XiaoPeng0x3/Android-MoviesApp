package com.example.moveshare.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.moveshare.bean.Movie;
import com.example.moveshare.bean.SimpleSubjectBean;
import com.example.moveshare.bean.SubjectBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUtil {


    public static String getAssetsContent(Context context,String fileName) {
        String result = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            int length = inputStream.available();
            byte [] buffer = new byte[length];
            inputStream.read(buffer);
            result = new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }



    public static Movie loadAllMovies(Context context, String fileName, int start, int count){

        Movie movie = new Movie();
        try {
            String strMovie = getAssetsContent(context,fileName);
            JSONObject movies_json = new JSONObject(strMovie);
            String moviesString = movies_json.getString("subjects");
            Gson gson = new Gson();

            List<SimpleSubjectBean> movieAllList = gson.fromJson(moviesString,  new TypeToken<List<SimpleSubjectBean>>() {}.getType());
            List<SimpleSubjectBean> movieList =  new ArrayList<SimpleSubjectBean>();//电影列表

            int j = 0;
            for (int i=start;i<movieAllList.size();i++){
                if (j<count){
                    movieList.add(movieAllList.get(i));
                    j++;
                }else {
                    break;
                }
            }

            movie.setStart(start);
            movie.setCount(count);
            movie.setTotal(movieAllList.size());
            movie.setSubjects(movieList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movie;

    }


    public static SubjectBean loadMovieById(Context context, String id){

        SubjectBean movie = null;
        try {
            String strMovie = getAssetsContent(context,id+".json");
            Gson gson = new Gson();
            movie = gson.fromJson(strMovie,SubjectBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movie;

    }


    public static String listToString(List<String> mList) {
        String convertedListStr = "";
        if (null != mList && mList.size() > 0) {
            String[] mListArray = mList.toArray(new String[mList.size()]);
            for (int i = 0; i < mListArray.length; i++) {
                if (i < mListArray.length - 1) {
                    convertedListStr += mListArray[i] + ",";
                } else {
                    convertedListStr += mListArray[i];
                }
            }
            return convertedListStr;
        } else return "";
    }


    public static List<String> getCollectMovies(Context context){
        SharedPreferences pref = context.getSharedPreferences("mdata",context.MODE_PRIVATE);
        String strmIds = pref.getString("mIds","");

        List<String> arrList = Arrays.asList(strmIds.split(",")); //[a, b, c]

        List mIdlist = new ArrayList();
        //去空字符
        for (int i = 0;i<arrList.size();i++){
            String s = arrList.get(i);
            if (!s.equals("")){
                mIdlist.add(s);
            }
        }

        return mIdlist;

    }


    public static boolean collectMovie(Context context,String mId) {
        //获取已收藏的电影编号
        List<String> mIdlist = getCollectMovies(context);
        mIdlist.add(mId);
        //去重
        Set set = new HashSet();
        List<String> newmIdlist = new  ArrayList<String>();
        set.addAll(mIdlist);
        newmIdlist.addAll(set);
        SharedPreferences.Editor editor = context.getSharedPreferences("mdata",context.MODE_PRIVATE).edit();
        String strmIds = listToString(newmIdlist);//1,2,3
        editor.putString("mIds",strmIds);
        editor.commit();
        return true;
    }

    public static int delCollectMovie(Context context,String mId) {

        int index = -1;
        //获取已收藏的电影编号
        List<String> mIdlist = getCollectMovies(context);

        for (int i=0;i<mIdlist.size();i++){
            String mId_tmp = mIdlist.get(i);
            if (mId.equals(mId_tmp)){
                mIdlist.remove(i);
                index = i;
                break;
            }
        }

        SharedPreferences.Editor editor = context.getSharedPreferences("mdata",context.MODE_PRIVATE).edit();

        String strmIds = listToString(mIdlist);
        editor.putString("mIds",strmIds);
        editor.commit();

        return index;
    }

    //获取收藏的电影列表
    public static List<SimpleSubjectBean> getAllCollectMovies(Context context, List<SimpleSubjectBean> movieAllList){

        List<SimpleSubjectBean> collectList =  new ArrayList<SimpleSubjectBean>();//收藏电影列表
        try {

            //获取已收藏的电影编号
            List<String> mIdlist = getCollectMovies(context);

            for (int i=0;i<movieAllList.size();i++){

                SimpleSubjectBean subjectBean = movieAllList.get(i);

                for (int j = 0;j<mIdlist.size();j++){
                    String mId = mIdlist.get(j);
                    if (mId.equals(subjectBean.getId())){
                        collectList.add(subjectBean);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return collectList;

    }


    public static void shareText(Context context,String text){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
//        share_intent.putExtra(Intent.EXTRA_SUBJECT, "share");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, text);//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        context.startActivity(share_intent);
    }

    public static void shareImage(Context context,Uri imgUri){
        //指定要分享的图片路径
//        Uri imgUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setType("image/*");//设置分享内容的类型
        shareIntent.setAction(Intent.ACTION_SEND);//设置分享行为
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);//添加分享内容

        shareIntent = Intent.createChooser(shareIntent, "share");
        context.startActivity(shareIntent);
    }

}
