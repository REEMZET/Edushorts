package com.reemzet.mycollege.Sbte.Models;

public class SyllabusModel {
    String subcode,subname,subimg,subfm,subpm,subpdf;

    public SyllabusModel() {
    }

    public SyllabusModel(String subcode, String subname, String subimg, String subfm, String subpm, String subpdf) {
        this.subcode = subcode;
        this.subname = subname;
        this.subimg = subimg;
        this.subfm = subfm;
        this.subpm = subpm;
        this.subpdf = subpdf;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubimg() {
        return subimg;
    }

    public void setSubimg(String subimg) {
        this.subimg = subimg;
    }

    public String getSubfm() {
        return subfm;
    }

    public void setSubfm(String subfm) {
        this.subfm = subfm;
    }

    public String getSubpm() {
        return subpm;
    }

    public void setSubpm(String subpm) {
        this.subpm = subpm;
    }

    public String getSubpdf() {
        return subpdf;
    }

    public void setSubpdf(String subpdf) {
        this.subpdf = subpdf;
    }
}
