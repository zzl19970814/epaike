package com.xinsteel.epaike.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinsteel.epaike.utils.ConstantPropertiesUtils;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.*;

/**
 * @author zhouzx
 */
public class SendMethods {

    private static String ACCESS_TOKEN;
    private static List<Map<String, String>> Message_List ;
    private static List<String> API_MATER_NO_LIST = new ArrayList<>();


    /**
     * 根据orderId查找易派客的信息
     * @param orderId orderId
     * @return 返回值
     */
    public static Map<String, String>  getYiPaiKeOrderInfo(String orderId){
        for (Map<String, String> orderInfo:
                Message_List){
            if(orderId.equals(orderInfo.get("orderId"))){
                return orderInfo;
            }
        }
        return null;
    }

    public static String getAccessTocken() {
        Map<String, String> params=new HashMap<>();
        params.put("client_id", ConstantPropertiesUtils.CLIENT_ID);
        params.put("client_secret", ConstantPropertiesUtils.CLIENT_SECRET);
        params.put("grant_type", ConstantPropertiesUtils.GRANT_TYPE);
        params.put("companyid", ConstantPropertiesUtils.COMPANY_ID);
        params.put("username", ConstantPropertiesUtils.USERNAME);
        params.put("password", ConstantPropertiesUtils.PASSWORD);

        String url = ConstantPropertiesUtils.URL + "/oauth/token";

        Set<String> keys = params.keySet();
        StringBuffer arg = new StringBuffer("?");
        for (String key : keys) {
            arg.append((key) + "=" + SubmitRequest.urlEncoderText(params.get(key)) + "&");
        }
        arg.deleteCharAt(arg.length() -1).toString();
        url = url + arg;
        System.out.println("url:"+url);
        String jsonStr = SSLRequest.sendSSLPostRequest(url,"");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String accessToken = jsonObject.getJSONObject("data").getString("access_token");
        ACCESS_TOKEN = accessToken;
        return accessToken;
    }


    public static List<Map<String, String>> getMessage() {
        JSONObject json = new JSONObject();
        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","100");
        String url = ConstantPropertiesUtils.URL + "/v2/message/getMessage";
        Set<String> keys = params.keySet();
        StringBuffer arg = new StringBuffer("?");
        for (String key : keys) {
            arg.append((key) + "=" + SubmitRequest.urlEncoderText(params.get(key)) + "&");
        }
        arg.deleteCharAt(arg.length() -1).toString();
        url = url + arg;
        System.out.println("url:"+url);
        String jsonStr = SSLRequest.sendSSLPostRequest(url,"");
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        List<Map<String, String>> messageList = new ArrayList<>();
        Map<String, String> temp = new HashMap<>();;
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 1; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            // 获取返回的messageBody
            JSONObject messageBody = jsonObject1.getJSONObject("messageBody");

            // messageId
            String messageId = jsonObject1.getString("messageId");
            // 获取eOrder
            JSONObject eOrder = messageBody.getJSONObject("eOrder");
            // 订单Id
            String orderId = eOrder.get("orderid").toString();
            // 创建时间
            String createtime = eOrder.getString("createtime");
            // 订单编号
            String orderno = eOrder.getString("orderno");
            // 采购商
            String purchasecompany = eOrder.getString("purchasecompany");
            String purchasecompanyid = eOrder.getString("purchasecompanyid");

            JSONArray eOrderSkulist = messageBody.getJSONArray("eOrderSkulist");
            // 单品描述
            String displayskuoptions = eOrderSkulist.getJSONObject(0).getString("displayskuoptions");
            // 易派客物料编码
            String materno = eOrderSkulist.getJSONObject(0).getString("materno");
            // 订单明细ID
            String orderskuid = eOrderSkulist.getJSONObject(0).getString("orderskuid");
            // 商品id
            String productid = eOrderSkulist.getJSONObject(0).getString("productid");
            // 商品名称
            String productname = eOrderSkulist.getJSONObject(0).getString("productname");
            // 单品id
            String productskuid = eOrderSkulist.getJSONObject(0).getString("productskuid");


            temp.put("messageId", messageId);
            temp.put("orderId", orderId);
            temp.put("createtime", createtime);
            temp.put("orderno", orderno);
            temp.put("purchasecompany", purchasecompany);
            temp.put("purchasecompanyid", purchasecompanyid);
            temp.put("displayskuoptions", displayskuoptions);
            temp.put("materno", materno);
            temp.put("orderskuid", orderskuid);
            temp.put("productid", productid);
            temp.put("productname", productname);
            temp.put("productskuid", productskuid);

            messageList.add(temp);
            temp = new HashMap<>();
        }

