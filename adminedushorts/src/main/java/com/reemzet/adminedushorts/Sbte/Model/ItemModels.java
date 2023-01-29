package com.reemzet.adminedushorts.Sbte.Model;

import java.util.List;

public class ItemModels {
    String itemtitle, itemurl, itemdesc, itemlength, itemprice, itemid, itemtype, uploaddate, itemviewcount, itemthumbnail;
    List<String> itemtag;

    public ItemModels() {
    }

    public ItemModels(String itemtitle, String itemurl, String itemdesc, String itemlength, String itemprice, String itemid, String itemtype, String uploaddate, String itemviewcount, String itemthumbnail) {
        this.itemtitle = itemtitle;
        this.itemurl = itemurl;
        this.itemdesc = itemdesc;
        this.itemlength = itemlength;
        this.itemprice = itemprice;
        this.itemid = itemid;
        this.itemtype = itemtype;
        this.uploaddate = uploaddate;
        this.itemviewcount = itemviewcount;
        this.itemthumbnail = itemthumbnail;
    }

    public ItemModels(String itemtitle, String itemurl, String itemdesc, String itemlength, String itemprice, String itemid, String itemtype, String uploaddate, String itemviewcount, String itemthumbnail, List<String> itemtag) {
        this.itemtitle = itemtitle;
        this.itemurl = itemurl;
        this.itemdesc = itemdesc;
        this.itemlength = itemlength;
        this.itemprice = itemprice;
        this.itemid = itemid;
        this.itemtype = itemtype;
        this.uploaddate = uploaddate;
        this.itemviewcount = itemviewcount;
        this.itemthumbnail = itemthumbnail;
        this.itemtag = itemtag;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getItemurl() {
        return itemurl;
    }

    public void setItemurl(String itemurl) {
        this.itemurl = itemurl;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getItemlength() {
        return itemlength;
    }

    public void setItemlength(String itemlength) {
        this.itemlength = itemlength;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate;
    }

    public String getItemviewcount() {
        return itemviewcount;
    }

    public void setItemviewcount(String itemviewcount) {
        this.itemviewcount = itemviewcount;
    }

    public String getItemthumbnail() {
        return itemthumbnail;
    }

    public void setItemthumbnail(String itemthumbnail) {
        this.itemthumbnail = itemthumbnail;
    }

    public List<String> getItemtag() {
        return itemtag;
    }

    public void setItemtag(List<String> itemtag) {
        this.itemtag = itemtag;
    }
}