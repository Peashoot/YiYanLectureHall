/*
 Navicat Premium Data Transfer

 Source Server         : Mine ( Mysql )
 Source Server Type    : MySQL
 Source Server Version : 50624
 Source Host           : localhost:3306
 Source Schema         : peablog

 Target Server Type    : MySQL
 Target Server Version : 50624
 File Encoding         : 65001

 Date: 25/03/2020 17:40:58
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bg_sysuser
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bg_sysuser`
(
    `id`                     int(11)       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `roleIds`                varchar(255)  NOT NULL COMMENT '角色类型id（不同角色id之间用\',\'隔开）',
    `username`               varchar(30)   NOT NULL COMMENT '用户名',
    `password`               varchar(100)  NOT NULL COMMENT '加密后的密码',
    `salt`                   varchar(32)   NOT NULL COMMENT '加密盐密钥',
    `nickName`               varchar(30)   NOT NULL COMMENT '昵称',
    `email`                  varchar(255)  NOT NULL COMMENT '邮箱',
    `contact`                varchar(20)   NULL DEFAULT NULL COMMENT '联系方式',
    `emsorqq`                varchar(20)   NULL DEFAULT NULL COMMENT 'EMS或者QQ',
    `lastLoginTime`          timestamp(0)  NOT NULL COMMENT '上一次登录时间',
    `registerTime`           timestamp(0)  NOT NULL COMMENT '用户注册时间',
    `lastLoginIP`            varchar(100)  NULL DEFAULT NULL COMMENT '上一次登录IP',
    `registerIP`             varchar(100)  NULL DEFAULT NULL COMMENT '注册IP',
    `updateTime`             timestamp(0)  NOT NULL COMMENT '更新时间',
    `accountNonExpired`      bit(1)        NOT NULL COMMENT '账户未过期',
    `accountNonLocked`       bit(1)        NOT NULL COMMENT '账户未锁定',
    `createntialsNonExpired` bit(1)        NOT NULL COMMENT '凭证未过期',
    `enabled`                bit(1)        NOT NULL COMMENT '已启用',
    `lastPasswordResetDate`  timestamp(0)  NOT NULL COMMENT '密码被重置的日期',
    `location`               varchar(255)  NULL DEFAULT NULL COMMENT '所在地区',
    `headPortrait`           varchar(255)  NOT NULL COMMENT '头像照片（网络路径）',
    `gender`                 int(11)       NOT NULL COMMENT '性别 0 保密 1 男 2 女',
    `personalProfile`        varchar(2047) NULL DEFAULT NULL COMMENT '个人简介',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_bg_sysuser_username` (`username`) USING BTREE COMMENT '用户名索引',
    INDEX `index_bg_sysuser_username_and_email` (`username`, `email`) USING BTREE COMMENT '用户名和邮箱联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_operaterecord
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bg_operaterecord`
(
    `id`              bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '访客或系统操作员操作记录id',
    `operatorId`      bigint(20)    NOT NULL COMMENT '访客或系统操作员id',
    `action`          int(11)       NOT NULL COMMENT '行为',
    `operateObjectId` varchar(50)   NOT NULL COMMENT '操作对象',
    `record`          varchar(1023) NULL DEFAULT NULL COMMENT '操作记录文字描述',
    `actionDate`      timestamp(0)  NOT NULL COMMENT '操作时间',
    `actionResult`    bit(1)        NOT NULL COMMENT '操作状态 0 失败 1 成功',
    `operatorType`    int(11)       NOT NULL COMMENT '0 访客 1 系统操作员',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_visitrecord_visitorId` (`operatorId`) USING BTREE COMMENT '访客id索引',
    INDEX `index_visitrecord_action` (`action`) USING BTREE COMMENT '操作类型索引',
    INDEX `index_visitrecord_actionDate` (`actionDate`) USING BTREE COMMENT '操作时间索引',
    INDEX `index_visitrecord_operatorType` (`operatorType`) USING BTREE COMMENT '操作员类型索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;