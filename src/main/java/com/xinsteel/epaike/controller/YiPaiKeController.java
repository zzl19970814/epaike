package com.xinsteel.epaike.controller;


import com.xinsteel.epaike.service.SendMethods;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author zhouzx
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class YiPaiKeController {

    @GetMapping("/gettoken")
    public String getToken(){
        String accessTocken = SendMethods.getAccessTocken();
        return accessTocken;
    }

    //每天自动调用一次，调用完就调用删除消息接口
    @GetMapping("/getMessage")
    public List getMessage(){
        List data = SendMethods.getMessage();
        return data;
    }

    /**
     * 企业订单回传
     * 只调用一次
     * @return
     */
    @GetMapping("/saveEnterpriseErpOrderNo")
    public String saveEnterpriseErpOrderNo(){
        try {
            SendMethods.startSaveErpNo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 企业产品上传
     * 拉取消息后调用
     */
    @GetMapping("/saveEnterpriseMaterInfo")
    public String saveEnterpriseMaterInfo(){
        SendMethods.uploadMaterInfo();
        return null;
    }

    /**
     * 销售订单流程节点上传接口
     * 订单节点发生变化后调用
     */
    @GetMapping("/saveEnterpriseWorkflowNode")
    public String saveEnterpriseWorkflowNode(){
        SendMethods.uploadWorkflowNode();
        return null;
    }

    /**
     * 企业产品仓储信息上传接口
     * 每天调用一次
     * 产品生产数量
     */
    @GetMapping("/saveEnterprisePlaceInfo")
    public void saveEnterprisePlaceInfo(){
        SendMethods.autoSavePlaceInfo();
    }

    /**
     * 企业原材料信息
     * getMessage后调用
     */
    @GetMapping("/saveEnterpriseMaterial")
    public void saveEnterpriseMaterial(){
        SendMethods.autoSaveMaterial();

    }

    /**
     * 原材料仓储
     */
    @GetMapping("/saveEnterpriseMateriaPlace")
    public void saveEnterpriseMateriaPlace(){
        SendMethods.saveEnterpriseMateriaPlace();
    }

    /**
     * 视频地址上传接口
     * 随时可以调用且只需调用一次
     */
    @GetMapping("/saveEnterpriseVideoFile")
    public void saveEnterpriseVideoFile(){
        SendMethods.saveEnterpriseVideoFile();
    }

    /**
     * 检测报告图片
     * 订单节点改变为发运后调用
     */
    @GetMapping("/saveEnterpriseImageFile")
    public void  saveEnterpriseImageFile() {
        SendMethods.autoSaveImageFile();
    }

    /**
     * 订单概况信息上传接口
     * getMessage后调用
     */
    @GetMapping("/saveOrderSurveyInfo")
    public void saveOrderSurveyInfo(){
        SendMethods.autoSaveSurveyInfo();
    }

    /**
     * 合同签订概况接口
     * getMessage后调用
     */
        @GetMapping("/saveOrderContractInfo")
    public void saveOrderContractInfo(){
        SendMethods.autoSaveContractInfo();
    }

    /**
     * 合同签订详情接口
     * getMessage后调用
     */
    @GetMapping("/saveOrderContractDetail")
    public void saveOrderContractDetail(){
        SendMethods.autoSaveContractDetail();
    }

    /**
     * 雷达图节点配置
     * getMessage前调用
     */
    @GetMapping("/saveOrderMapConfigure")
    public void saveOrderMapConfigure(){
        // 加个生产过程nodecode5 nodename为生产制造
        int [] nodeCodes = new int[] {1,2,3,4,5,19,20};
        String [] nodeNames = new String[]{"合同签订","工程设计","采购","排产计划","生产制造","入库","物流"};
        for (int i = 0; i < nodeCodes.length; i++) {
            SendMethods.saveOrderMapConfigure(nodeNames[i], nodeCodes[i]);
        }

    }
    /**
     * 雷达图节点各进度
     * 每天调用一次，需要mes的接口与数据库对比nodecode判断是否要调用此方法
     */
    @GetMapping("/saveOrderMapSchedule")
    public void saveOrderMapSchedule(){
        SendMethods.autoSaveMapSchedule();
    }

    /**
     * 雷达图视频
     */
    @GetMapping("/saveOrderMapVideo")
    public void saveOrderMapVideo(){
        SendMethods.saveOrderMapVideo();
    }

    /**
     * 雷达图工程设计信息
     * getMessage之后调用
     */
    @GetMapping("/saveOrderEngineeringDesign")
    public void saveOrderEngineeringDesign(){
        SendMethods.autoSaveEngineeringDesign();

    }

    /**
     * 保存原材料采购概况信息
     * getMessage之后调用
     *
     */
    @GetMapping("/saveOrderMaterialSurvey")
    public void saveOrderMaterialSurvey(){
        SendMethods.autoSaveMaterialSurvey();
    }


    /**
     * 保存原材料采购详情信息
     * getMessage之后调用
     */
    @GetMapping("/saveOrderMaterialDetail")
    public void saveOrderMaterialDetail(){
        SendMethods.autoSaveMaterialDetail();
    }

    /**
     * 排产计划信息上传接口
     * nodecode更改到 4 之后调用
     */
    @GetMapping("/saveOrderProductionSchedulingInfo")
    public void saveOrderProductionSchedulingInfo(){
        SendMethods.autoSaveProductionSchedulingInfo();
    }

    /**
     * 生产制造过程概况接口
     * nodecode 为 5 时调用
     */
    @GetMapping("/saveOrderManufacturingProcess")
    public void saveOrderManufacturingProcess(){
        SendMethods.autoSaveManufacturingProcess();
    }

    /**
     * 生产制造过程详情接口
     * nodecode为 5 时调用
     */
    @GetMapping("/saveOrderManufacturingDetail")
    public void saveOrderManufacturingDetail(){
        SendMethods.autoSaveManufacturingDetail();
    }

    /**
     * 入库信息上传接口
     * nodecode 为 19 时调用
     */
    @GetMapping("/saveOrderInputInfo")
    public void saveOrderInputInfo(){
        SendMethods.autoSaveOrderInputInfo();
    }

    /**
     * 入库信息上传接口
     * nodecode为 19 时调用
     */
    @GetMapping("/saveShipmentImageFile")
    public void saveShipmentImageFile(){
        SendMethods.autoSaveShipmentImageFile();
    }

    /**
     * 订单产品展示信息上传接口
     */
    @GetMapping("/saveOrderProductInfoNo3d")
    public void saveOrderProductInfoNo3d(){
        SendMethods.autoSaveOrderProductInfoNo3d();
    }

    /**
     * 物流跟踪信息上传接口
     */
    @GetMapping("/saveOrderLogisticsInfo")
    public void saveOrderLogisticsInfo(){
        SendMethods.autoSaveOrderLogisticsInfo();
    }






}
