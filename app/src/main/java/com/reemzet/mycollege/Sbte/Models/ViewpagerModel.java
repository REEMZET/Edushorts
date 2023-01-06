package com.reemzet.mycollege.Sbte.Models;

public class ViewpagerModel {
    String posterurl,posterevent;

    ViewpagerModel(){

    }
    public ViewpagerModel(String posterurl, String posterevent) {
        this.posterurl = posterurl;
        this.posterevent = posterevent;
    }

    public String getPosterurl() {
        return posterurl;
    }

    public void setPosterurl(String posterurl) {
        this.posterurl = posterurl;
    }

    public String getPosterevent() {
        return posterevent;
    }

    public void setPosterevent(String posterevent) {
        this.posterevent = posterevent;
    }
}

