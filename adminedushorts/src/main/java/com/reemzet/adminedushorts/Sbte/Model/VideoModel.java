package com.reemzet.adminedushorts.Sbte.Model;

public class VideoModel {
    String videoid,videotitle,videodesc,videourl,videoviews,videolength,videoimg,videoprice;

    public VideoModel() {
    }

    public VideoModel(String videoid, String videotitle, String videodesc, String videourl, String videoviews, String videolength, String videoimg, String videoprice) {
        this.videoid = videoid;
        this.videotitle = videotitle;
        this.videodesc = videodesc;
        this.videourl = videourl;
        this.videoviews = videoviews;
        this.videolength = videolength;
        this.videoimg = videoimg;
        this.videoprice = videoprice;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getVideotitle() {
        return videotitle;
    }

    public void setVideotitle(String videotitle) {
        this.videotitle = videotitle;
    }

    public String getVideodesc() {
        return videodesc;
    }

    public void setVideodesc(String videodesc) {
        this.videodesc = videodesc;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getVideoviews() {
        return videoviews;
    }

    public void setVideoviews(String videoviews) {
        this.videoviews = videoviews;
    }

    public String getVideolength() {
        return videolength;
    }

    public void setVideolength(String videolength) {
        this.videolength = videolength;
    }

    public String getVideoimg() {
        return videoimg;
    }

    public void setVideoimg(String videoimg) {
        this.videoimg = videoimg;
    }

    public String getVideoprice() {
        return videoprice;
    }

    public void setVideoprice(String videoprice) {
        this.videoprice = videoprice;
    }
}
