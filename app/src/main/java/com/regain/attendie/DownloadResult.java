package com.regain.attendie;

public class DownloadResult {
//    payload = {"stynumber": str(sem), "publish": "Y"}
    String stynumber,publish;

    public DownloadResult(String stynumber) {
        this.stynumber = stynumber;
        this.publish = "Y";
    }

    public DownloadResult() {
    }

    public String getStynumber() {
        return stynumber;
    }

    public void setStynumber(String stynumber) {
        this.stynumber = stynumber;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }
}
