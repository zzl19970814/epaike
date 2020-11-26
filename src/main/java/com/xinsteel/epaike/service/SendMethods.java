package com.xinsteel.epaike.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pcitc.apiapplication.service.api.dto.EpecResult;
import com.pcitc.apiapplication.service.api.ssl.EpecApiUtil;
import com.xinsteel.epaike.dao.OrderInfoMapper;
import com.xinsteel.epaike.pojo.OrderInfo;
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
    private static List<String> API_MATER_NO_LIST = new ArrayList<>();
//  private static Logger LOGGER = (Logger) LoggerFactory.getLogger(SendMethods.class);

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    private static SendMethods codeMapUtils;

    @PostConstruct
    public void init() {
        codeMapUtils = this;
        codeMapUtils.orderInfoMapper = this.orderInfoMapper;
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

        OrderInfo orderInfo = new OrderInfo();

        orderInfo = codeMapUtils.orderInfoMapper.selectByPrimaryKey(orderId);

        String erpOrderNo = orderInfo.getErporderno();

        data.put("erpOrderNo", erpOrderNo);

        // 添加到Message_List中 后面的接口要用
        Map<String, String> map = new HashMap<>();
        getYiPaiKeOrderInfo(orderId).put("erpOrderNo", erpOrderNo);
        System.out.println(Message_List.toString());

        data.put("orderId", orderId);
//        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
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
        data.put("defaultProduct",1);

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

        data.put("productionname", "已完成");
        data.put("productionstate", "已完成");

        // String projectname = System.currentTimeMillis()+"";
        String projectname = "one";
        data.put("projectname", projectname);

        // 后面的接口要用
        Map<String, String> map = new HashMap<>();
        getYiPaiKeOrderInfo(orderId).put("projectName", projectname);
        System.out.println(Message_List.toString());

//        data.put("projectno", System.currentTimeMillis()+"");
        data.put("projectno", "1");
        data.put("projectschedule", 100);
        // 生产进度延期状态
        data.put("projectschedulestate", 0);

        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
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
        data.put("materialcompose", "暂无");
        // 原材料名称(钢材)
        data.put("materialname", "煤炭");
        // 原材料编码(1002A001)
        data.put("materialno", "0001");
        // 原材料计量单位
        data.put("materialunit", "吨");
        // 原材料采购数量
        data.put("quantity", 100);

        data.put("supplierCompanyName", "中石化");




        String url = "/v2/supplychain/saveEnterpriseMaterial";
        JSONObject jsonObj=new JSONObject(data);
        System.out.println(jsonObj.toString());

        Map<String, String> params=new HashMap<>();
        params.put("access_token",ACCESS_TOKEN);
        params.put("data","[" + jsonObj.toString() + "]");

        String utilsStr = utils(url, params);
        return utilsStr;
    }

    public static String saveEnterpriseMateriaPlace(){
        Map<String, Object> data = new HashMap<>();
        data.put("corpcode", ConstantPropertiesUtils.CLIENT_ID);
        data.put("materialno","0001");
        data.put("materialPlace","新钢");
        data.put("materialQuantity",200);
        data.put("materialUnit","吨");

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
        data.put("filename", "one.mp4");
        // 文件类型(0:视频)
        data.put("filetype", 0);
        data.put("fileurl", "https://www.xinsteel.com.cn/portal/resource/media/XinSteelVideo.mp4");
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
        data.put("filename", "one.jpg");
        //文件类型(1:检测报告,2:工艺流程)
        short i = 1;
        data.put("fileType", i);
        // 图片内容(传图片时为必填，将图片转为base64后再传输)
        data.put("imagecontent", "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");
        // 是否公共附件(0：否,1:是)
        data.put("ispublic", 0);
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        String s = data.toString();
        System.out.println(s);

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(date));

        data.put("contractapprovaldate", formatter.format(date));

        data.put("deliverytime", formatter.format(date));

        data.put("filename", "one.jpg");
        data.put("imagecontent", "imagecodata:image/png;base64,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");

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
        data.put("quantity", 200);

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

        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            // 初始化
            int [] nodeCodes = new int[] {1,2,3,4,5,19,20};
            for (int i = 0; i < nodeCodes.length; i++) {
                saveOrderMapSchedule(orderId,100,nodeCodes[i]);
            }
        }

    }


    /**
     * 雷达图各节点进度接口
     */
    public static void saveOrderMapSchedule(String orderId, Integer nodeSchedule, Integer nodeCode){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("purchaseCompanyId", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
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
        data.put("filename", "one.mp4");
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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 固定值位2，填写节点配置表的节点编号
        data.put("nodeCode", 2);

        data.put("designname","one");
        data.put("designleader", "范经理");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(formatter.format(date));

        data.put("checkcompletetime", formatter.format(date));



        // 图片名称，且带图片后缀
        data.put("filename", "one.jpg");
        // 设计文档 图片，Base64编码
        data.put("designimage", "data:image/jpeg;base64,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");



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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);


        // 设计某个值
        data.put("nodecode",3);

        data.put("erporderno", getYiPaiKeOrderInfo(orderId).get("erpOrderNo"));

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(formatter.format(date));

        //原材料到齐日期(yyyy-MM-dd HH:mm:ss格式)
        data.put("rawmaterialtime", formatter.format(date));
        //物料品类数(物料明细总数量)
        data.put("materialquantity", 200);

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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        //序号
        data.put("serialNumber", 1);
        // 设计某个值
        data.put("nodecode",3);
        // 原材料名称
        data.put("materialName","煤炭");
        // 企业物料编码
        data.put("materialNo","0001");
        // 原材料使用数量
        data.put("rawMaterialOfUse",200);
        // 原材料合格证书文件名称(图片名称，且带图片后缀)
        data.put("certificateFileName","one.jpg");
        // 原材料合格证书（图片）(Base64编码) (非必填)
        data.put("materialCertificate","data:image/jpeg;base64,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");
        // 原材料出厂报文件名称(图片名称，且带图片后缀
        data.put("reportFileName","two.jpg");
        //原材料出厂报告（图片）非必填（Base64编码）
        data.put("materialReport","data:image/jpeg;base64,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");
        // 原材料供应商名称
        data.put("supplierCompanyName","中石化");
        // 规格型号
        data.put("specifications","0-25mm");

        String url = "/v2/supplychain/saveOrderMaterialDetail";

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 计划开始时间
        data.put("planbegintime",formatter.format(date));
        // 计划结束时间
        data.put("planendtime",formatter.format(date));

        // 固定名称
        data.put("leadername","范经理");

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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        data.put("orderno", getYiPaiKeOrderInfo(orderId).get("orderno"));

        data.put("erpOrderNo",getYiPaiKeOrderInfo(orderId).get("erpOrderNo"));


        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 计划开始时间
        data.put("plannedstarttime", formatter.format(date));
        data.put("plannedEndTime", formatter.format(date));
        data.put("actualStartTime", formatter.format(date));
        data.put("actualEndTime", formatter.format(date));
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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);
        // 检验类型 (唯一性验证)
        data.put("inspectionType","全检");
        // 检验数量
        data.put("inspectionQuantity",100);
        // 合格数量
        data.put("quantity",100);
        // 检验合格率
        data.put("qualifiedRate",100);
        // 节点编码(固定值 填写节点配置表的节点编号，唯一性验证)
        data.put("nodeCode",5);
        // 文件名称（图片名称，且带图片后缀）
        data.put("fileName","one.jpg");
        // 节点检验记录（图片Base64）
        data.put("nodeCheckRecord","data:image/jpeg;base64,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");


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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // TODO 把orderId对应 apimaterno
        data.put("apimaterNo", "2020");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 入库时间
        data.put("belaidupStartTime",formatter.format(date));
        // 文件名称
        data.put("fileName","one.jpg");
        // 产品出厂检验报告（图片Base64）
        data.put("inspectionReport","data:image/jpeg;base64,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");
        // 固定的节点编码
        data.put("nodeCode",19);
        // 批次
        data.put("batch","1");
        // 验收时间
        data.put("acceptanceTime",formatter.format(date));
        // 检验数据
        data.put("inspectionData",100);
        // 合格数量
        data.put("qualifiedQuantity",100);
        // 合格率
        data.put("qualifiedRate",100);






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
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // 物流方式
        data.put("logisticsType","火车");
        // 承运人
        data.put("carrier","范经理");
        // 运单号
        data.put("billNo","0001");
        // 文件名称（图片名称，且带图片后缀）
        data.put("fileName","one.jpg");
        // 发货单（图片,Base64编码）
        data.put("invoiceNo","data:image/jpeg;base64,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");
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
     */
    public static void autoSaveOrderProductInfoNo3d(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderProductInfoNo3d(orderId);
        }
    }


    /**
     * 订单产品展示信息上传接口
     * @param orderId
     */
    public static void saveOrderProductInfoNo3d(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        data.put("productSkuId", getYiPaiKeOrderInfo(orderId).get("productskuid"));
        data.put("productName",getYiPaiKeOrderInfo(orderId).get("productname"));

        // 文件名称(图片名称，且带图片后缀)
        data.put("fileName","one.jpg");
        // 商品展示图片
        data.put("imageUrl","https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");

        // TODO 商品原材料编码（企业）
        data.put("materialNo","0001");
        // 商品原材料名称
        data.put("materialName","煤炭");
        // 原材料用量
        data.put("quantity",200);
        // 原材料单位
        data.put("materialUnit","吨");

        // 原材料供应商名称
        data.put("supplierCompanyName","中石化");






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
     */
    public static void autoSaveOrderLogisticsInfo(){
        for (Map<String, String> orderInfo : Message_List
        ) {
            String orderId = orderInfo.get("orderId");
            saveOrderLogisticsInfo(orderId);
        }
    }
    /**
     * 物流跟踪信息上传接口
     * @param orderId
     */

    public static void saveOrderLogisticsInfo(String orderId){
        Map<String, Object> data = new HashMap<>();
        data.put("orderId",orderId);
        data.put("purchasecompanyid", getYiPaiKeOrderInfo(orderId).get("purchasecompanyid"));
        data.put("suppliercompanyid", ConstantPropertiesUtils.COMPANY_ID);

        // 阶段编号
        data.put("eventNumber",1);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 阶段时间
        data.put("eventTime",formatter.format(date));
        // 阶段地点
        data.put("eventPlace","新钢");
        // TODO 物流单号
        data.put("billNo","0001");
        // 运输方式
        data.put("tansportType","4");



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
