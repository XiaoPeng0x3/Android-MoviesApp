package com.example.moveshare.bean;

import java.util.List;

//电影详情
public class SimpleSubjectBean {

    private RatingEntity rating;//评分
    private int collect_count;//收藏数量
    private String title;//中文名
    private String original_title;//原名
    private String subtype;//条目分类, movie或者tv
    private String year;//年代
    private ImagesEntity images;//电影海报图，分别提供288px x 465px(大)，96px x 155px(中) 64px x 103px(小)尺寸
    private String alt;//条目页URL
    private String id;//条目id
    private List<String> genres;//影片类型，最多提供3个
    private List<CelebrityEntity> casts;//主演，最多可获得4个，数据结构为影人的简化描述
    private List<CelebrityEntity> directors;//导演，数据结构为影人的简化描述

    public void setRating(RatingEntity rating) {
        this.rating = rating;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setImages(ImagesEntity images) {
        this.images = images;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setCasts(List<CelebrityEntity> casts) {
        this.casts = casts;
    }

    public void setDirectors(List<CelebrityEntity> directors) {
        this.directors = directors;
    }

    public RatingEntity getRating() {
        return rating;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getYear() {
        return year;
    }

    public ImagesEntity getImages() {
        return images;
    }

    public String getAlt() {
        return alt;
    }

    public String getId() {
        return id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<CelebrityEntity> getCasts() {
        return casts;
    }

    public List<CelebrityEntity> getDirectors() {
        return directors;
    }
}