        Message_List = messageList;

        return messageList;

    }

    /**
     * 循环开始调用保存企业订单号
     * @return
     */
    public static void startSaveErpNo() throws Exception {

        for (Map<String, String> orderInfo:
        Message_List){
            String orderId = orderInfo.get("orderId");
            boolean b = saveEnterpriseErpOrderNo(orderId);
            if(!b){
                throw new Exception();
            }
        }
    }

    /**
     * 根据orderId上传企业订单号
     * @param orderId orderId
     * @return 返回
     */
    public static boolean saveEnterpriseErpOrderNo(String orderId) {

        // data 的json
        Map<String, Object> data = new HashMap<>();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);

        String erpOrderNo = "20201109";

        data.put("erpOrderNo", erpOrderNo);

        // 添加到Message_List中 后面的接口要用
        Map<String, String> map = new HashMap<>();
        getYiPaiKeOrderInfo(orderId).put("erpOrderNo", erpOrderNo);
        System.out.println(Message_List.toString());

        data.put("orderId", orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));

        String url = "/v2/supplychain/saveEnterpriseErpOrderNo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");

        String utilStr = utils(url, params);

        return utilStr.equals("true");

    }


    /**
     * 根据查询出来的易派客单品id上传供应商对应的商品id和名称
     */
    public static void uploadMaterInfo(){

        // 测试可用
//        getAccessTocken();
//        getMessage();

        for (Map<String, String> orderInfo:
                Message_List){
            String productSkuId = orderInfo.get("productskuid");

            // TODO 根据单品id查找供应商对应的商品id和名称

            String apiMaterName = "A商品";
            String apiMaterNo = "2020";

            API_MATER_NO_LIST.add(apiMaterNo);

            orderInfo.put("apiMaterNo", apiMaterNo);
            orderInfo.put("apiMaterName", apiMaterName);

            if(productSkuId.equals("79000012")) {

                saveEnterpriseMaterInfo(apiMaterName,apiMaterNo, productSkuId);
            }

        }
    }

    /**
     * 企业产品上传
     * @param apiMaterName 供应商产品名称
     * @param apiMaterNo 供应商产品代码
     * @param productSkuId 易派客单品id
     * @return
     */
    public static String saveEnterpriseMaterInfo(String apiMaterName, String apiMaterNo, String productSkuId) {
        // data 的json
        Map<String, Object> data = new HashMap<>();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        data.put("apimaterName", apiMaterName);
        data.put("apimaterNo", apiMaterNo);

        data.put("productskuid", productSkuId);
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        String url = "/v2/supplychain/saveEnterpriseMaterInfo";
        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");

        String utilsStr = utils(url, params);

        return utilsStr;



    }

    /**
     * 循环上传销售节点
     */
    public static void uploadWorkflowNode(){



        for (Map<String, String> orderInfo:
                Message_List){
            String orderId = orderInfo.get("orderId");
            saveEnterpriseWorkflowNode(orderId);

        }
    }


    public static String searchWorkflowNodeInfo(String orderId){
        //TODO 根据orderId查找销售节点数据
        return null;


    }


    /**
     * 根据orderId保存销售节点
     * @param orderId
     */
    public static void saveEnterpriseWorkflowNode(String orderId) {
        // data 的json
        Map<String, Object> data = new HashMap<>();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        data.put("orderId", orderId);
        data.put("orderno", getYiPaiKeOrderInfo(orderId).get("orderno"));

        data.put("erporderno", "20201109");

        data.put("productionname", "暂无");
        data.put("productionstate", "暂无");

        String projectname = System.currentTimeMillis()+"";
        data.put("projectname", projectname);

        // 后面的接口要用
        Map<String, String> map = new HashMap<>();
        getYiPaiKeOrderInfo(orderId).put("projectName", projectname);
        System.out.println(Message_List.toString());

        data.put("projectno", System.currentTimeMillis()+"");
        data.put("projectschedule", 0);
        // 生产进度延期状态
        data.put("projectschedulestate", 0);

        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        // ============================================

        JSONObject jsonObj = new JSONObject(data);
        System.out.println(jsonObj.toString());


        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");
        String url = "/v2/supplychain/saveEnterpriseWorkflowNode";

        String utilsStr = utils(url, params);




    }


    //==================================================未测试===========================================


    /**
     * 调用保存仓储信息的方法
     */
    public static void autoSavePlaceInfo(){
        for (String s : API_MATER_NO_LIST
             ) {
            saveEnterprisePlaceInfo(s);
        }
    }
    /**
     * 根据apiMaterNo保存仓储信息
     */
    public static void saveEnterprisePlaceInfo(String apiMaterNo){
        Map<String, Object> data = new HashMap<>();
        data.put("apimaterno", apiMaterNo);
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        data.put("productplace", "新钢公司");
        // 产品仓储数量
        data.put("productquantity", 200);
        data.put("productUnit", "吨");

        String url = "/v2/supplychain/saveEnterprisePlaceInfo";
        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");
        String utilsStr = utils(url, params);
    }

    /**
     * 调用上传企业原材料信息代码
     */
    public static void autoSaveMaterial(){
        for (String s : API_MATER_NO_LIST
             ) {
            saveEnterpriseMaterial(s);
        }
    }
    /**
     * 根据产品代码上传 企业原材料信息
     */
    public static String saveEnterpriseMaterial(String apiMaterNo){
        Map<String, Object> data = new HashMap<>();
        data.put("apimaterno", apiMaterNo);
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);

        //原材料产地(巴西)
        data.put("materialarea", "暂无");
        // 原材料成分(钢)


        
        data.put("materialcompose", "钢");
        // 原材料名称(钢材)
        data.put("materialname", "暂无");
        // 原材料编码(1002A001)
        data.put("materialno", "暂无");
        // 原材料计量单位
        data.put("materialunit", "吨");
        // 原材料采购数量
        data.put("quantity", 11);

        data.put("supplierCompanyName", "暂无");




        String url = "/v2/supplychain/saveEnterpriseMaterial";
        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");

        String utilsStr = utils(url, params);
        return utilsStr;
    }

    /**
     * 视频地址上传接口
     */
    public static String saveEnterpriseVideoFile (){
        Map<String, Object> data = new HashMap();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        // 文件名称(唯一性，图片名称，且带图片后缀)
        data.put("filename", "暂无");
        // 文件类型(0:视频)
        data.put("filetype", 0);
        data.put("fileurl", "暂无");
        // 是否公共附件(0：否,1:是)
        data.put("ispublic", 0);
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        String url = "/v2/supplychain/saveEnterpriseFile";
        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");
        String utilsStr = utils(url, params);
        return utilsStr;
    }

    /**
     * 循环上传检测图片
     */
    public static void autoSaveImageFile(){
        for (String s : API_MATER_NO_LIST
             ) {
            saveEnterpriseImageFile(s);
        }
    }
    /**
     * 检测报告图片
     */
    public static String saveEnterpriseImageFile(String apiMaterNo){
        Map<String, Object> data = new HashMap();
        data.put("apimaterno",apiMaterNo);
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        // 文件名称(图片名称，且带图片后缀，唯一性)
        data.put("filename", "暂无.jpg");
        //文件类型(1:检测报告,2:工艺流程)
        short i = 1;
        data.put("filetype", i);
        // 图片内容(传图片时为必填，将图片转为base64后再传输)
        data.put("imagecontent", "data:image/jpeg;base64,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");
        // 是否公共附件(0：否,1:是)
        data.put("ispublic", 0);
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        String url = "/v2/supplychain/saveEnterpriseImageFile";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data",jsonObj.toString());
        String utilsStr = utils(url, params);
        return utilsStr;
    }

    /**
     * 消息删除接口
     */
    public static boolean removeMessage(String[] messageIds){
        String url = ConstantPropertiesUtils.URL + "/v2/message/removeMessages";

        System.out.println(messageIds.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data",messageIds.toString());

        String utilsStr = utils(url, params);

        return utilsStr.equals("true");

    }


    /**
     * 循环保存订单概况信息
     */
    public static void autoSaveSurveyInfo(){
        for (Map<String, String> orderInfo : Message_List
             ) {
            String orderId = orderInfo.get("orderId");
            saveOrderSurveyInfo(orderId);
        }
    }
    /**
     * 订单概况信息上传接口
     */
    public static String saveOrderSurveyInfo(String orderId){
        Map<String, Object> data = new HashMap<>();

        data.put("erporderno", getYiPaiKeOrderInfo(orderId).get("erpOrderNo"));
        // 有无3D模型(0:无,1:有)
        data.put("has3dmodel", 0);


        data.put("ordercreatetime", getYiPaiKeOrderInfo(orderId).get("createtime"));
        data.put("orderId", orderId);
        data.put("orderno", getYiPaiKeOrderInfo(orderId).get("orderno"));

        // TODO 订单所涉及的项目名称（从之前输入的地方获取）

        data.put("projectname", getYiPaiKeOrderInfo(orderId).get("projectName"));

        data.put("purchasecompany", getYiPaiKeOrderInfo(orderId).get("purchasecompany"));
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        String url = "/v2/supplychain/saveOrderContractInfo";


        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
        return utilsStr;

    }

    /**
     * 循环保存合同签订概况信息
     */
    public static void autoSaveContractInfo(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderContractInfo(orderId);
        }
    }

    /**
     * 合同签订概况接口
     */
    public static void saveOrderContractInfo(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderno", getYiPaiKeOrderInfo(orderId).get("orderno"));
        data.put("orderId", orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // MES系统数据
        data.put("erporderno", getYiPaiKeOrderInfo(orderId).get("erpOrderNo"));
        data.put("nodecode", 1);

        data.put("purchasecompany", getYiPaiKeOrderInfo(orderId).get("purchasecompany"));

        // 合同生效日期 假设
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println(formatter.format(date));

        data.put("contractapprovaldate", formatter.format(date));

        data.put("deliverytime", formatter.format(date));

        data.put("filename", "暂无");
        data.put("imagecontent", "暂无");

        String url = "/v2/supplychain/saveOrderContractInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }

    /**
     * 循环保存合同签订详情信息
     */
    public static void autoSaveContractDetail(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderContractDetail(orderId);
        }
    }

    /**
     * 合同签订详情接口
     */
    public static void saveOrderContractDetail(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // MES系统数据
        data.put("apimaterno", getYiPaiKeOrderInfo(orderId).get("apiMaterNo"));
        data.put("apimatername", getYiPaiKeOrderInfo(orderId).get("apiMaterName"));

        data.put("productid", getYiPaiKeOrderInfo(orderId).get("productid"));

        // 固定值1，填写节点配置表的节点编号
        data.put("nodeCode", 1);
        data.put("quantity", 20);

        data.put("unit", "吨");
        //序号 唯一性验证
        data.put("serialNumber", 1);

        String url = "/v2/supplychain/saveOrderContractDetail";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);

    }




    /**
     * 雷达图节点配置接口
     */
    public static void saveOrderMapConfigure(String nodeName, Integer nodeCode){
        Map<String, Object> data = new HashMap<>();
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        //节点名称（合同签订、工程设计、采购、排产计划、入库、物流6项节点雷达图必须有。编号分别是1、2、3、4、19、20）
        data.put("nodename", nodeName);
        // 节点编码（企业自定义保证唯一，编码分别是，1到20排序）
        data.put("nodecode", nodeCode);
        // 备注（非必填）
        //data.put("remark", "暂无");
        String url = "/v2/supplychain/saveOrderAdarmapConfigure";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);

    }

    /**
     * 上传雷达图各节点进度
     */
    public static void autoSaveMapSchedule(){

        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            // 初始化
            int [] nodeCodes = new int[] {1,2,3,4,19,20};
            for (int i = 0; i < nodeCodes.length; i++) {
                saveOrderMapSchedule(orderId,0,nodeCodes[i]);
            }
        }

    }


    /**
     * 雷达图各节点进度接口
     */
    public static void saveOrderMapSchedule(String orderId, Integer nodeSchedule, Integer nodeCode){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyId"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 节点进度（0-100整数）
        data.put("nodeschedule",nodeSchedule);
        // 节点编码 填写节点配置表的节点编号（唯一性判断）
        data.put("nodecode",nodeCode);

        String url = "/v2/supplychain/saveOrderAdarmapSchedule";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }

    /**
     * 雷达图视频上传接口
     */
    public static void saveOrderMapVideo(){
        Map<String, Object> data = new HashMap<>();


        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 填写节点配置表的节点编号,确定唯一性
        data.put("nodeCode", "暂无");
        // 图片名称，且带图片后缀
        data.put("filename", "暂无");
        data.put("fileurl", "暂无");



        String url = "/v2/supplychain/saveOrderAdarmapVideo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }

    /**
     * 循环保存工程设计信息
     */
    public static void autoSaveEngineeringDesign(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderEngineeringDesign(orderId);
        }
    }


    /**
     * 工程设计信息上传接口
     */
    public static void saveOrderEngineeringDesign(String orderId){

        Map<String, Object> data = new HashMap<>();


        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 固定值位2，填写节点配置表的节点编号
        data.put("nodeCode", 2);

        data.put("designname",System.currentTimeMillis());
        data.put("designleader", "暂定");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        System.out.println(formatter.format(date));

        data.put("checkcompletetime", formatter.format(date));



        // 图片名称，且带图片后缀
        data.put("filename", "暂无.jpg");
        // 设计文档 图片，Base64编码
        data.put("designimage", "暂无");



        String url = "/v2/supplychain/saveOrderEngineeringDesign";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }



    /**
     * 循环保存原材料采购概况信息
     */
    public static void autoSaveMaterialSurvey(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderMaterialSurvey(orderId);
        }
    }

    /**
     * 原材料采购概况
     */
    public static void saveOrderMaterialSurvey(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);


        // 设计某个值
        data.put("nodecode",3);

        data.put("erporderno", getYiPaiKeOrderInfo(orderId).get("erpOrderNo"));

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//        System.out.println(formatter.format(date));

        //原材料到齐日期(yyyy-MM-dd HH:mm:ss格式)
        data.put("rawmaterialtime", formatter.format(date));
        //物料品类数(物料明细总数量)
        data.put("materialquantity", 2000);

        String url = "/v2/supplychain/saveOrderMaterialSurvey";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);


    }


    /**
     * 循环保存原材料采购详情信息
     */
    public static void autoSaveMaterialDetail(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderMaterialDetail(orderId);
        }
    }

    /**
     * 原材料采购详情
     */
    public static void saveOrderMaterialDetail(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);

        //序号
        data.put("serialNumber", 1);
        // 设计某个值
        data.put("nodecode",3);
        // 原材料名称
        data.put("materialName","");
        // 企业物料编码
        data.put("materialNo","");
        // 原材料使用数量
        data.put("rawMaterialOfUse","");
        // 原材料合格证书文件名称(图片名称，且带图片后缀)
        data.put("certificateFileName","");
        // 原材料合格证书（图片）(Base64编码) (非必填)
