package com.reemzet.mycollege.Sbte.Models;

public class PdfModel {
    String pdfname,pdfurl;

    public PdfModel() {
    }

    public PdfModel(String pdfname, String pdfurl) {
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
