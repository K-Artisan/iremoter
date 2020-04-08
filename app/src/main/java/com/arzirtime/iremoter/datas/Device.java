package com.arzirtime.iremoter.datas;

import java.io.Serializable;

public class Device implements Serializable {

    private String num;
    private String name;
    private String image;
    private int imageResId;

    private String ip;
    private int port;
    private String macAddress;
    private String deviceTypeCode;

    //region GET/DET

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }

    //endregion

    public Device(String num, String ip, int port ,String name, int imageResId){
        this.num = num;
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.imageResId = imageResId;
    }
}