//        data.put("materialCertificate","");
        // 原材料出厂报文件名称(图片名称，且带图片后缀
        data.put("reportFileName","");
        //原材料出厂报告（图片）非必填（Base64编码）
        data.put("materialReport","");
        // 原材料供应商名称
        data.put("supplierCompanyName","");
        // 规格型号
        data.put("specifications","");

        String url = "/v2/supplychain/saveOrderMaterialSurvey";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }


    /**
     * 保存排产计划信息上传接口
     */
    public static void autoSaveProductionSchedulingInfo(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderProductionSchedulingInfo(orderId);
        }
    }

    /**
     * 排产计划信息上传接口
     */
    public static void saveOrderProductionSchedulingInfo(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        data.put("nodecode", 4);
        data.put("apimatername",getYiPaiKeOrderInfo(orderId).get("apiMaterName"));
        data.put("apimaterno",getYiPaiKeOrderInfo(orderId).get("apiMaterNo"));

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // 计划开始时间
        data.put("planbegintime",formatter.format(date));
        // 计划结束时间
        data.put("planendtime",formatter.format(date));

        // 固定名称
        data.put("leadername","xinsteel");

        String url = "/v2/supplychain/saveOrderProductionSchedulingInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }


    /**
     * 保存排产计划信息上传接口
     */
    public static void autoSaveManufacturingProcess(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderManufacturingProcess(orderId);
        }
    }

    /**
     * 生产制造过程概况接口
     */
    public static void saveOrderManufacturingProcess(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);
        data.put("orderno", getYiPaiKeOrderInfo(orderId).get("orderno"));


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // 计划开始时间
        data.put("plannedstarttime", formatter.format(date));

        //序号
        data.put("serialNumber","");
        // 设计某个值
        data.put("nodecode",4);
        // 原材料名称
        data.put("materialName","");
        // 企业物料编码
        data.put("materialNo","");
        // 原材料使用数量
        data.put("rawMaterialOfUse","");
        // 原材料合格证书文件名称(图片名称，且带图片后缀)
        data.put("certificateFileName","");
        // 原材料合格证书（图片）(Base64编码) (非必填)
