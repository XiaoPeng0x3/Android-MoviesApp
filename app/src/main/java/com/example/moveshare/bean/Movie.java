/**
  * Copyright 2019 bejson.com 
  */
package com.example.moveshare.bean;
import java.util.List;

/**
 * Auto-generated: 2019-12-18 10:13:57
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Movie {

    private int count;
    private int start;
    private int total;
    private List<SimpleSubjectBean> subjects;

    public void setCount(int count) {
         this.count = count;
     }
     public int getCount() {
         return count;
     }

    public void setStart(int start) {
         this.start = start;
     }
    public int getStart() {
         return start;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }


    public List<SimpleSubjectBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SimpleSubjectBean> subjects) {
        this.subjects = subjects;
    }
}