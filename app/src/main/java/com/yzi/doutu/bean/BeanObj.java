package com.yzi.doutu.bean;

import java.util.List;

/**
 * Created by yzh-t105 on 2016/10/25.
 */

public class BeanObj {

    /**
     * code : 1
     * msg : 获取banner成功
     * data : [{"operatorType":null,"operatorId":null,"operator":null,"pageElementId":1469046347308,"pageId":1111,"pageName":null,"elementCode":"2","elementType":"2","ranking":2,"title":"王玉涛","textContent":"1","resUrl":"http://x.laiding.com.hk/ec-shtml/activityHtml/fashionskirt/img-wang/banner.jpg","hrefUrl":"/ec-shtml/activityHtml/fashionskirt/wangyitao.html","hrefUrlSourceType":null,"hrefUrlSourceValue":null,"versionType":0,"attr1":null,"attr2":null,"attr3":null,"attr4":null,"editorType":null,"editorId":null,"createTime":1475115666000,"createTimeStart":null,"createTimeEnd":null,"updateTime":1475115670000,"updateTimeStart":null,"updateTimeEnd":null,"pk":1469046347308},{"operatorType":null,"operatorId":null,"operator":null,"pageElementId":1469046347309,"pageId":1111,"pageName":null,"elementCode":"4","elementType":"1","ranking":4,"title":"欲罢不能的百褶裙","textContent":"1000","resUrl":"http://x.laiding.com.hk/ec-shtml/activityHtml/fashionskirt/images/bzq-banner.jpg","hrefUrl":"/ec-shtml/activityHtml/fashionskirt/fashionskirt.html","hrefUrlSourceType":null,"hrefUrlSourceValue":null,"versionType":0,"attr1":null,"attr2":null,"attr3":null,"attr4":null,"editorType":null,"editorId":null,"createTime":null,"createTimeStart":null,"createTimeEnd":null,"updateTime":null,"updateTimeStart":null,"updateTimeEnd":null,"pk":1469046347309},{"operatorType":null,"operatorId":null,"operator":null,"pageElementId":1469046347311,"pageId":1111,"pageName":null,"elementCode":"3","elementType":"3","ranking":3,"title":"内外兼修精致女人","textContent":"1","resUrl":"http://x.laiding.com.hk/ec-shtml/activityHtml/zhuanti/images-outin/outin.jpg","hrefUrl":"/ec-shtml/activityHtml/zhuanti/outin.html","hrefUrlSourceType":null,"hrefUrlSourceValue":null,"versionType":0,"attr1":null,"attr2":null,"attr3":null,"attr4":null,"editorType":null,"editorId":null,"createTime":1476511086000,"createTimeStart":null,"createTimeEnd":null,"updateTime":1476511091000,"updateTimeStart":null,"updateTimeEnd":null,"pk":1469046347311},{"operatorType":1,"operatorId":1455775852817,"operator":null,"pageElementId":1469046347312,"pageId":1111,"pageName":null,"elementCode":"1","elementType":"2","ranking":1,"title":"上新","textContent":"1","resUrl":"http://120.76.161.248/image-server/activity/banner/20160822/newqiu.jpg","hrefUrl":"/ec-shtml/activityHtml/banner/newqiu.html","hrefUrlSourceType":null,"hrefUrlSourceValue":null,"versionType":0,"attr1":null,"attr2":null,"attr3":null,"attr4":null,"editorType":null,"editorId":null,"createTime":1469046407000,"createTimeStart":null,"createTimeEnd":null,"updateTime":1469046407000,"updateTimeStart":null,"updateTimeEnd":null,"pk":1469046347312}]
     */

