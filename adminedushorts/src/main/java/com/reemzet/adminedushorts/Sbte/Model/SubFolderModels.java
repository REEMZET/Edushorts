package com.reemzet.adminedushorts.Sbte.Model;

public class SubFolderModels {
    String foldername,nocontent;

    public SubFolderModels() {
    }

    public SubFolderModels(String foldername, String nocontent) {
        this.foldername = foldername;
        this.nocontent = nocontent;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getNocontent() {
        return nocontent;
    }

    public void setNocontent(String nocontent) {
        this.nocontent = nocontent;
    }
}
