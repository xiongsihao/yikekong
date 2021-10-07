/*
 Navicat Premium Data Transfer

 Source Server         : 亿可控
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 192.168.152.130:3306
 Source Schema         : ykk

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 07/10/2021 17:35:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_admin
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin`;
CREATE TABLE `tb_admin`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录名',
  `password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '类型 1超级管理员 0普通用户',
  `board` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '看板',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_alarm
-- ----------------------------
DROP TABLE IF EXISTS `tb_alarm`;
CREATE TABLE `tb_alarm`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报警名称',
  `quota_id` int(0) NULL DEFAULT NULL COMMENT '指标id',
  `operator` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '运算符',
  `threshold` int(0) NULL DEFAULT NULL COMMENT '报警阈值',
  `level` int(0) NULL DEFAULT NULL COMMENT '报警级别  1一般 2严重',
  `cycle` int(0) NULL DEFAULT NULL COMMENT '沉默周期（分钟）',
  `webhook` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'web钩子',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tb_alarm_name_uindex`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_board
-- ----------------------------
DROP TABLE IF EXISTS `tb_board`;
CREATE TABLE `tb_board`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `admin_id` int(0) NULL DEFAULT 1 COMMENT '管理员id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '看板名称',
  `quota` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '指标(趋势时设置)',
  `device` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备(累计)',
  `system` tinyint(0) NULL DEFAULT 0 COMMENT '是否是系统看板',
  `disable` tinyint(0) NULL DEFAULT 0 COMMENT '是否不显示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tb_board_name_uindex`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_gps
-- ----------------------------
DROP TABLE IF EXISTS `tb_gps`;
CREATE TABLE `tb_gps`  (
  `id` bigint(0) NOT NULL COMMENT 'id',
  `subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主题',
  `sn_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备识别码字段',
  `single_field` tinyint(0) NULL DEFAULT NULL COMMENT '类型（单字段、双字段）',
  `value_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经纬度字段',
  `separation` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经纬度分隔符',
  `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经度字段',
  `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '维度字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tb_gps_subject_uindex`(`subject`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_quota
-- ----------------------------
DROP TABLE IF EXISTS `tb_quota`;
CREATE TABLE `tb_quota`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指标名称',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指标单位',
  `subject` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '报文主题',
  `value_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指标值字段',
  `sn_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备识别码字段',
  `webhook` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'web钩子',
  `value_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '指标字段类型，Double、Inteter、Boolean',
  `reference_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参考值',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tb_quota_name_uindex`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
