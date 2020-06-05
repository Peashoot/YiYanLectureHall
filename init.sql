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
-- Table structure for bg_sysuser -- 系统用户表
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
    `socialAccount`          varchar(20)   NULL DEFAULT NULL COMMENT 'EMS或者QQ',
    `lastLoginTime`          bigint(20)    NOT NULL COMMENT '上一次登录时间',
    `registerTime`           bigint(20)    NOT NULL COMMENT '用户注册时间',
    `lastLoginIp`            varchar(100)  NULL DEFAULT NULL COMMENT '上一次登录IP',
    `registerIp`             varchar(100)  NULL DEFAULT NULL COMMENT '注册IP',
    `updateTime`             bigint(20)    NOT NULL COMMENT '更新时间',
    `accountExpiredTime`     bigint(20)    NOT NULL COMMENT '账户过期时间',
    `accountLockedTime`      bigint(20)    NOT NULL COMMENT '账户锁定时间',
    `credentialsExpiredTime` bigint(20)    NOT NULL COMMENT '凭证过期时间',
    `enabledTimestamp`       bigint(20)    NOT NULL COMMENT '启用时间',
    `lastPasswordResetTime`  bigint(20)    NOT NULL COMMENT '密码被重置的日期',
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
-- Table structure for bg_operaterecord -- 操作记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bg_operaterecord`
(
    `id`              bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '访客或系统操作员操作记录id',
    `operatorId`      bigint(20)    NOT NULL COMMENT '访客或系统操作员id',
    `action`          int(11)       NOT NULL COMMENT '行为',
    `operateObjectId` varchar(50)   NOT NULL COMMENT '操作对象',
    `record`          varchar(1023) NULL DEFAULT NULL COMMENT '操作记录文字描述',
    `actionTime`      bigint(20)    NOT NULL COMMENT '操作时间',
    `actionResult`    bit(1)        NOT NULL COMMENT '操作状态 0 失败 1 成功',
    `operatorType`    int(11)       NOT NULL COMMENT '0 访客 1 系统操作员',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_visitrecord_visitorId` (`operatorId`) USING BTREE COMMENT '访客id索引',
    INDEX `index_visitrecord_action` (`action`) USING BTREE COMMENT '操作类型索引',
    INDEX `index_visitrecord_actionTime` (`actionTime`) USING BTREE COMMENT '操作时间索引',
    INDEX `index_visitrecord_operatorType` (`operatorType`) USING BTREE COMMENT '操作员类型索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_article -- 文章信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bg_article`
