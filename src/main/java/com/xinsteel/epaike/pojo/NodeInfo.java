package com.xinsteel.epaike.pojo;

public class NodeInfo {
    private String orderid;

    private String nodename;

    private Integer nodecode;

    private String filename;

    private String fileurl;

    public NodeInfo(String orderid, String nodename, Integer nodecode, String filename, String fileurl) {
        this.orderid = orderid;
        this.nodename = nodename;
        this.nodecode = nodecode;
        this.filename = filename;
        this.fileurl = fileurl;
    }

    public NodeInfo() {
        super();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getNodename() {
        return nodename;
    }

    public void setNodename(String nodename) {
        this.nodename = nodename == null ? null : nodename.trim();
    }

    public Integer getNodecode() {
        return nodecode;
    }

    public void setNodecode(Integer nodecode) {
        this.nodecode = nodecode;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl == null ? null : fileurl.trim();
    }
}