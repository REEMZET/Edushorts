package com.reemzet.mycollege.Model;

public class PurchaseModel {
    String productid,productname,producturl,producttype,purchasedate,productvalidity,productprice;

    public PurchaseModel() {
    }

    public PurchaseModel(String productid, String productname, String producturl, String producttype, String purchasedate, String productvalidity, String productprice) {
        this.productid = productid;
        this.productname = productname;
        this.producturl = producturl;
        this.producttype = producttype;
        this.purchasedate = purchasedate;
        this.productvalidity = productvalidity;
        this.productprice = productprice;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(String purchasedate) {
        this.purchasedate = purchasedate;
    }

    public String getProductvalidity() {
        return productvalidity;
    }

    public void setProductvalidity(String productvalidity) {
        this.productvalidity = productvalidity;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }
}
