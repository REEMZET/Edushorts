package com.reemzet.adminedushorts.Sbte.Model;

public class QuesBankYearModel {
    String yearname,noofcontent;

    public QuesBankYearModel() {
    }

    public QuesBankYearModel(String yearname, String noofcontent) {
        this.yearname = yearname;
        this.noofcontent = noofcontent;
    }

    public String getYearname() {
        return yearname;
    }

    public void setYearname(String yearname) {
        this.yearname = yearname;
    }

    public String getNoofcontent() {
        return noofcontent;
    }

    public void setNoofcontent(String noofcontent) {
        this.noofcontent = noofcontent;
    }
}
