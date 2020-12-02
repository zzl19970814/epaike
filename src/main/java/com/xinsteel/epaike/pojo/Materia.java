package com.xinsteel.epaike.pojo;

import java.math.BigDecimal;

public class Materia {

    private String materialno;

    private String materialname;

    private String suppliercompanyname;

    private BigDecimal quantity;

    private String materialunit;

    private BigDecimal materialquantity;

    private BigDecimal rawmaterialofuse;

    private String specifications;

    public Materia( String materialno, String materialname, String suppliercompanyname, BigDecimal quantity, String materialunit, BigDecimal materialquantity, BigDecimal rawmaterialofuse, String specifications) {
        this.materialno = materialno;
        this.materialname = materialname;
        this.suppliercompanyname = suppliercompanyname;
        this.quantity = quantity;
        this.materialunit = materialunit;
        this.materialquantity = materialquantity;
        this.rawmaterialofuse = rawmaterialofuse;
        this.specifications = specifications;
    }

    public Materia() {
        super();
    }

    public String getMaterialno() {
        return materialno;
    }

    public void setMaterialno(String materialno) {
        this.materialno = materialno == null ? null : materialno.trim();
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname == null ? null : materialname.trim();
    }

    public String getSuppliercompanyname() {
        return suppliercompanyname;
    }

    public void setSuppliercompanyname(String suppliercompanyname) {
        this.suppliercompanyname = suppliercompanyname == null ? null : suppliercompanyname.trim();
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getMaterialunit() {
        return materialunit;
    }

    public void setMaterialunit(String materialunit) {
        this.materialunit = materialunit == null ? null : materialunit.trim();
    }

    public BigDecimal getMaterialquantity() {
        return materialquantity;
    }

    public void setMaterialquantity(BigDecimal materialquantity) {
        this.materialquantity = materialquantity;
    }

    public BigDecimal getRawmaterialofuse() {
        return rawmaterialofuse;
    }

    public void setRawmaterialofuse(BigDecimal rawmaterialofuse) {
        this.rawmaterialofuse = rawmaterialofuse;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications == null ? null : specifications.trim();
    }
}