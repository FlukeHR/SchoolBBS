package com.bbs.pojo;

import java.sql.Date;

public class Post {
    private int id;
    private String title;
    private String author;
    private String type;
    private String content;
    private java.sql.Date date;
    private String mediaFiles; // 存储图片和视频文件路径，多个文件用逗号分隔

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostType() {
        return type;
    }

    public void setPostType(String postType) {
        type = postType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(String mediaFiles) {
        this.mediaFiles = mediaFiles;
    }
}
