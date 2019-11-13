package com.codesaid.lib_share.bean;

/**
 * Created By codesaid
 * On :2019-11-13
 * Package Name: com.codesaid.lib_share.bean
 * desc: 构造者模式 封装 Share 实体类
 */
public class Share {

    private int mShareType; //指定分享类型
    private String mShareTitle; //指定分享内容标题
    private String mShareText; //指定分享内容文本
    private String mSharePhoto; //指定分享本地图片
    private String mShareTitleUrl;
    private String mShareSiteUrl;
    private String mShareSite;
    private String mUrl;
    private String mResourceUrl;

    private Share(Builder builder) {
        this.mShareType = builder.mShareType;
        this.mShareTitle = builder.mShareTitle;
        this.mShareText = builder.mShareText;
        this.mSharePhoto = builder.mSharePhoto;
        this.mShareTitleUrl = builder.mShareTitleUrl;
        this.mShareSiteUrl = builder.mShareSiteUrl;
        this.mShareSite = builder.mShareSite;
        this.mUrl = builder.mUrl;
        this.mResourceUrl = builder.mResourceUrl;
    }

    public int getShareType() {
        return mShareType;
    }

    public String getShareTitle() {
        return mShareTitle;
    }

    public String getShareText() {
        return mShareText;
    }

    public String getSharePhoto() {
        return mSharePhoto;
    }

    public String getShareTitleUrl() {
        return mShareTitleUrl;
    }

    public String getShareSiteUrl() {
        return mShareSiteUrl;
    }

    public String getShareSite() {
        return mShareSite;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getResourceUrl() {
        return mResourceUrl;
    }

    public static class Builder {
        /**
         * share relative
         */
        private int mShareType; //指定分享类型
        private String mShareTitle; //指定分享内容标题
        private String mShareText; //指定分享内容文本
        private String mSharePhoto; //指定分享本地图片
        private String mShareTitleUrl;
        private String mShareSiteUrl;
        private String mShareSite;
        private String mUrl;
        private String mResourceUrl;

        public Builder() {
        }

        public Builder setShareType(int shareType) {
            this.mShareType = shareType;
            return this;
        }

        public Builder setShareTitle(String shareTitle) {
            this.mShareTitle = shareTitle;
            return this;
        }

        public Builder setShareText(String shareText) {
            this.mShareText = shareText;
            return this;
        }

        public Builder setSharePhoto(String sharePhoto) {
            this.mSharePhoto = sharePhoto;
            return this;
        }

        public Builder setShareTitleUrl(String shareTitleUrl) {
            this.mShareTitleUrl = shareTitleUrl;
            return this;
        }

        public Builder setShareSiteUrl(String shareSiteUrl) {
            this.mShareSiteUrl = shareSiteUrl;
            return this;
        }

        public Builder setShareSite(String shareSite) {
            this.mShareSite = shareSite;
            return this;
        }

        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;

        }

        public Builder setResourseUrl(String resourceUrl) {
            this.mResourceUrl = resourceUrl;
            return this;
        }

        public Share build() {
            return new Share(this);
        }
    }
}
