package com.yzi.doutu.bean;

import java.io.Serializable;

/**
 * Created by yzh-t105 on 2016/9/27.
 */

public  class DataBean implements Serializable {
    private String formWhere;
    private int bisDelete;
    private int bisRecommend;
    private int clickNum;
    private int clickWeight;
    private long createTime;
    private String gifPath;//如果是制作图片，这个字段将会替换成本地图片SD的路径
    private int id;
    private int mediaType;
    private String name;
    private String picPath;
    private long recommendTime;
    private long ts;
    private int typeId;
    private int weight;

    //sqlite 所需字段
    private String oldUrl; //原图片地址
    private String fileName;//文件名

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOldUrl() {
        return oldUrl;
    }

    public void setOldUrl(String oldUrl) {
        this.oldUrl = oldUrl;
    }

    public String getFormWhere() {
        return formWhere;
    }

    public void setFormWhere(String formWhere) {
        this.formWhere = formWhere;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getBisDelete() {
        return bisDelete;
    }

    public void setBisDelete(int bisDelete) {
        this.bisDelete = bisDelete;
    }

    public int getBisRecommend() {
        return bisRecommend;
    }

    public void setBisRecommend(int bisRecommend) {
        this.bisRecommend = bisRecommend;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public int getClickWeight() {
        return clickWeight;
    }

    public void setClickWeight(int clickWeight) {
        this.clickWeight = clickWeight;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getGifPath() {
        return gifPath;
    }

    public void setGifPath(String gifPath) {
        this.gifPath = gifPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public long getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(long recommendTime) {
        this.recommendTime = recommendTime;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
