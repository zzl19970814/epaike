package com.xinsteel.epaike.pojo;

import java.math.BigDecimal;

public class ProductInfo {
    private String apimaterno;

    private String apimatername;

    private Long productskuid;

    private String orderid;

    private BigDecimal productquantity;

    private String productname;

    private String productfilename;

    private String productimageurl;

    public ProductInfo(String apimaterno, String apimatername, Long productskuid, String orderid, BigDecimal productquantity, String productname, String productfilename, String productimageurl) {
        this.apimaterno = apimaterno;
        this.apimatername = apimatername;
        this.productskuid = productskuid;
        this.orderid = orderid;
        this.productquantity = productquantity;
        this.productname = productname;
        this.productfilename = productfilename;
        this.productimageurl = productimageurl;
    }

    public ProductInfo() {
        super();
    }

    public String getApimaterno() {
        return apimaterno;
    }

    public void setApimaterno(String apimaterno) {
        this.apimaterno = apimaterno == null ? null : apimaterno.trim();
    }

    public String getApimatername() {
        return apimatername;
    }

    public void setApimatername(String apimatername) {
        this.apimatername = apimatername == null ? null : apimatername.trim();
    }

    public Long getProductskuid() {
        return productskuid;
    }

    public void setProductskuid(Long productskuid) {
        this.productskuid = productskuid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public BigDecimal getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(BigDecimal productquantity) {
        this.productquantity = productquantity;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public String getProductfilename() {
        return productfilename;
    }

    public void setProductfilename(String productfilename) {
        this.productfilename = productfilename == null ? null : productfilename.trim();
    }

    public String getProductimageurl() {
        return productimageurl;
    }

    public void setProductimageurl(String productimageurl) {
        this.productimageurl = productimageurl == null ? null : productimageurl.trim();
    }
}