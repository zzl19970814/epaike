package com.xinsteel.epaike.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo {
    private String orderid;

    private String orderno;

    private Long purchasecompanyid;

    private String purchasecompany;

    private Long productid;

    private String erporderno;

    private String createtime;

    private String projectno;

    private String projectname;

    private Integer projectschedule;

    private String productionstate;

    private String productionname;

    private String videofilename;

    private String videofileurl;

    private Date contractapprovaldate;

    private Date deliverytime;

    private String contractfilename;

    private String contractimagecontent;

    private Date rawmaterialtime;

    private Date planbegintime;

    private Date planendtime;

    private Date plannedstarttime;

    private Date plannedendtime;

    private Date actualstarttime;

    private Date actualendtime;

    private String inspectiontype;

    private BigDecimal inspectionquantity;

    private BigDecimal qualifiedquantity;

    private BigDecimal qualifiedrate;

    private String nodecheckfilename;

    private String nodecheckrecord;

    private Date belaidupstarttime;

    private String inspectionreportfilename;

    private String inspectionreport;

    private String batch;

    private Date acceptancetime;

    private BigDecimal inspectiondata;

    private BigDecimal storagequalifiedquantity;

    private BigDecimal storagequalifiedrate;

    private String logisticstype;

    private String billno;

    private String transporttype;

    private String invoicefilename;

    private String invoiceno;

    public OrderInfo(String orderid, String orderno, Long purchasecompanyid, String purchasecompany, Long productid, String erporderno, String createtime, String projectno, String projectname, Integer projectschedule, String productionstate, String productionname, String videofilename, String videofileurl, Date contractapprovaldate, Date deliverytime, String contractfilename, String contractimagecontent, Date rawmaterialtime, Date planbegintime, Date planendtime, Date plannedstarttime, Date plannedendtime, Date actualstarttime, Date actualendtime, String inspectiontype, BigDecimal inspectionquantity, BigDecimal qualifiedquantity, BigDecimal qualifiedrate, String nodecheckfilename, String nodecheckrecord, Date belaidupstarttime, String inspectionreportfilename, String inspectionreport, String batch, Date acceptancetime, BigDecimal inspectiondata, BigDecimal storagequalifiedquantity, BigDecimal storagequalifiedrate, String logisticstype, String billno, String transporttype, String invoicefilename, String invoiceno) {
        this.orderid = orderid;
        this.orderno = orderno;
        this.purchasecompanyid = purchasecompanyid;
        this.purchasecompany = purchasecompany;
        this.productid = productid;
        this.erporderno = erporderno;
        this.createtime = createtime;
        this.projectno = projectno;
        this.projectname = projectname;
        this.projectschedule = projectschedule;
        this.productionstate = productionstate;
        this.productionname = productionname;
        this.videofilename = videofilename;
        this.videofileurl = videofileurl;
        this.contractapprovaldate = contractapprovaldate;
        this.deliverytime = deliverytime;
        this.contractfilename = contractfilename;
        this.contractimagecontent = contractimagecontent;
        this.rawmaterialtime = rawmaterialtime;
        this.planbegintime = planbegintime;
        this.planendtime = planendtime;
        this.plannedstarttime = plannedstarttime;
        this.plannedendtime = plannedendtime;
        this.actualstarttime = actualstarttime;
        this.actualendtime = actualendtime;
        this.inspectiontype = inspectiontype;
        this.inspectionquantity = inspectionquantity;
        this.qualifiedquantity = qualifiedquantity;
        this.qualifiedrate = qualifiedrate;
        this.nodecheckfilename = nodecheckfilename;
        this.nodecheckrecord = nodecheckrecord;
        this.belaidupstarttime = belaidupstarttime;
        this.inspectionreportfilename = inspectionreportfilename;
        this.inspectionreport = inspectionreport;
        this.batch = batch;
        this.acceptancetime = acceptancetime;
        this.inspectiondata = inspectiondata;
        this.storagequalifiedquantity = storagequalifiedquantity;
        this.storagequalifiedrate = storagequalifiedrate;
        this.logisticstype = logisticstype;
        this.billno = billno;
        this.transporttype = transporttype;
        this.invoicefilename = invoicefilename;
        this.invoiceno = invoiceno;
    }

    public OrderInfo() {
        super();
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public Long getPurchasecompanyid() {
        return purchasecompanyid;
    }

    public void setPurchasecompanyid(Long purchasecompanyid) {
        this.purchasecompanyid = purchasecompanyid;
    }

    public String getPurchasecompany() {
        return purchasecompany;
    }

    public void setPurchasecompany(String purchasecompany) {
        this.purchasecompany = purchasecompany == null ? null : purchasecompany.trim();
    }

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

    public String getErporderno() {
        return erporderno;
    }

    public void setErporderno(String erporderno) {
        this.erporderno = erporderno == null ? null : erporderno.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getProjectno() {
        return projectno;
    }

    public void setProjectno(String projectno) {
        this.projectno = projectno == null ? null : projectno.trim();
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname == null ? null : projectname.trim();
    }

    public Integer getProjectschedule() {
        return projectschedule;
    }

    public void setProjectschedule(Integer projectschedule) {
        this.projectschedule = projectschedule;
    }

    public String getProductionstate() {
        return productionstate;
    }

    public void setProductionstate(String productionstate) {
        this.productionstate = productionstate == null ? null : productionstate.trim();
    }

    public String getProductionname() {
        return productionname;
    }

    public void setProductionname(String productionname) {
        this.productionname = productionname == null ? null : productionname.trim();
    }

    public String getVideofilename() {
        return videofilename;
    }

    public void setVideofilename(String videofilename) {
        this.videofilename = videofilename == null ? null : videofilename.trim();
    }

    public String getVideofileurl() {
        return videofileurl;
    }

    public void setVideofileurl(String videofileurl) {
        this.videofileurl = videofileurl == null ? null : videofileurl.trim();
    }

    public Date getContractapprovaldate() {
        return contractapprovaldate;
    }

    public void setContractapprovaldate(Date contractapprovaldate) {
        this.contractapprovaldate = contractapprovaldate;
    }

    public Date getDeliverytime() {
        return deliverytime;
    }

    public void setDeliverytime(Date deliverytime) {
        this.deliverytime = deliverytime;
    }

    public String getContractfilename() {
        return contractfilename;
    }

    public void setContractfilename(String contractfilename) {
        this.contractfilename = contractfilename == null ? null : contractfilename.trim();
    }

    public String getContractimagecontent() {
        return contractimagecontent;
    }

    public void setContractimagecontent(String contractimagecontent) {
        this.contractimagecontent = contractimagecontent == null ? null : contractimagecontent.trim();
    }

    public Date getRawmaterialtime() {
        return rawmaterialtime;
    }

    public void setRawmaterialtime(Date rawmaterialtime) {
        this.rawmaterialtime = rawmaterialtime;
    }

    public Date getPlanbegintime() {
        return planbegintime;
    }

    public void setPlanbegintime(Date planbegintime) {
        this.planbegintime = planbegintime;
    }

    public Date getPlanendtime() {
        return planendtime;
    }

    public void setPlanendtime(Date planendtime) {
        this.planendtime = planendtime;
    }

    public Date getPlannedstarttime() {
        return plannedstarttime;
    }

    public void setPlannedstarttime(Date plannedstarttime) {
        this.plannedstarttime = plannedstarttime;
    }

    public Date getPlannedendtime() {
        return plannedendtime;
    }

    public void setPlannedendtime(Date plannedendtime) {
        this.plannedendtime = plannedendtime;
    }

    public Date getActualstarttime() {
        return actualstarttime;
    }

    public void setActualstarttime(Date actualstarttime) {
        this.actualstarttime = actualstarttime;
    }

    public Date getActualendtime() {
        return actualendtime;
    }

    public void setActualendtime(Date actualendtime) {
        this.actualendtime = actualendtime;
    }

    public String getInspectiontype() {
        return inspectiontype;
    }

    public void setInspectiontype(String inspectiontype) {
        this.inspectiontype = inspectiontype == null ? null : inspectiontype.trim();
    }

    public BigDecimal getInspectionquantity() {
        return inspectionquantity;
    }

    public void setInspectionquantity(BigDecimal inspectionquantity) {
        this.inspectionquantity = inspectionquantity;
    }

    public BigDecimal getQualifiedquantity() {
        return qualifiedquantity;
    }

    public void setQualifiedquantity(BigDecimal qualifiedquantity) {
        this.qualifiedquantity = qualifiedquantity;
    }

    public BigDecimal getQualifiedrate() {
        return qualifiedrate;
    }

    public void setQualifiedrate(BigDecimal qualifiedrate) {
        this.qualifiedrate = qualifiedrate;
    }

    public String getNodecheckfilename() {
        return nodecheckfilename;
    }

    public void setNodecheckfilename(String nodecheckfilename) {
        this.nodecheckfilename = nodecheckfilename == null ? null : nodecheckfilename.trim();
    }

    public String getNodecheckrecord() {
        return nodecheckrecord;
    }

    public void setNodecheckrecord(String nodecheckrecord) {
        this.nodecheckrecord = nodecheckrecord == null ? null : nodecheckrecord.trim();
    }

    public Date getBelaidupstarttime() {
        return belaidupstarttime;
    }

    public void setBelaidupstarttime(Date belaidupstarttime) {
        this.belaidupstarttime = belaidupstarttime;
    }

    public String getInspectionreportfilename() {
        return inspectionreportfilename;
    }

    public void setInspectionreportfilename(String inspectionreportfilename) {
        this.inspectionreportfilename = inspectionreportfilename == null ? null : inspectionreportfilename.trim();
    }

    public String getInspectionreport() {
        return inspectionreport;
    }

    public void setInspectionreport(String inspectionreport) {
        this.inspectionreport = inspectionreport == null ? null : inspectionreport.trim();
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch == null ? null : batch.trim();
    }

    public Date getAcceptancetime() {
        return acceptancetime;
    }

    public void setAcceptancetime(Date acceptancetime) {
        this.acceptancetime = acceptancetime;
    }

    public BigDecimal getInspectiondata() {
        return inspectiondata;
    }

    public void setInspectiondata(BigDecimal inspectiondata) {
        this.inspectiondata = inspectiondata;
    }

    public BigDecimal getStoragequalifiedquantity() {
        return storagequalifiedquantity;
    }

    public void setStoragequalifiedquantity(BigDecimal storagequalifiedquantity) {
        this.storagequalifiedquantity = storagequalifiedquantity;
    }

    public BigDecimal getStoragequalifiedrate() {
        return storagequalifiedrate;
    }

    public void setStoragequalifiedrate(BigDecimal storagequalifiedrate) {
        this.storagequalifiedrate = storagequalifiedrate;
    }

    public String getLogisticstype() {
        return logisticstype;
    }

    public void setLogisticstype(String logisticstype) {
        this.logisticstype = logisticstype == null ? null : logisticstype.trim();
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno == null ? null : billno.trim();
    }

    public String getTransporttype() {
        return transporttype;
    }

    public void setTransporttype(String transporttype) {
        this.transporttype = transporttype == null ? null : transporttype.trim();
    }

    public String getInvoicefilename() {
        return invoicefilename;
    }

    public void setInvoicefilename(String invoicefilename) {
        this.invoicefilename = invoicefilename == null ? null : invoicefilename.trim();
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno == null ? null : invoiceno.trim();
    }
}