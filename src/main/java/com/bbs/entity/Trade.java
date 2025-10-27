package com.bbs.entity;

import java.sql.Timestamp;

public class Trade {
    private int id;
    private String title;
    private Double price;
    private String description;
    private String images; // 多图，逗号分隔，相对路径 uploads/trade/xxx.ext
    private String author;
    private Timestamp date;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    // 兼容旧字段：返回第一张图片
    public String getImg() {
        if (images == null || images.trim().isEmpty()) return null;
        String[] arr = images.split(",");
        return arr.length > 0 ? arr[0] : null;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
}
