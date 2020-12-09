package com.xinsteel.epaike.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.pcitc.apiapplication.service.api.dto.EpecResult;
import com.pcitc.apiapplication.service.api.ssl.EpecApiUtil;
import com.xinsteel.epaike.dao.MateriaMapper;
import com.xinsteel.epaike.dao.OrderInfoMapper;
import com.xinsteel.epaike.dao.ProductInfoMapper;
import com.xinsteel.epaike.pojo.Materia;
import com.xinsteel.epaike.pojo.OrderInfo;
import com.xinsteel.epaike.pojo.ProductInfo;
import com.xinsteel.epaike.utils.Base64EncodeUtils;
import com.xinsteel.epaike.utils.ConstantPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class SendMethods {

    private static String ACCESS_TOKEN;
    private static List<Map<String, String>> Message_List ;

    @Autowired
    private MateriaMapper materiaMapper;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    private static SendMethods sendMethods;

    @PostConstruct
    public void init() {
        sendMethods = this;
        sendMethods.orderInfoMapper = this.orderInfoMapper;
        sendMethods.productInfoMapper = this.productInfoMapper;
        sendMethods.materiaMapper = this.materiaMapper;
    }




    public static List getOrderId(){
        List orderIdList = new ArrayList();
        List<OrderInfo> resultList = sendMethods.orderInfoMapper.selectAllOrderInfo();
        for (OrderInfo orderInfo:
                resultList) {
            orderIdList.add(orderInfo.getOrderid());
        }
        return orderIdList;
    }
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
        ProductInfo productInfo = new ProductInfo();
        OrderInfo orderInfo = new OrderInfo();
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
            //判断orderId是否为空，为空不调用，不为空就调用
            orderInfo.setOrderid(eOrder.get("orderid").toString());
            // 创建时间
            orderInfo.setCreatetime(eOrder.getString("createtime"));
            // 订单编号
            orderInfo.setOrderno(eOrder.getString("orderno"));
            // 采购商
            orderInfo.setPurchasecompany(eOrder.getString("purchasecompany"));
            orderInfo.setPurchasecompanyid((long) Integer.parseInt(eOrder.getString("purchasecompanyid")));

            JSONArray eOrderSkulist = messageBody.getJSONArray("eOrderSkulist");
            // 单品描述
            productInfo.setApimatername(eOrderSkulist.getJSONObject(0).getString("displayskuoptions"));
//            // 易派客物料编码
//            String materno = eOrderSkulist.getJSONObject(0).getString("materno");
            // 订单明细ID
//            String orderskuid = eOrderSkulist.getJSONObject(0).getString("orderskuid");
            // 商品id
            orderInfo.setProductid((long) Integer.parseInt(eOrderSkulist.getJSONObject(0).getString("productid")));
            // 商品名称
            orderInfo.setProductname(eOrderSkulist.getJSONObject(0).getString("productname"));
            // 单品id
            productInfo.setProductskuid((long) Integer.parseInt(eOrderSkulist.getJSONObject(0).getString("productskuid")));


            temp.put("messageId", messageId);
//            temp.put("orderId", orderId);
//            temp.put("createtime", createtime);
//            temp.put("orderno", orderno);
//            temp.put("purchasecompany", purchasecompany);
//            temp.put("purchasecompanyid", purchasecompanyid);
//            temp.put("displayskuoptions", displayskuoptions);
//            temp.put("materno", materno);
//            temp.put("orderskuid", orderskuid);
//            temp.put("productid", productid);
//            temp.put("productname", productname);
//            temp.put("productskuid", productskuid);

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

        List<OrderInfo> resultList = sendMethods.orderInfoMapper.selectAllOrderInfo();
        for (OrderInfo orderInfo:
                resultList) {
            String orderid = orderInfo.getOrderid();
            boolean b = saveEnterpriseErpOrderNo(orderid);
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

        OrderInfo orderInfo = new OrderInfo();

        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        String erpOrderNo = orderInfo.getErporderno();

        data.put("erpOrderNo", erpOrderNo);

        // 添加到Message_List中 后面的接口要用
        Map<String, String> map = new HashMap<>();
        //getYiPaiKeOrderInfo(orderId).put("erpOrderNo", erpOrderNo);
//        System.out.println(Message_List.toString());

        data.put("orderId", orderId);
        data.put("purchasecompanyid",orderInfo.getPurchasecompanyid());
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
        List<ProductInfo> resultList = sendMethods.productInfoMapper.selectAllProductInfo();
        int count = 0;
        for (ProductInfo productInfo:
                resultList){
            Long productSkuId = productInfo.getProductskuid();
            String apiMaterName = productInfo.getApimatername();
            String apiMaterNo = productInfo.getApimaterno();
            saveEnterpriseMaterInfo(apiMaterName,apiMaterNo, productSkuId, count);
            count++;
        }
    }

    /**
     * 企业产品上传
     * @param apiMaterName 供应商产品名称
     * @param apiMaterNo 供应商产品代码
     * @param productSkuId 易派客单品id
     * @return
     */
    public static String saveEnterpriseMaterInfo(String apiMaterName, String apiMaterNo, Long productSkuId, int count) {
        // data 的json
        Map<String, Object> data = new HashMap<>();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        data.put("apimaterName", apiMaterName);
        data.put("apimaterNo", apiMaterNo);

        data.put("productskuid", productSkuId);
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        if (count == 0){
            data.put("defaultProduct",1);
        }

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

        List orderIdList = getOrderId();
        for (int i = 0; i <orderIdList.size() ; i++) {
            saveEnterpriseWorkflowNode(orderIdList.get(i).toString());
        }
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
        OrderInfo orderInfo = new OrderInfo();

        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("orderno", orderInfo.getOrderno());

        data.put("erporderno", orderInfo.getErporderno());

        data.put("productionname", orderInfo.getProductionname());
        data.put("productionstate", orderInfo.getProductionstate());
        data.put("projectname", orderInfo.getProjectname());
        data.put("projectno", orderInfo.getProjectno());
        data.put("projectschedule", orderInfo.getProjectschedule());
        // 生产进度延期状态
        data.put("projectschedulestate", 0);
        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        data.put("skiperp", 1);
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
        List apiMaterNoList = sendMethods.productInfoMapper.selectAllApiMaterNo();
        for (int i = 0; i < apiMaterNoList.size(); i++) {
                saveEnterprisePlaceInfo(apiMaterNoList.get(i).toString());
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
        Double productquantity = sendMethods.productInfoMapper.selectProductQuantityByApiMaterNo(apiMaterNo);
        data.put("productquantity", productquantity);
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
        // xg
        List<ProductInfo> resultList = sendMethods.productInfoMapper.selectAllProductInfo();
        for (int i = 0; i < resultList.size(); i++) {
                List materialNoList = sendMethods.materiaMapper.selectAllMaterialNo();
                for (int j = 0; j <materialNoList.size() ; j++) {

                    saveEnterpriseMaterial(resultList.get(i).getApimaterno(),materialNoList.get(j).toString());
                }
        }


    }
    /**
     * 根据产品代码上传 企业原材料信息
     */
    public static String saveEnterpriseMaterial(String apiMaterNo, String materialno){
        Map<String, Object> data = new HashMap<>();
        data.put("apimaterno", apiMaterNo);
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);

        Materia materia = sendMethods.materiaMapper.selectMaterialByMaterialNo(materialno);

        //原材料产地(巴西)
        data.put("materialarea", materia.getSuppliercompanyname());
        // 原材料成分(钢)
        data.put("materialcompose", "暂无");
        // 原材料名称(钢材)
        data.put("materialname", materia.getMaterialname());
        // 原材料编码(1002A001)
        data.put("materialno", materia.getMaterialno());
        // 原材料计量单位
        data.put("materialunit", materia.getMaterialunit());
        // 原材料采购数量
        data.put("quantity", materia.getMaterialquantity());

        data.put("supplierCompanyName", materia.getSuppliercompanyname());

        String url = "/v2/supplychain/saveEnterpriseMaterial";
        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");

        String utilsStr = utils(url, params);
        return utilsStr;
    }

    public static void startEnterpriseMateriaPlace(){
        List list = sendMethods.materiaMapper.selectAllMaterialNo();
        for (int i = 0; i <  list.size(); i++) {
            saveEnterpriseMateriaPlace(list.get(i).toString());
        }
    }

    public static String saveEnterpriseMateriaPlace(String materialno){
        Map<String, Object> data = new HashMap<>();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        data.put("materialno",materialno);
        Materia materia = sendMethods.materiaMapper.selectMaterialByMaterialNo(materialno);
        data.put("materialPlace","新钢");
        data.put("materialQuantity",materia.getMaterialquantity());
        data.put("materialUnit", materia.getMaterialunit());

        String url = "/v2/supplychain/saveEnterpriseMateriaPlace";
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
        data.put("filename", "xinsteelVideo.mp4");
        // 文件类型(0:视频)
        data.put("filetype", 0);
        data.put("fileurl", "https://www.xinsteel.com.cn/portal/resource/media/XinSteelVideo.mp4?width=500&height=300&autoplay=0&definition=1");
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
        // xg
        List<ProductInfo> resultList = sendMethods.productInfoMapper.selectAllProductInfo();
        for (ProductInfo productInfo:
                resultList) {
            try {
                saveEnterpriseImageFile(productInfo.getApimaterno());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 检测报告图片
     */
    public static String saveEnterpriseImageFile(String apiMaterNo) throws Exception {

        Map<String, Object> data = new HashMap();
        data.put("apimaterno",apiMaterNo);
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        // 文件名称(图片名称，且带图片后缀，唯一性)
        data.put("filename", apiMaterNo+".jpg");
        //文件类型(1:检测报告,2:工艺流程)
        short i = 1;
        data.put("fileType", i);
        // 图片内容(传图片时为必填，将图片转为base64后再传输)

        String s = Base64EncodeUtils.imageToBase64("E:\\code\\githubProject\\epaike\\src\\main\\resources\\static\\product.png");


        data.put("imagecontent", s);
        // 是否公共附件(0：否,1:是)
        data.put("ispublic", 0);
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
//        String s = data.toString();
//        System.out.println(s);

        String url = "/v2/supplychain/saveEnterpriseImageFile";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data",data.toString());
        System.out.println(params.toString());
        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }
//        String s1 = SSLRequest.sendSSLPostRequest(ConstantPropertiesUtils.URL + url+"?access_token="+ACCESS_TOKEN, jsonObj.toString());
//        System.out.println(s1);
//        String utilsStr = utils(url, params);
//        return utilsStr;
        return null;
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

        // xg
        List id = getOrderId();
        for (int i = 0; i < id.size() ; i++) {
            saveOrderSurveyInfo(id.get(i).toString());
        }

    }
    /**
     * 订单概况信息上传接口
     */
    public static String saveOrderSurveyInfo(String orderId){
        Map<String, Object> data = new HashMap<>();

        OrderInfo orderInfo = new OrderInfo();

        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);
        data.put("erporderno", orderInfo.getErporderno());
        // 有无3D模型(0:无,1:有)
        data.put("has3dmodel", 0);


        data.put("ordercreatetime", orderInfo.getCreatetime());
        data.put("orderId", orderId);
        data.put("orderno", orderInfo.getOrderno());

        // TODO 订单所涉及的项目名称（从之前输入的地方获取）

        data.put("projectname", orderInfo.getProjectname());

        data.put("purchasecompany", orderInfo.getPurchasecompany());
        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        String url = "/v2/supplychain/saveOrderSurveyInfo";


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

        // xg
        List orderId = getOrderId();
        for (int i = 0; i < orderId.size() ; i++) {
            try {
                saveOrderContractInfo(orderId.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 合同签订概况接口
     */
    public static void saveOrderContractInfo(String orderId) throws Exception {
        Map<String, Object> data = new HashMap<>();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("orderno", orderInfo.getOrderno());
        data.put("orderId", orderId);
        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // MES系统数据
        data.put("erporderno", orderInfo.getErporderno());
        data.put("nodecode", 1);

        data.put("purchasecompany", orderInfo.getPurchasecompany());

        // 合同生效日期 假设
        /*Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(date));*/

        data.put("contractapprovaldate", orderInfo.getContractapprovaldate());

        data.put("deliverytime", orderInfo.getDeliverytime());

        data.put("filename", orderId+".jpg");

        // 获取文件中存储的图片
        String contractimagecontentAddress = orderInfo.getContractimagecontent();
        String imageToBase64 = Base64EncodeUtils.imageToBase64(contractimagecontentAddress);

        data.put("imagecontent", imageToBase64);

        String url = "/v2/supplychain/saveOrderContractInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }

//        String utilsStr = utils(url, params);
    }

    /**
     * 循环保存合同签订详情信息
     */
    public static void autoSaveContractDetail(){
        // xg
        List orderId = getOrderId();
        for (int i = 0; i < orderId.size() ; i++) {
            //
            List list = sendMethods.productInfoMapper.selectApiMaterNoListByOrderId(orderId.get(i).toString());
            for (int j = 0; j < list.size(); j++) {

                saveOrderContractDetail(orderId.get(i).toString(), list.get(j).toString(), j+1+"");
            }
        }
    }

    /**
     * 合同签订详情接口
     */
    public static void saveOrderContractDetail(String orderId, String apiMaterNo, String serialNumber){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // MES系统数据
        data.put("apimaterno", apiMaterNo);
        ProductInfo productInfo = sendMethods.productInfoMapper.selectProductInfoByApiMaterNo(apiMaterNo, orderId);
        data.put("apimatername", productInfo.getApimatername());

        data.put("productid", orderInfo.getProductid());

        // 固定值1，填写节点配置表的节点编号
        data.put("nodeCode", 1);
        data.put("quantity", orderInfo.getQualifiedquantity());

        data.put("unit", "吨");
        //序号 唯一性验证
        data.put("serialNumber", serialNumber);

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
        data.put("supplierCompanyId", ConstantPropertiesUtils.COMPANY_ID);
        //节点名称（合同签订、工程设计、采购、排产计划、生产制造过程、入库、物流6项节点雷达图必须有。编号分别是1、2、3、4、5、19、20）
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

        // xg
        List orderId = getOrderId();
        for (int i = 0; i < orderId.size(); i++) {
            int [] nodeCodes = new int[] {1,2,3,4,5,19,20};
            for (int j = 0; j < nodeCodes.length;j++) {

                saveOrderMapSchedule(orderId.get(i).toString(),100,nodeCodes[j]);
            }
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            // 初始化
            int [] nodeCodes = new int[] {1,2,3,4,5,19,20};
            for (int i = 0; i < nodeCodes.length; i++) {
                saveOrderMapSchedule(orderId,100,nodeCodes[i]);
            }
        }*/

    }


    /**
     * 雷达图各节点进度接口
     */
    public static void saveOrderMapSchedule(String orderId, Integer nodeSchedule, Integer nodeCode){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchaseCompanyId", orderInfo.getPurchasecompanyid());
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


        data.put("supplierCompanyId", ConstantPropertiesUtils.COMPANY_ID);
        // 填写节点配置表的节点编号,确定唯一性
        data.put("nodeCode", "5");
        // 图片名称，且带图片后缀
        data.put("filename", "XinSteelVideo.mp4");
        data.put("fileurl", "https://www.xinsteel.com.cn/portal/resource/media/XinSteelVideo.mp4");



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

        // xg
        List orderId = getOrderId();
        for (int i = 0; i < orderId.size() ; i++) {
            try {
                saveOrderEngineeringDesign(orderId.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 工程设计信息上传接口
     */
    public static void saveOrderEngineeringDesign(String orderId) throws Exception {

        Map<String, Object> data = new HashMap<>();


        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());

        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 固定值位2，填写节点配置表的节点编号
        data.put("nodeCode", 2);

        data.put("designname",orderId);
        data.put("designleader", "李经理");

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(formatter.format(date));

        data.put("checkcompletetime", orderInfo.getPlanbegintime());



        // 图片名称，且带图片后缀
        data.put("filename", orderId+".jpg");
        // 设计文档 图片，Base64编码
        String imageToBase64 = Base64EncodeUtils.imageToBase64("E:\\code\\githubProject\\epaike\\src\\main\\resources\\static\\design.jpg");
        data.put("designimage", imageToBase64);



        String url = "/v2/supplychain/saveOrderEngineeringDesign";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }

//        String utilsStr = utils(url, params);
    }



    /**
     * 循环保存原材料采购概况信息
     */
    public static void autoSaveMaterialSurvey(){
        // xg

        List orderId = getOrderId();
        for (int i = 0; i < orderId.size(); i++) {
            saveOrderMaterialSurvey(orderId.get(i).toString());
        }
    }

    /**
     * 原材料采购概况
     */
    public static void saveOrderMaterialSurvey(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        // 设计某个值
        data.put("nodecode",3);

        data.put("erporderno", orderInfo.getErporderno());

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(formatter.format(date));

        //原材料到齐日期(yyyy-MM-dd HH:mm:ss格式)
        data.put("rawmaterialtime", orderInfo.getRawmaterialtime());
        //物料品类数(物料明细总数量)

        // TODO 原料信息
        data.put("materialquantity", 7);

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

        List orderId = getOrderId();
        for (int i = 0; i < orderId.size(); i++) {
            List list = sendMethods.materiaMapper.selectAllMaterialNo();
            for (int j = 0; j < list.size(); j++) {

                try {
                    saveOrderMaterialDetail(orderId.get(i).toString(), list.get(j).toString(), j+1+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
       /* for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderMaterialDetail(orderId);
        }*/
    }

    /**
     * 原材料采购详情
     */
    public static void saveOrderMaterialDetail(String orderId, String materialNo, String serialNumber) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);


        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        //序号
        data.put("serialNumber", serialNumber);
        // 设计某个值
        data.put("nodecode",3);
        // 企业物料编码
        data.put("materialNo",materialNo);
        Materia materia = sendMethods.materiaMapper.selectMaterialByMaterialNo(materialNo);
        // 原材料名称
        data.put("materialName",materia.getMaterialname());


        // 原材料使用数量
        data.put("rawMaterialOfUse",materia.getRawmaterialofuse());

        String imageToBase64 = Base64EncodeUtils.imageToBase64("E:\\code\\githubProject\\epaike\\src\\main\\resources\\static\\materialCheck.jpg");
        // 原材料合格证书文件名称(图片名称，且带图片后缀)
        data.put("certificateFileName",orderId+"certificate.jpg");
        // 原材料合格证书（图片）(Base64编码) (非必填)
        data.put("materialCertificate",imageToBase64);
        // 原材料出厂报文件名称(图片名称，且带图片后缀
        data.put("reportFileName",orderId+"report.jpg");
        //原材料出厂报告（图片）非必填（Base64编码）
        data.put("materialReport",imageToBase64);
        // 原材料供应商名称
        data.put("supplierCompanyName",materia.getSuppliercompanyname());
        // 规格型号
        data.put("specifications",materia.getSpecifications());

        String url = "/v2/supplychain/saveOrderMaterialDetail";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());
        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }

//        String utilsStr = utils(url, params);
    }


    /**
     * 保存排产计划信息上传接口
     */
    public static void autoSaveProductionSchedulingInfo(){

        // xg
        List orderIds = getOrderId();
        for (int i = 0; i < orderIds.size(); i++) {
            List list = sendMethods.productInfoMapper.selectApiMaterNoListByOrderId(orderIds.get(i).toString());
            for (int j = 0; j < list.size() ; j++) {
                saveOrderProductionSchedulingInfo(orderIds.get(i).toString(), list.get(j).toString());
            }
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderProductionSchedulingInfo(orderId);
        }*/
    }

    /**
     * 排产计划信息上传接口
     */
    public static void saveOrderProductionSchedulingInfo(String orderId, String apiMaterNo){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);
        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);



        data.put("nodecode", 4);
        ProductInfo productInfo = sendMethods.productInfoMapper.selectProductInfoByApiMaterNo(apiMaterNo, orderId);
        data.put("apimatername",productInfo.getApimatername());
        data.put("apimaterno", apiMaterNo);

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 计划开始时间
        data.put("planbegintime", orderInfo.getPlanbegintime());
        // 计划结束时间
        data.put("planendtime", orderInfo.getPlanendtime());

        // 固定名称
        data.put("leadername","李经理");

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

        List orderId = getOrderId();
        for (int i = 0; i < orderId.size() ; i++) {
            saveOrderManufacturingProcess(orderId.get(i).toString());
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderManufacturingProcess(orderId);
        }*/
    }

    /**
     * 生产制造过程概况接口
     */
    public static void saveOrderManufacturingProcess(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        data.put("orderno", orderInfo.getOrderno());

        data.put("erpOrderNo",orderInfo.getErporderno());


//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 计划开始时间
        data.put("plannedstarttime", orderInfo.getPlannedstarttime());
        data.put("plannedEndTime", orderInfo.getPlannedendtime());
        data.put("actualStartTime", orderInfo.getActualstarttime());
        data.put("actualEndTime", orderInfo.getActualendtime());
        data.put("nodeCode", 5);





        String url = "/v2/supplychain/saveOrderManufacturingProcess";

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

        List orderId = getOrderId();
        for (int i = 0; i <  orderId.size(); i++) {
            try {
                saveOrderManufacturingDetail(orderId.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       /* for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderManufacturingDetail(orderId);
        }*/
    }


    /**
     * 生产制造过程详情接口
     */
    public static void saveOrderManufacturingDetail(String orderId) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 检验类型 (唯一性验证)
        data.put("inspectionType",orderInfo.getInspectiontype());
        // 检验数量
        data.put("inspectionQuantity",orderInfo.getInspectionquantity());
        // 合格数量
        data.put("quantity",orderInfo.getQualifiedquantity());
        // 检验合格率
        data.put("qualifiedRate",orderInfo.getQualifiedrate());
        // 节点编码(固定值 填写节点配置表的节点编号，唯一性验证)
        data.put("nodeCode",5);
        // 文件名称（图片名称，且带图片后缀）
        data.put("fileName",orderId+"nodeCheck.jpg");

        String nodecheckrecordAddress = orderInfo.getNodecheckrecord();
        String imageToBase64 = Base64EncodeUtils.imageToBase64(nodecheckrecordAddress);

        // 节点检验记录（图片Base64）
        data.put("nodeCheckRecord", imageToBase64);


        String url = "/v2/supplychain/saveOrderManufacturingDetail";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());
        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }

//        String utilsStr = utils(url, params);
    }


    /**
     * 入库信息上传接口
     */
    public static void autoSaveOrderInputInfo(){

        List orderId = getOrderId();
        for (int i = 0; i < orderId.size(); i++) {
            List list = sendMethods.productInfoMapper.selectApiMaterNoListByOrderId(orderId.get(i).toString());
            for (int j = 0; j < list.size() ; j++) {
                try {
                    saveOrderInputInfo(orderId.get(i).toString(), list.get(j).toString(), j+1+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderInputInfo(orderId);
        }*/
    }

    /**
     * 入库信息上传接口
     * @param orderId 订单id
     */
    public static void saveOrderInputInfo(String orderId, String apiMaterNo, String batch) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        data.put("apimaterNo", apiMaterNo);

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 入库时间
        data.put("belaidupStartTime", orderInfo.getBelaidupstarttime());
        // 文件名称
        String nodecheckrecordAddress = orderInfo.getNodecheckrecord();
        String imageToBase64 = Base64EncodeUtils.imageToBase64(nodecheckrecordAddress);

        data.put("fileName",batch+orderId+"inspection.jpg");
        // 产品出厂检验报告（图片Base64）
        data.put("inspectionReport",imageToBase64);
        // 固定的节点编码
        data.put("nodeCode",19);
        // 批次
        data.put("batch", batch);
        // 验收时间
        data.put("acceptanceTime", orderInfo.getAcceptancetime());
        ProductInfo productInfo = sendMethods.productInfoMapper.selectProductInfoByApiMaterNo(apiMaterNo, orderId);
        // 检验数据
        data.put("inspectionData",productInfo.getProductquantity());
        // 合格数量
        data.put("qualifiedQuantity",productInfo.getProductquantity());
        // 合格率
        data.put("qualifiedRate",100);






        String url = "/v2/supplychain/saveOrderInputInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }

//        String utilsStr = utils(url, params);
    }

    /**
     * 发货信息上传接口
     */
    public static void autoSaveShipmentImageFile(){
        List orderId = getOrderId();
        for (int i = 0; i <  orderId.size(); i++) {
            try {
                saveShipmentImageFile(orderId.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveShipmentImageFile(orderId);
        }*/
    }


    /**
     * 发货信息上传接口
     * @param orderId
     */
    public static void saveShipmentImageFile(String orderId) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // 物流方式
        data.put("logisticsType",orderInfo.getLogisticstype());
        // 承运人
        data.put("carrier","李经理");
        // 运单号
        data.put("billNo", orderInfo.getBillno());
        // 文件名称（图片名称，且带图片后缀）
        data.put("fileName",orderId+"invoice.jpg");

        String imageToBase64 = Base64EncodeUtils.imageToBase64("E:\\code\\githubProject\\epaike\\src\\main\\resources\\static\\materialCheck.jpg");
        // 发货单（图片,Base64编码）
        data.put("invoiceNo",imageToBase64);
        // 节点编码（固定值）
        data.put("nodeCode",20);



        String url = "/v2/supplychain/saveShipmentImageFile";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }

//        String utilsStr = utils(url, params);
    }

    /**
     * 订单产品展示信息上传接口
     */
    public static void autoSaveOrderProductInfoNo3d(){
        List orderId = getOrderId();
        for (int i = 0; i < orderId.size(); i++) {
            List productSkuId = sendMethods.productInfoMapper.selectProductSkuIdByOrderId(orderId.get(i).toString());
            for (int j = 0; j < productSkuId.size(); j++) {
                List materialNoList = sendMethods.materiaMapper.selectAllMaterialNo();
                for (int k = 0; k < materialNoList.size() ; k++) {

                    try {
                        saveOrderProductInfoNo3d(orderId.get(i).toString(), productSkuId.get(j).toString(), materialNoList.get(k).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderProductInfoNo3d(orderId);
        }*/
    }


    /**
     * 订单产品展示信息上传接口
     * @param orderId
     */
    public static void saveOrderProductInfoNo3d(String orderId, String productSkuId, String materialNo) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);

        // Search the orderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);



        data.put("productSkuId", productSkuId);
        data.put("productName", orderInfo.getProductname());

        // 文件名称(图片名称，且带图片后缀)
        data.put("fileName","one.jpg");

        String s = Base64EncodeUtils.imageToBase64("E:\\code\\githubProject\\epaike\\src\\main\\resources\\static\\product.png");
        // 商品展示图片
        data.put("imageUrl",s);

        // TODO 商品原材料编码（企业）
        data.put("materialNo",materialNo);
        Materia materia = sendMethods.materiaMapper.selectMaterialByMaterialNo(materialNo);
        // 商品原材料名称
        data.put("materialName", materia.getMaterialname());
        // 原材料用量
        data.put("quantity", materia.getMaterialquantity());
        // 原材料单位
        data.put("materialUnit",materia.getMaterialunit());

        // 原材料供应商名称
        data.put("supplierCompanyName", materia.getSuppliercompanyname());

        String url = "/v2/supplychain/saveOrderProductInfoNo3d";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        EpecResult result= EpecApiUtil.invokeAPI(ConstantPropertiesUtils.URL+url, ACCESS_TOKEN,jsonObj.toString());
        if(result.isSuccess()){
            System.out.println(JSON.toJSON(result));
        }
//        String utilsStr = utils(url, params);
    }


    /**
     * 物流跟踪信息上传接口
     */
    public static void autoSaveOrderLogisticsInfo(){
        List orderId = getOrderId();



        for (int i = 0; i <  orderId.size(); i++) {
            saveOrderLogisticsInfo(orderId.get(i).toString());
        }

        List<Map<String, String>> infoList = new ArrayList<>();
        Map<String, String> map = new HashMap();
        map.put("eventTime","2018-11-15 00:00:00");
        map.put("eventPlace","祁集镇煤化工大道 中安联合煤化有限公司");
        infoList.add(map);

        Map<String, String> map2 = new HashMap();
        map2.put("eventTime","2019-06-14 00:00:00");
        map2.put("eventPlace","喀什中石化油库：新疆喀什市世纪大道30号（中石化油库）");
        infoList.add(map2);

        for (int j = 0; j <  orderId.size(); j++) {
            saveOrderLogisticsInfo2(orderId.get(j).toString(),infoList.get(j).get("eventTime"),infoList.get(j).get("eventPlace"));
        }
        /*for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderLogisticsInfo(orderId);
        }*/
    }
    /**
     * 物流跟踪信息上传接口
     * @param orderId
     */

    public static void saveOrderLogisticsInfo(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        OrderInfo orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 阶段时间
        data.put("eventTime", orderInfo.getBelaidupstarttime());
        // 阶段地点
        data.put("eventPlace","新钢");
        // TODO 物流单号
        data.put("billNo",orderInfo.getBillno());
        // 运输方式
        data.put("tansportType",orderInfo.getTransporttype());
        data.put("eventNumber",1);



        String url = "/v2/supplychain/saveOrderLogisticsInfo";

        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data", jsonObj.toString());

        String utilsStr = utils(url, params);
    }

    public static void saveOrderLogisticsInfo2(String orderId,String eventTime, String eventPlace){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        OrderInfo orderInfo = sendMethods.orderInfoMapper.selectByPrimaryKey(orderId);

        data.put("purchasecompanyid", orderInfo.getPurchasecompanyid());
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 阶段时间
        data.put("eventTime", eventTime);
        // 阶段地点
        data.put("eventPlace", eventPlace);
        // TODO 物流单号
        data.put("billNo",orderInfo.getBillno());
        // 运输方式
        data.put("tansportType",orderInfo.getTransporttype());
        data.put("eventNumber",2);



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
