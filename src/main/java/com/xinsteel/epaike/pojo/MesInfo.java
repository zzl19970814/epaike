package com.xinsteel.epaike.pojo;

import java.util.Date;

public class MesInfo {
    private String orderId;
    private String apiMaterNo;
    private String apiMaterName;
    private String erpOrderNo;
    private String productQuantity;
    private String orderCreateTime;
    private String NodeName;
    private String contractApproveDate;
    private Date deliveryTime;
    private String orderQuantity;
    private String planBeginTime;
    private String planEndTime;
    private String actualEndTime;



    @Override
    public String toString() {
        return "JsonAnalysis{" +
                "orderId='" + orderId + '\'' +
                ", apiMaterNo='" + apiMaterNo + '\'' +
                ", apiMaterName='" + apiMaterName + '\'' +
                ", erpOrderNo='" + erpOrderNo + '\'' +
                ", productQuantity='" + productQuantity + '\'' +
                ", orderCreateTime='" + orderCreateTime + '\'' +
                ", NodeName='" + NodeName + '\'' +
                ", contractApproveDate='" + contractApproveDate + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", orderQuantity='" + orderQuantity + '\'' +
                ", planBeginTime='" + planBeginTime + '\'' +
                ", planEndTime='" + planEndTime + '\'' +
                ", actualEndTime='" + actualEndTime + '\'' +
                ", warrantyFileName='" + warrantyFileName + '\'' +
                ", checkRecord='" + checkRecord + '\'' +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getApiMaterNo() {
        return apiMaterNo;
    }

    public void setApiMaterNo(String apiMaterNo) {
        this.apiMaterNo = apiMaterNo;
    }

    public String getApiMaterName() {
        return apiMaterName;
    }

    public void setApiMaterName(String apiMaterName) {
        this.apiMaterName = apiMaterName;
    }

    public String getErpOrderNo() {
        return erpOrderNo;
    }

    public void setErpOrderNo(String erpOrderNo) {
        this.erpOrderNo = erpOrderNo;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getContractApproveDate() {
        return contractApproveDate;
    }

    public void setContractApproveDate(String contractApproveDate) {
        this.contractApproveDate = contractApproveDate;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getPlanBeginTime() {
        return planBeginTime;
    }

    public void setPlanBeginTime(String planBeginTime) {
        this.planBeginTime = planBeginTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getWarrantyFileName() {
        return warrantyFileName;
    }

    public void setWarrantyFileName(String warrantyFileName) {
        this.warrantyFileName = warrantyFileName;
    }

    public String getCheckRecord() {
        return checkRecord;
    }

    public void setCheckRecord(String checkRecord) {
        this.checkRecord = checkRecord;
    }

    private String warrantyFileName;
    private String checkRecord;
}
