package com.reemzet.mycollege.Sbte.Models;

public class QuestionModel {
    String pdfname,pdfurl;

    public QuestionModel() {
    }

    public QuestionModel(String pdfname, String pdfurl) {
        this.pdfname = pdfname;
        this.pdfurl = pdfurl;
    }

    public String getPdfname() {
        return pdfname;
    }

    public void setPdfname(String pdfname) {
        this.pdfname = pdfname;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
}
