package com.yzi.doutu.bean;

import java.io.Serializable;

/**
 * Created by yzh-t105 on 2017/4/27.
 */

public class Theme implements Serializable{
    private String id;
    private String userId; //用户id
    private String folderId;//文件夹id
    private String folderName;//文件夹名称
    private String thumbs;//略缩图数组 ["http://wxq.pic.bugua.com/6c27a0cd2cef0fc200c50a3bd1a9d44a.gif!sw300st.jpeg","http://wxq.pic.bugua.com/309e6e3823f4e693c310e18719f1cb6d.gif!sw300st.jpeg","http://wxq.pic.bugua.com/fa4ab1569a421f4738dd2581dc5e2558.gif!sw300st.jpeg","http://wxq.pic.bugua.com/79451d5ab064218b5c8faedf51946e22.gif!sw300st.jpeg"]

    public String getId() {
        return userId+folderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getThumbs() {
        return thumbs;
    }

    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }
}
