package com.reemzet.mycollege.Sbte.Models;

public class LearningposterModel {
    String bgcolor,textcolor,imgurl,postermsg;

    public LearningposterModel() {
    }

    public LearningposterModel(String bgcolor, String textcolor, String imgurl, String postermsg) {
        this.bgcolor = bgcolor;
        this.textcolor = textcolor;
        this.imgurl = imgurl;
        this.postermsg = postermsg;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(String textcolor) {
        this.textcolor = textcolor;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPostermsg() {
        return postermsg;
    }

    public void setPostermsg(String postermsg) {
        this.postermsg = postermsg;
    }
}