//        data.put("materialCertificate","");
        // 原材料出厂报文件名称(图片名称，且带图片后缀
        data.put("reportFileName","");
        //原材料出厂报告（图片）非必填（Base64编码）
        data.put("materialReport","");
        // 原材料供应商名称
        data.put("supplierCompanyName","");
        // 规格型号
        data.put("specifications","");

        String url = "/v2/supplychain/saveOrderMaterialSurvey";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }


    /**
     * 保存排产计划信息上传接口
     */
    public static void autoSaveManufacturingDetail(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderManufacturingDetail(orderId);
        }
    }


    /**
     * 生产制造过程详情接口
     */
    public static void saveOrderManufacturingDetail(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 检验类型 (唯一性验证)
        data.put("inspectionType","");
        // 检验数量
        data.put("inspectionQuantity","");
        // 合格数量
        data.put("quantity","");
        // 检验合格率
        data.put("qualifiedRate","");
        // 节点编码(固定值 填写节点配置表的节点编号，唯一性验证)
        data.put("nodeCode",4);
        // 文件名称（图片名称，且带图片后缀）
        data.put("fileName","");
        // 节点检验记录（图片Base64）
        data.put("nodeCheckRecord","");


        String url = "/v2/supplychain/saveOrderManufacturingDetail";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }


    /**
     * 入库信息上传接口
     */
    public static void autoSaveOrderInputInfo(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderInputInfo(orderId);
        }
    }

    /**
     * 入库信息上传接口
     * @param orderId 订单id
     */
    public static void saveOrderInputInfo(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // TODO 把orderId对应 apimaterno
        data.put("apimaterNo", "");

        // 入库时间
        data.put("belaidupStartTime","");
        // 文件名称
        data.put("fileName","");
        // 产品出厂检验报告（图片Base64）
        data.put("inspectionReport","");
        // 固定的节点编码
        data.put("nodeCode",19);
        // 批次
        data.put("batch","");
        // 验收时间
        data.put("acceptanceTime","");
        // 检验数据
        data.put("inspectionData","");
        // 合格数量
        data.put("qualifiedQuantity","");
        // 合格率
        data.put("qualifiedRate","");






        String url = "/v2/supplychain/saveOrderInputInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }

    /**
     * 发货信息上传接口
     */
    public static void autoSaveShipmentImageFile(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveShipmentImageFile(orderId);
        }
    }


    /**
     * 发货信息上传接口
     * @param orderId
     */
    public static void saveShipmentImageFile(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // 物流方式
        data.put("logisticsType","");
        // 承运人
        data.put("carrier","");
        // 运单号
        data.put("billNo","");
        // 文件名称（图片名称，且带图片后缀）
        data.put("fileName","");
        // 发货单（图片,Base64编码）
        data.put("invoiceNo","");
        // 节点编码（固定值）
        data.put("nodeCode",20);



        String url = "/v2/supplychain/saveShipmentImageFile";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }


    /**
     * 订单产品展示信息上传接口
     * @param orderId
     */
    public static void saveOrderProductInfoNo3d(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);

        data.put("productSkuId", getYiPaiKeOrderInfo(orderId).get("productskuid"));
        data.put("productName",getYiPaiKeOrderInfo(orderId).get("productname"));

        // 文件名称(图片名称，且带图片后缀)
        data.put("fileName","");
        // 商品展示图片
        data.put("imageUrl","");

        // TODO 商品原材料编码（企业）
        data.put("materialNo","");
        // 商品原材料名称
        data.put("materialName","");
        // 原材料用量
        data.put("quantity","");
        // 原材料单位
        data.put("materialUnit","吨");

        // 原材料供应商名称
        data.put("supplierCompanyName","");






        String url = "/v2/supplychain/saveOrderProductInfoNo3d";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }

    /**
     * 物流跟踪信息上传接口
     * @param orderId
     */

    public static void saveOrderLogisticsInfo(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanid", getYiPaiKeOrderInfo(orderId).get("purchasecompanid"));
        data.put("suppliecompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // 阶段编号
        data.put("eventNumber","");
        // 阶段时间
        data.put("eventTime","");
        // 阶段地点
        data.put("eventPlace","");
        // TODO 物流单号
        data.put("billNo","");
        // 运输方式
        data.put("tansportType","");



        String url = "/v2/supplychain/saveOrderLogisticsInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }


























    /**
     * 发送请求的方法
     * @param url 接口地址
     * @param params 数据
     * @return 返回结果
     */
    private static String utils(String url, Map<String, String> params){
        url = ConstantPropertiesUtils.URL + url;
        Set<String> keys = params.keySet();
        StringBuffer arg = new StringBuffer("?");
        for (String key : keys) {
            arg.append((key) + "=" + SubmitRequest.urlEncoderText(params.get(key)) + "&");
        }
        arg.deleteCharAt(arg.length() -1).toString();
        url = url + arg;
        System.out.println("url:"+url);
        String jsonStr = SSLRequest.sendSSLPostRequest(url,"");
        System.out.println("jsonStr:"+jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (jsonObject.getString("data").equals("true")){
            return "true";
        }
        return "false";
    }






}