(
    `id`           varchar(32)   NOT NULL COMMENT '记录id',
    `author`       varchar(50)   NOT NULL COMMENT '作者',
    `title`        varchar(255)  NOT NULL COMMENT '标题',
    `keywords`     varchar(255)  NOT NULL COMMENT '关键词(多个关键词用“;”隔开)',
    `filePath`     varchar(255)  NOT NULL COMMENT '静态md文件本地路径',
    `createTime`   bigint(20)    NOT NULL COMMENT '创建时间',
    `createUserId` int(11)       NOT NULL COMMENT '创建用户',
    `modifyTime`   bigint(20)    NOT NULL COMMENT '修改时间',
    `modifyUserId` int(11)       NOT NULL COMMENT '修改用户',
    `status`       int(11)       NOT NULL COMMENT '文章状态 0：编辑中；200：已发布；400：已删除',
    `overview`     varchar(1023) NULL DEFAULT NULL COMMENT '文章概述',
    `pageView`     bigint(20)    NOT NULL COMMENT '浏览量',
    `articleUrl`   varchar(255)  NOT NULL COMMENT '文章URL连接',
    `supportCount` int(11)       NOT NULL COMMENT '赞成数',
    `againstCount` int(11)       NOT NULL COMMENT '反对数',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_bg_article_author` (`author`) USING BTREE COMMENT '作者索引',
    INDEX `index_bg_article_createtime` (`createTime`) USING BTREE COMMENT '创建时间索引',
    INDEX `index_bg_article_modifytime` (`modifyTime`) USING BTREE COMMENT '修改时间索引'
) ENGINE = InnoDB
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_comment -- 评论信息表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `bg_comment`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT COMMENT '记录id',
    `visitorId`    bigint(20)  NOT NULL COMMENT '访客id',
    `comment`      text        NOT NULL COMMENT '评论内容',
    `articleId`    varchar(32) NOT NULL COMMENT '文件id',
    `commentTime`  bigint(20)  NOT NULL COMMENT '评论时间',
    `status`       int(11)     NOT NULL COMMENT '评论状态',
    `anonymous`    bit(1)      NOT NULL COMMENT '是否匿名',
    `supportCount` int(11)     NOT NULL COMMENT '赞成数',
    `againstCount` int(11)     NOT NULL COMMENT '反对数',
    `commentTo`    int(11)     NOT NULL COMMENT '引用的评论id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_bg_comment_visitorid` (`visitorId`) USING BTREE COMMENT '访客id',
    INDEX `index_bg_comment_articelid` (`articleId`) USING BTREE COMMENT '文章id'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_exceptionrecord -- 异常日志记录表
-- ----------------------------
DROP TABLE IF EXISTS `bg_exceptionrecord`;
CREATE TABLE `bg_exceptionrecord`
(
    `id`           bigint(20)   NOT NULL COMMENT '记录id',
    `className`    varchar(255) NOT NULL COMMENT '异常类名称',
    `methodName`   varchar(255) NOT NULL COMMENT '异常方法名称',
    `paramValues`  varchar(255) NOT NULL COMMENT '产生异常时，方法的传入参数值',
    `exceptionMsg` varchar(255) NOT NULL COMMENT '错误说明',
    `invokeStack`  varchar(255) NOT NULL COMMENT '调用堆栈',
    `appearTime`   bigint(20)   NOT NULL COMMENT '发生异常的时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_bg_exceptionrecord_classname` (`className`) USING BTREE COMMENT '类名索引',
    INDEX `index_bg_exceptionrecord_methodname` (`methodName`) USING BTREE COMMENT '方法名索引'
) ENGINE = InnoDB
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_file -- 本地文件存储信息
-- ----------------------------
DROP TABLE IF EXISTS `bg_file`;
CREATE TABLE `bg_file`
(
    `id`             varchar(32)  NOT NULL COMMENT '记录id',
    `creatorId`      bigint(20)   NOT NULL COMMENT '访客id',
    `sysUserId`      int(11)      NOT NULL COMMENT '系统管理员id',
    `originalName`   varchar(255) NULL DEFAULT NULL COMMENT '原始文件名',
    `type`           varchar(10)  NOT NULL COMMENT '文件类型',
    `localPath`      varchar(255) NOT NULL COMMENT '本地路径',
    `netPath`        varchar(255) NOT NULL COMMENT '网络路径',
    `createTime`     bigint(20)   NOT NULL COMMENT '创建日期',
    `originalNetUrl` varchar(255) NULL DEFAULT NULL COMMENT '原始网络路径',
    `md5Sign`        varchar(255) NOT NULL COMMENT '文件MD5签名',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_bg_file_creatorid` (`creatorId`) USING BTREE COMMENT '访客id'
) ENGINE = InnoDB
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_visitor -- 访客信息表
-- ----------------------------
DROP TABLE IF EXISTS `bg_visitor`;
CREATE TABLE `bg_visitor`
(
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '记录id',
    `visitor`        varchar(255) NOT NULL COMMENT '访客名称',
    `sysUserName`    varchar(255) NULL DEFAULT NULL COMMENT '系统用户名称',
    `firstVisitTime` bigint(20)   NOT NULL COMMENT '第一次访问时间',
    `visitFromIp`    varchar(36)  NOT NULL COMMENT '访客访问的ip地址（ipv4或ipv6）',
    `location`       varchar(255) NULL DEFAULT NULL COMMENT '根据IP地址查询到的访问地址',
    `browser`        varchar(255) NOT NULL COMMENT '浏览器指纹',
    `os`             varchar(30)  NOT NULL COMMENT '操作系统',
    `roleIds`        varchar(255) NOT NULL COMMENT '角色id列表（多个id用\',\'隔开）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for bg_role
-- ----------------------------
DROP TABLE IF EXISTS `bg_role`;
CREATE TABLE `bg_role`
(
    `id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '记录id',
    `roleName`     varchar(50)  NOT NULL COMMENT '角色名称，ROLE_开头',
    `permissions`   varchar(511) NOT NULL COMMENT '权限',
    `insertUserId` int(11)      NOT NULL COMMENT '新增用户id',
    `insertTime`   bigint(20)   NOT NULL COMMENT '新增时间',
    `updateUserId` int(11)      NOT NULL COMMENT '修改用户id',
    `updateTime`   bigint(20)   NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;