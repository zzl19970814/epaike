package com.xinsteel.epaike.controller;


import com.xinsteel.epaike.service.SendMethods;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author zhouzx
 */
@RestController
@RequestMapping("/api")
public class YiPaiKeController {

    @GetMapping("/gettoken")
    public String getToken(){
        String accessTocken = SendMethods.getAccessTocken();
        return accessTocken;
    }

    @GetMapping("/getMessage")
    public List getMessage(){
        List data = SendMethods.getMessage();
        return data;
    }

    /**
     * 企业订单回传
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
     */
    @GetMapping("/saveEnterpriseMaterInfo")
    public String saveEnterpriseMaterInfo(){
        SendMethods.uploadMaterInfo();
        return null;
    }

    /**
     * 销售订单流程节点上传接口
     */
    @GetMapping("/saveEnterpriseWorkflowNode")
    public String saveEnterpriseWorkflowNode(){
        SendMethods.uploadWorkflowNode();
        return null;
    }

    /**
     * 企业产品仓储信息上传接口
     */
    @GetMapping("/saveEnterprisePlaceInfo")
    public void saveEnterprisePlaceInfo(){
        SendMethods.autoSavePlaceInfo();
    }

    /**
     * 企业原材料信息
     */
    @GetMapping("/saveEnterpriseMaterial")
    public void saveEnterpriseMaterial(){
        SendMethods.autoSaveMaterial();

    }

    /**
     * 视频地址上传接口
     */
    @GetMapping("/saveEnterpriseVideoFile")
    public void saveEnterpriseVideoFile(){
        SendMethods.saveEnterpriseVideoFile();
    }

    /**
     * 检测报告图片
     */
    @GetMapping("/saveEnterpriseImageFile")
    public void  saveEnterpriseImageFile() {
        SendMethods.autoSaveImageFile();
    }

    /**
     * 订单概况信息上传接口
     */
    @GetMapping("/saveOrderSurveyInfo")
    public void saveOrderSurveyInfo(){
        SendMethods.autoSaveSurveyInfo();
    }

    /**
     * 合同签订概况接口
     */
    @GetMapping("/saveOrderContractInfo")
    public void saveOrderContractInfo(){
        SendMethods.autoSaveContractInfo();
    }

    /**
     * 合同签订详情接口
     */
    @GetMapping("/saveOrderContractDetail")
    public void saveOrderContractDetail(){
        SendMethods.autoSaveContractDetail();
    }

    /**
     * 雷达图节点配置
     */
    @GetMapping("/saveOrderMapConfigure")
    public void saveOrderMapConfigure(){
        int [] nodeCodes = new int[] {1,2,3,4,19,20};
        String [] nodeNames = new String[]{"合同签订","工程设计","采购","排产计划","入库","物流"};
        for (int i = 0; i < nodeCodes.length; i++) {

            SendMethods.saveOrderMapConfigure(nodeNames[i], nodeCodes[i]);
        }

    }
    /**
     * 雷达图节点各进度
     */
    @GetMapping("/saveOrderMapSchedule")
    public void saveOrderMapSchedule(){
        SendMethods.autoSaveMapSchedule();

    }


    /**
     * 雷达图节点各进度
     */
    @GetMapping("/saveOrderEngineeringDesign")
    public void saveOrderEngineeringDesign(){
        SendMethods.autoSaveEngineeringDesign();

    }

    /**
     * 循环保存原材料采购概况信息
     */
    @GetMapping("/saveOrderMaterialSurvey")
    public void saveOrderMaterialSurvey(){
        SendMethods.autoSaveMaterialSurvey();
    }


    /**
     * 保存原材料采购详情信息
     */
    @GetMapping("/saveOrderMaterialDetail")
    public void saveOrderMaterialDetail(){
        SendMethods.autoSaveMaterialDetail();
    }





    /**
     * 排产计划信息上传接口
     */
    @GetMapping("/saveOrderProductionSchedulingInfo")
    public void saveOrderProductionSchedulingInfo(){
        SendMethods.autoSaveProductionSchedulingInfo();
    }

    /**
     * 生产制造过程概况接口
     */
    @GetMapping("/saveOrderManufacturingProcess")
    public void saveOrderManufacturingProcess(){
        SendMethods.autoSaveManufacturingProcess();
    }

    /**
     * 生产制造过程详情接口
     */
    @GetMapping("/saveOrderManufacturingDetail")
    public void saveOrderManufacturingDetail(){
        SendMethods.autoSaveManufacturingDetail();
    }

    /**
     * 入库信息上传接口
     */
    @GetMapping("/saveOrderInputInfo")
    public void saveOrderInputInfo(){
        SendMethods.autoSaveOrderInputInfo();
    }

    /**
     * 入库信息上传接口
     */
    @GetMapping("/saveShipmentImageFile")
    public void saveShipmentImageFile(){
        SendMethods.autoSaveShipmentImageFile();
    }





}
