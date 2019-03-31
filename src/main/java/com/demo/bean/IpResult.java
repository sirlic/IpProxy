package com.demo.bean;

public class IpResult {
    private String ip;
    private int port;
    private int type;

    private int score;

    public IpResult(String ip, int port, int type,int score) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.score = score;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
