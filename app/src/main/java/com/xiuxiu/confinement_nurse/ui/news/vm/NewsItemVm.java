package com.xiuxiu.confinement_nurse.ui.news.vm;

import java.io.Serializable;

public class NewsItemVm implements Serializable {
    /**
     * 消息id
     */
    private String newsId;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 消息数
     */
    private int numberOfNews;

    /**
     * 用户类型
     * 个人/
     */
    private String userType;

    /**
     * 地区
     */
    private String area;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 消息主体
     */
    private String news;

    /**
     * 时间
     */
    private long time;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getNumberOfNews() {
        return numberOfNews;
    }

    public void setNumberOfNews(int numberOfNews) {
        this.numberOfNews = numberOfNews;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
