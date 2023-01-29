package com.reemzet.adminedushorts.Model;

public class TranscationModel {
    String trantitle,trantype,tranamount,tranid,trandate,trantime,tranuserid;


    public TranscationModel() {
    }

    public TranscationModel(String trantitle, String trantype, String tranamount, String tranid, String trandate, String trantime, String tranuserid) {
        this.trantitle = trantitle;
        this.trantype = trantype;
        this.tranamount = tranamount;
        this.tranid = tranid;
        this.trandate = trandate;
        this.trantime = trantime;
        this.tranuserid = tranuserid;
    }

    public String getTrantitle() {
        return trantitle;
    }

    public void setTrantitle(String trantitle) {
        this.trantitle = trantitle;
    }

    public String getTrantype() {
        return trantype;
    }

    public void setTrantype(String trantype) {
        this.trantype = trantype;
    }

    public String getTranamount() {
        return tranamount;
    }

    public void setTranamount(String tranamount) {
        this.tranamount = tranamount;
    }

    public String getTranid() {
        return tranid;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
    }

    public String getTrandate() {
        return trandate;
    }

    public void setTrandate(String trandate) {
        this.trandate = trandate;
    }

    public String getTrantime() {
        return trantime;
    }

    public void setTrantime(String trantime) {
        this.trantime = trantime;
    }

    public String getTranuserid() {
        return tranuserid;
    }

    public void setTranuserid(String tranuserid) {
        this.tranuserid = tranuserid;
    }
}
