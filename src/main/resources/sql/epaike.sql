/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : epaike

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 09/12/2020 15:29:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for materia
-- ----------------------------
DROP TABLE IF EXISTS `materia`;
CREATE TABLE `materia`  (
  `materialNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '原材料编码',
  `materialName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '原材料名称',
  `supplierCompanyName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '原材料供应商名称',
  `quantity` decimal(20, 0) NULL DEFAULT NULL COMMENT '采购数量',
  `materialUnit` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '原材料计量单位',
  `materialQuantity` decimal(20, 0) NULL DEFAULT NULL COMMENT '原材料仓储数量',
  `rawMaterialOfUse` decimal(50, 0) NULL DEFAULT NULL COMMENT '原材料使用数量',
  `specifications` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '原材料规格型号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of materia
-- ----------------------------
INSERT INTO `materia` VALUES ('1', '洗精煤', '山东能源集团煤炭营销有限公司', 3400000, '吨/年', 200000, 290000, '0-50mm');
INSERT INTO `materia` VALUES ('2', '高炉用喷吹煤', '安徽恒源煤电股份有限公司销售分公司', 1500000, '吨/年', 40000, 140000, '0-25mm');
INSERT INTO `materia` VALUES ('3', '焦炭', '江西煤炭储备中心有限公司', 1700000, '吨/年', 60000, 150000, '>25mm');
INSERT INTO `materia` VALUES ('4', '烧结煤', '新余市恒久贸易有限公司', 350000, '吨/年', 15000, 35000, '0-5mm');
INSERT INTO `materia` VALUES ('5', '合金', '鄂尔多斯市西京矿冶有限责任公司', 180000, '吨/年', 15000, 15000, '10-50mm');
INSERT INTO `materia` VALUES ('6', '废钢', '新余市再生资源有限公司', 1320000, '吨/年', 110000, 110000, '600*800*800mm');
INSERT INTO `materia` VALUES ('7', '溶剂', '江西天寅矿业有限公司', 1080000, '吨/年', 90000, 90000, '0-3mm');

-- ----------------------------
-- Table structure for nodeinfo
-- ----------------------------
DROP TABLE IF EXISTS `nodeinfo`;
CREATE TABLE `nodeinfo`  (
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `node_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `node_code` int NULL DEFAULT NULL,
  `file_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of nodeinfo
-- ----------------------------

-- ----------------------------
-- Table structure for orderinfo
-- ----------------------------
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo`  (
  `orderId` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '主键，中石化订单id',
  `orderNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '易派客订单编号',
  `purchaseCompanyId` bigint NULL DEFAULT NULL COMMENT '采购商id',
  `purchaseCompany` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '采购商名称',
  `productId` bigint NULL DEFAULT NULL COMMENT '商品id',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `erpOrderNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '我们自己的订单id',
  `createtime` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '下单时间',
  `projectNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '项目编号',
  `projectName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '项目名称',
  `projectSchedule` int NULL DEFAULT NULL COMMENT '项目进度',
  `productionState` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '生产状态，未到排产日期为未生产，到日期改为生产中，全部交货后改为生产完成',
  `productionName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '生产状态，未到排产日期为未生产状态名称，到日期改为生产中，全部交货后改为生产完成',
  `videoFileName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '视频文件名称',
  `videoFileUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '视频地址',
  `contractApprovalDate` datetime(0) NULL DEFAULT NULL COMMENT '合同审批生效日期yyyy-MM-dd HH:mm:ss格式',
  `deliveryTime` datetime(0) NULL DEFAULT NULL COMMENT '交货日期',
  `contractFileName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '合同文件名称',
  `contractImageContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '图片内容Base64编码',
  `rawMaterialTime` datetime(0) NULL DEFAULT NULL COMMENT '原材料到齐日期',
  `planBeginTime` datetime(0) NULL DEFAULT NULL COMMENT '排产计划开始时间',
  `planEndTime` datetime(0) NULL DEFAULT NULL COMMENT '排产计划结束时间',
  `plannedStartTime` datetime(0) NULL DEFAULT NULL COMMENT '计划开始时间',
  `plannedEndTime` datetime(0) NULL DEFAULT NULL COMMENT '计划结束时间',
  `actualStartTime` datetime(0) NULL DEFAULT NULL COMMENT '实际开始时间',
  `actualEndTime` datetime(0) NULL DEFAULT NULL COMMENT '实际完成时间',
  `inspectionType` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '检验类型',
  `inspectionQuantity` decimal(20, 0) NULL DEFAULT NULL COMMENT '检验数量',
  `qualifiedQuantity` decimal(20, 0) NULL DEFAULT NULL COMMENT '合格数量',
  `qualifiedRate` decimal(20, 0) NULL DEFAULT NULL COMMENT '检验合格率',
  `nodeCheckFileName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '节点检验记录文件名称',
  `nodeCheckRecord` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '节点检验记录（图片）图片Base64',
  `belaidupStartTime` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  `inspectionReportFileName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品出厂报告名称',
  `inspectionReport` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '产品出厂检验报告（图片）图片Base64',
  `batch` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '批次',
  `acceptanceTime` datetime(0) NULL DEFAULT NULL COMMENT '验收时间',
  `inspectionData` decimal(20, 0) NULL DEFAULT NULL COMMENT '检验数据',
  `storageQualifiedQuantity` decimal(20, 0) NULL DEFAULT NULL COMMENT '入库合格数量',
  `storageQualifiedRate` decimal(20, 0) NULL DEFAULT NULL COMMENT '入库合格率',
  `logisticsType` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '物流方式',
  `billNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '运单号',
  `transportType` varchar(6) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '运输方式',
  `invoiceFileName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '发货文件名称',
  `invoiceNo` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '发货单图片,Base64编码',
  PRIMARY KEY (`orderId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orderinfo
-- ----------------------------
INSERT INTO `orderinfo` VALUES ('4919891', '20181018000004560682', 100636, '中安联合煤化有限责任公司', 7158627, '热轧碳素结构钢板·Q235B·生产商', 'W112018100908', '2018-10-18 08:59:43', '1', '201810180859', 100, '已生产', '已生产', NULL, NULL, '2018-10-23 00:00:00', '2018-11-15 00:00:00', '2', 'E:\\\\code\\\\githubProject\\\\epaike\\\\src\\\\main\\\\resources\\\\static\\\\order18.jpg', '2018-10-23 00:00:00', '2018-10-23 00:00:00', '2018-12-23 00:00:00', '2018-10-23 00:00:00', '2018-12-23 00:00:00', '2018-10-23 00:00:00', '2018-12-23 00:00:00', '全检', 311, 311, 100, NULL, 'E:\\\\code\\\\githubProject\\\\epaike\\\\src\\\\main\\\\resources\\\\static\\\\checkFile18.jpg', '2018-10-23 00:00:00', NULL, NULL, NULL, '2018-10-23 00:00:00', 311, 311, 100, '汽车', '20181023', '2', NULL, NULL);
INSERT INTO `orderinfo` VALUES ('5442427', '20190428000004771487', 100519, '中国石化销售股份有限公司新疆石油分公司', 7158627, '热轧碳素结构钢板·Q235B·生产商', 'W112019050904', '2019-04-28 16:04:57', '2', '201904281604', 100, '已生产', '已生产', NULL, NULL, '2019-05-14 00:00:00', '2019-06-14 00:00:00', '1', 'E:\\\\code\\\\githubProject\\\\epaike\\\\src\\\\main\\\\resources\\\\static\\\\order19.jpg', '2019-05-14 00:00:00', '2019-05-14 00:00:00', '2019-07-14 00:00:00', '2019-05-14 00:00:00', '2019-07-14 00:00:00', '2019-05-14 00:00:00', '2019-07-14 00:00:00', '全检', 580, 580, 100, '', 'E:\\\\code\\\\githubProject\\\\epaike\\\\src\\\\main\\\\resources\\\\static\\\\checkFile19.jpg', '2019-05-14 00:00:00', NULL, NULL, NULL, '2019-05-14 00:00:00', 580, 580, 100, '火车', '20190514', '4', NULL, NULL);

-- ----------------------------
-- Table structure for productinfo
-- ----------------------------
DROP TABLE IF EXISTS `productinfo`;
CREATE TABLE `productinfo`  (
  `apimaterNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '存货编码',
  `apimaterName` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '存货名称',
  `productSkuId` bigint NULL DEFAULT NULL COMMENT '易派客单品id',
  `orderId` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '中石化订单id',
  `productQuantity` decimal(20, 0) NULL DEFAULT NULL COMMENT '产品仓储数量',
  `productFileName` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '上传商品文件名称',
  `productImageUrl` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '商品展示'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of productinfo
-- ----------------------------
INSERT INTO `productinfo` VALUES ('510100120500\n', '热轧碳素结构钢板\\δ=5mm Q235B GB/T3274', 61261458, '5442427', 71, NULL, '');
INSERT INTO `productinfo` VALUES ('510100120600\n', '热轧碳素结构钢板\\δ=6mm Q235B GB/T3274', 61261461, '5442427', 102, NULL, NULL);
INSERT INTO `productinfo` VALUES ('510100120800\n', '热轧碳素结构钢板\\δ=8mm Q235B GB/T3274', 61261464, '5442427', 188, NULL, NULL);
INSERT INTO `productinfo` VALUES ('510100121000\n', '热轧碳素结构钢板\\δ=10mm Q235B GB/T3274', 61261466, '5442427', 63, NULL, NULL);
INSERT INTO `productinfo` VALUES ('510100121200\n', '热轧碳素结构钢板\\δ=12mm Q235B GB/T3274', 61261468, '5442427', 109, NULL, NULL);
INSERT INTO `productinfo` VALUES ('510100121400', '热轧碳素结构钢板\\δ=14mm Q235B GB/T3274', 61261471, '5442427', 48, NULL, NULL);
INSERT INTO `productinfo` VALUES ('510100121200', '热轧碳素结构钢板\\δ=12mm Q235B GB/T3274', 61261468, '4919891', 312, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