    private String code;
    private String msg;
    /**
     * operatorType : null
     * operatorId : null
     * operator : null
     * pageElementId : 1469046347308
     * pageId : 1111
     * pageName : null
     * elementCode : 2
     * elementType : 2
     * ranking : 2
     * title : 王玉涛
     * textContent : 1
     * resUrl : http://x.laiding.com.hk/ec-shtml/activityHtml/fashionskirt/img-wang/banner.jpg
     * hrefUrl : /ec-shtml/activityHtml/fashionskirt/wangyitao.html
     * hrefUrlSourceType : null
     * hrefUrlSourceValue : null
     * versionType : 0
     * attr1 : null
     * attr2 : null
     * attr3 : null
     * attr4 : null
     * editorType : null
     * editorId : null
     * createTime : 1475115666000
     * createTimeStart : null
     * createTimeEnd : null
     * updateTime : 1475115670000
     * updateTimeStart : null
     * updateTimeEnd : null
     * pk : 1469046347308
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String operatorType;
        private String operatorId;
        private String operator;
        private long pageElementId;
        private int pageId;
        private String pageName;
        private String elementCode;
        private String elementType;
        private int ranking;
        private String title;
        private String textContent;
        private String resUrl;
        private String hrefUrl;
        private String hrefUrlSourceType;
        private String hrefUrlSourceValue;
        private int versionType;
        private String attr1;
        private String attr2;
        private String attr3;
        private String attr4;
        private String editorType;
        private String editorId;
        private long createTime;
        private String createTimeStart;
        private String createTimeEnd;
        private long updateTime;
        private String updateTimeStart;
        private String updateTimeEnd;
        private long pk;

        public String getOperatorType() {
            return operatorType;
        }

        public void setOperatorType(String operatorType) {
            this.operatorType = operatorType;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public long getPageElementId() {
            return pageElementId;
        }

        public void setPageElementId(long pageElementId) {
            this.pageElementId = pageElementId;
        }

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public String getPageName() {
            return pageName;
        }

        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        public String getElementCode() {
            return elementCode;
        }

        public void setElementCode(String elementCode) {
            this.elementCode = elementCode;
        }

        public String getElementType() {
            return elementType;
        }

        public void setElementType(String elementType) {
            this.elementType = elementType;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public String getResUrl() {
            return resUrl;
        }

        public void setResUrl(String resUrl) {
            this.resUrl = resUrl;
        }

        public String getHrefUrl() {
            return hrefUrl;
        }

        public void setHrefUrl(String hrefUrl) {
            this.hrefUrl = hrefUrl;
        }

        public String getHrefUrlSourceType() {
            return hrefUrlSourceType;
        }

        public void setHrefUrlSourceType(String hrefUrlSourceType) {
            this.hrefUrlSourceType = hrefUrlSourceType;
        }

        public String getHrefUrlSourceValue() {
            return hrefUrlSourceValue;
        }

        public void setHrefUrlSourceValue(String hrefUrlSourceValue) {
            this.hrefUrlSourceValue = hrefUrlSourceValue;
        }

        public int getVersionType() {
            return versionType;
        }

        public void setVersionType(int versionType) {
            this.versionType = versionType;
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public String getAttr2() {
            return attr2;
        }

        public void setAttr2(String attr2) {
            this.attr2 = attr2;
        }

        public String getAttr3() {
            return attr3;
        }

        public void setAttr3(String attr3) {
            this.attr3 = attr3;
        }

        public String getAttr4() {
            return attr4;
        }

        public void setAttr4(String attr4) {
            this.attr4 = attr4;
        }

        public String getEditorType() {
            return editorType;
        }

        public void setEditorType(String editorType) {
            this.editorType = editorType;
        }

        public String getEditorId() {
            return editorId;
        }

        public void setEditorId(String editorId) {
            this.editorId = editorId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateTimeStart() {
            return createTimeStart;
        }

        public void setCreateTimeStart(String createTimeStart) {
            this.createTimeStart = createTimeStart;
        }

        public String getCreateTimeEnd() {
            return createTimeEnd;
        }

        public void setCreateTimeEnd(String createTimeEnd) {
            this.createTimeEnd = createTimeEnd;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateTimeStart() {
            return updateTimeStart;
        }

        public void setUpdateTimeStart(String updateTimeStart) {
            this.updateTimeStart = updateTimeStart;
        }

        public String getUpdateTimeEnd() {
            return updateTimeEnd;
        }

        public void setUpdateTimeEnd(String updateTimeEnd) {
            this.updateTimeEnd = updateTimeEnd;
        }

        public long getPk() {
            return pk;
        }

        public void setPk(long pk) {
            this.pk = pk;
        }
    }
}
