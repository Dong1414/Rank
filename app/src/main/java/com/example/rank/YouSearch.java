package com.example.rank;

import android.graphics.drawable.Drawable;

public class YouSearch {
    private String iconDrawable ;
    private String titleStr ;
    private String Total ;
    private String Url;


    public void setIcon(String icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setTotal(String total) {
        Total = total ;
    }
    public void setUrl(String url) {
        Url = url ;
    }

    public String getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getTotal() {
        return this.Total ;
    }
    public String getUrl() {
        return this.Url ;
    }
}
