package Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class NoSubmitScanResult extends DataSupport{

    private int id;
    private String time;
    private String remark;
    private String scanResult;
    private String date;
    private String username;
    private String acunt;
    private String finaldate;
    private int pictures;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAcunt() {
        return acunt;
    }

    public void setAcunt(String acunt) {
        this.acunt = acunt;
    }

    public String getFinaldate() {
        return finaldate;
    }

    public void setFinaldate(String finaldate) {
        this.finaldate = finaldate;
    }

    public int getPictures() {
        return pictures;
    }

    public void setPictures(int pictures) {
        this.pictures = pictures;
    }
}
