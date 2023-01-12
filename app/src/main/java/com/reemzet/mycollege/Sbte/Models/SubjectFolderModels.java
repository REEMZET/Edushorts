package com.reemzet.mycollege.Sbte.Models;

public class SubjectFolderModels {
    String foldername,nocontent;

    public SubjectFolderModels(){

    }

    public SubjectFolderModels(String foldername, String nocontent) {
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
