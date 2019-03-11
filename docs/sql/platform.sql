/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-03-11 00:04:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_app
-- ----------------------------
DROP TABLE IF EXISTS `base_app`;
CREATE TABLE `base_app` (
  `app_id` varchar(50) NOT NULL COMMENT '客户端ID',
  `app_secret` varchar(255) NOT NULL COMMENT '客户端秘钥',
  `app_name` varchar(255) NOT NULL COMMENT 'app名称',
  `app_name_en` varchar(255) NOT NULL COMMENT 'app英文名称',
  `app_icon` varchar(255) NOT NULL COMMENT '应用图标',
  `app_type` varchar(50) NOT NULL COMMENT 'app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用',
  `app_desc` varchar(255) DEFAULT NULL COMMENT 'app描述',
  `app_os` varchar(25) DEFAULT NULL COMMENT '移动应用操作系统:ios-苹果 android-安卓',
  `website` varchar(255) NOT NULL COMMENT '官网地址',
  `redirect_uris` varchar(255) NOT NULL COMMENT '第三方授权回掉地址,多个,号隔开',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID:默认为0',
  `user_type` varchar(20) NOT NULL DEFAULT 'platform' COMMENT '用户类型:platform-平台 isp-服务提供商 dev-自研开发者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-基础信息';

-- ----------------------------
-- Records of base_app
-- ----------------------------
INSERT INTO `base_app` VALUES ('1552274783265', '2cde1eaa60fe4af1987f94caa13f29a2', '开放平台资源服务器', 'OpenCloudResourceServer', '', 'server', '开放平台资源服务器', '', 'http://www.baidu.com', 'http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html', '0', 'platform', '2018-11-12 17:48:45', '2019-01-10 22:22:46', '1', '1');
INSERT INTO `base_app` VALUES ('1552294656514', '74a02bade18a42388c3127751b96e1f7', '开放平台管理后台', 'OpenAdmin', '', 'pc', '开放平台管理后台', '', 'http://www.baidu.com', 'http://localhost:8080/login/callback', '0', 'platform', '2018-11-12 17:48:45', '2019-01-10 22:22:46', '1', '1');

-- ----------------------------
-- Table structure for base_app_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_app_authority`;
CREATE TABLE `base_app_authority` (
  `authority_id` bigint(50) NOT NULL COMMENT '权限ID',
  `app_id` varchar(100) NOT NULL COMMENT '应用ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
  KEY `authority_id` (`authority_id`) USING BTREE,
  KEY `app_id` (`app_id`) USING BTREE,
  CONSTRAINT `base_app_authority_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `base_authority` (`authority_id`),
  CONSTRAINT `base_app_authority_ibfk_2` FOREIGN KEY (`app_id`) REFERENCES `base_app` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-授权';

-- ----------------------------
-- Records of base_app_authority
-- ----------------------------

-- ----------------------------
-- Table structure for base_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_authority`;
CREATE TABLE `base_authority` (
  `authority_id` bigint(20) NOT NULL,
  `authority` varchar(255) NOT NULL COMMENT '权限标识',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单资源ID',
  `api_id` bigint(20) DEFAULT NULL COMMENT 'API资源ID',
  `operation_id` bigint(20) DEFAULT NULL,
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态',
  `service_id` varchar(100) DEFAULT NULL COMMENT '服务名',
  PRIMARY KEY (`authority_id`),
  KEY `fk_base_authority_base_resource_menu` (`menu_id`) USING BTREE,
  KEY `fk_base_authority_base_resource_api` (`api_id`) USING BTREE,
  KEY `operation_id` (`operation_id`),
  CONSTRAINT `base_authority_ibfk_1` FOREIGN KEY (`operation_id`) REFERENCES `base_resource_operation` (`operation_id`),
  CONSTRAINT `fk_base_authority_base_resource_api` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`),
  CONSTRAINT `fk_base_authority_base_resource_menu` FOREIGN KEY (`menu_id`) REFERENCES `base_resource_menu` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限-菜单权限、操作权限、API权限';

-- ----------------------------
-- Records of base_authority
-- ----------------------------
INSERT INTO `base_authority` VALUES ('1', 'all', null, '1', null, '1', 'all');
INSERT INTO `base_authority` VALUES ('2', 'actuator', null, '2', null, '1', 'all');

-- ----------------------------
-- Table structure for base_resource_api
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_api`;
CREATE TABLE `base_resource_api` (
  `api_id` bigint(20) NOT NULL COMMENT '接口ID',
  `api_code` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '接口编码',
  `api_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  `api_category` varchar(20) COLLATE utf8_bin DEFAULT 'default' COMMENT '接口分类:default-默认分类',
  `api_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `request_method` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求方式',
  `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务ID',
  `path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `priority` bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `is_open` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否为开放接口：0-否  1-是',
  `is_auth` tinyint(3) NOT NULL DEFAULT '1' COMMENT '是否需要认证: 0-无认证 1-身份认证 默认:1',
  PRIMARY KEY (`api_id`),
  UNIQUE KEY `api_code` (`api_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-API接口';

-- ----------------------------
-- Records of base_resource_api
-- ----------------------------
INSERT INTO `base_resource_api` VALUES ('1', 'all', '全部', 'default', '所有接口', 'post,get', 'all', '/**', '0', '1', '2019-03-07 21:35:16', '2019-03-07 21:35:19', '1', '1', '1');
INSERT INTO `base_resource_api` VALUES ('2', 'actuator', '监控端点', 'default', '监控端点', 'post', 'all', '/actuator/**', '0', '1', '2019-03-07 21:52:17', '2019-03-07 21:52:20', '1', '1', '1');

-- ----------------------------
-- Table structure for base_resource_menu
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_menu`;
CREATE TABLE `base_resource_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单Id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级菜单',
  `menu_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单编码',
  `menu_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `menu_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `prefix` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '路径前缀',
  `path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求路径',
  `icon` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '菜单标题',
  `target` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '_self' COMMENT '打开方式:_self窗口内,_blank新窗口',
  `priority` bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `menu_code` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-菜单信息';

-- ----------------------------
-- Records of base_resource_menu
-- ----------------------------
INSERT INTO `base_resource_menu` VALUES ('1', '0', 'system', '系统管理', '系统安全', '/', '', null, '_self', '1', '1', '2018-07-29 21:20:10', '2019-02-25 00:13:50', '1');
INSERT INTO `base_resource_menu` VALUES ('2', '1', 'systemAuth', '访问控制', '权限分配,菜单资源、操作资源、接口资源列表', '/', 'system/access-grant/index', '', '_self', '2', '1', '2018-07-29 21:20:13', '2019-02-25 00:16:10', '1');
INSERT INTO `base_resource_menu` VALUES ('3', '1', 'systemMenu', '菜单资源', '菜单资源管理', '/', 'system/menus/index', null, '_self', '3', '1', '2018-07-29 21:20:13', '2019-02-13 21:55:05', '1');
INSERT INTO `base_resource_menu` VALUES ('4', '1', 'systemMonitor', '服务监控', '服务监控', '/', '', null, '_self', '4', '1', '2018-07-29 21:20:13', '2019-02-25 00:15:43', '1');
INSERT INTO `base_resource_menu` VALUES ('5', '1', 'gatewayRoute', '动态路由', '网关路由', '/', 'gateway/route/index', null, '_self', '5', '1', '2018-07-29 21:20:13', '2019-02-25 00:15:23', '1');
INSERT INTO `base_resource_menu` VALUES ('6', '1', 'systemApi', '接口资源', '开发API接口资源', '/', 'system/api/index', null, '_self', '6', '1', '2018-07-29 21:20:13', '2018-12-29 14:55:33', '1');
INSERT INTO `base_resource_menu` VALUES ('7', '1', 'gatewayTrace', '服务追踪', '服务追踪', 'http://', 'www.baidu.com', null, '_self', '7', '1', '2018-11-30 02:11:18', '2019-01-12 01:44:59', '1');
INSERT INTO `base_resource_menu` VALUES ('8', '1', 'systemRole', '角色信息', '角色管理', '/', 'system/role/index', '', '_self', '8', '1', '2018-12-27 15:26:54', '2019-01-09 01:33:22', '1');
INSERT INTO `base_resource_menu` VALUES ('9', '1', 'systemApp', '应用信息', '应用信息、授权', '/', 'system/app/index', '', '_self', '0', '1', '2018-12-27 15:41:52', '2018-12-29 14:56:11', '1');
INSERT INTO `base_resource_menu` VALUES ('10', '1', 'systemUser', '系统用户', '', '/', 'system/user/index', '', '_self', '0', '1', '2018-12-27 15:46:29', '2018-12-29 14:56:28', '1');
INSERT INTO `base_resource_menu` VALUES ('11', '1', 'apiDebug', '接口调试', 'swagger接口调试', 'http://', 'localhost:8888', '', '_self', '0', '1', '2019-01-10 20:47:19', '2019-02-25 00:27:27', '1');
INSERT INTO `base_resource_menu` VALUES ('12', '1', 'gatewayLogs', '访问日志', '', '/', 'system/access-logs/index', '', '_self', '0', '1', '2019-01-28 02:37:42', '2019-02-25 00:16:40', '1');
INSERT INTO `base_resource_menu` VALUES ('13', '1', 'gateway', '网关管理', '', '/', '', '', '_self', '0', '1', '2019-02-25 00:15:09', '2019-02-25 00:15:09', '0');
INSERT INTO `base_resource_menu` VALUES ('14', '1', 'help', '帮助文档', '', '/', '', '', '_self', '0', '1', '2019-02-25 00:26:44', '2019-02-25 00:26:44', '0');
INSERT INTO `base_resource_menu` VALUES ('15', '1', 'wiki', '使用手册', 'wiki', 'https://', 'gitee.com/liuyadu/open-cloud/wikis/pages', '', '_self', '0', '1', '2019-02-25 01:02:44', '2019-02-25 01:03:37', '0');

-- ----------------------------
-- Table structure for base_resource_operation
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_operation`;
CREATE TABLE `base_resource_operation` (
  `operation_id` bigint(20) NOT NULL COMMENT '资源ID',
  `operation_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源编码',
  `operation_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源名称',
  `operation_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
  `path` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '资源路径',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '资源父节点',
  `priority` int(10) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`operation_id`),
  UNIQUE KEY `operation_code` (`operation_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-功能操作';

-- ----------------------------
-- Records of base_resource_operation
-- ----------------------------

-- ----------------------------
-- Table structure for base_role
-- ----------------------------
DROP TABLE IF EXISTS `base_role`;
CREATE TABLE `base_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '角色名称',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `role_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统角色-基础信息';

-- ----------------------------
-- Records of base_role
-- ----------------------------
INSERT INTO `base_role` VALUES ('1', 'super', '超级管理员', '1', '超级管理员,拥有所有权限,责任重大', '2018-07-29 21:14:50', '2019-01-29 01:19:03', '1');
INSERT INTO `base_role` VALUES ('2', 'admin', '普通管理员', '1', '普通管理员', '2018-07-29 21:14:54', '2019-02-13 23:09:46', '1');
INSERT INTO `base_role` VALUES ('3', 'dev', '自研开发者', '1', '第三方开发者', '2018-07-29 21:14:54', '2019-02-19 22:44:21', '1');
INSERT INTO `base_role` VALUES ('4', 'isp', '服务提供商', '1', '第三方开发者', '2018-07-29 21:14:54', '2019-01-29 01:19:46', '1');

-- ----------------------------
-- Table structure for base_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_role_authority`;
CREATE TABLE `base_role_authority` (
  `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间:null表示长期',
  KEY `authority_id` (`authority_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  CONSTRAINT `base_role_authority_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `base_authority` (`authority_id`),
  CONSTRAINT `base_role_authority_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统角色-授权';

-- ----------------------------
-- Records of base_role_authority
-- ----------------------------

-- ----------------------------
-- Table structure for base_role_user
-- ----------------------------
DROP TABLE IF EXISTS `base_role_user`;
CREATE TABLE `base_role_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  KEY `fk_user` (`user_id`) USING BTREE,
  KEY `fk_role` (`role_id`) USING BTREE,
  CONSTRAINT `base_role_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`user_id`),
  CONSTRAINT `base_role_user_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统角色-角色与用户关联';

-- ----------------------------
-- Records of base_role_user
-- ----------------------------
INSERT INTO `base_role_user` VALUES ('521677655146233856', '1');

-- ----------------------------
-- Table structure for base_user
-- ----------------------------
DROP TABLE IF EXISTS `base_user`;
CREATE TABLE `base_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(20) DEFAULT 'platform' COMMENT '用户类型:platform-平台 isp-服务提供商 dev-自研开发者',
  `company_id` bigint(20) DEFAULT NULL COMMENT '企业ID',
  `register_ip` varchar(100) DEFAULT NULL COMMENT '注册IP',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态:0-禁用 1-启用 2-锁定',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-基础信息';

-- ----------------------------
-- Records of base_user
-- ----------------------------
INSERT INTO `base_user` VALUES ('521677655146233856', 'admin', '超级管理员', null, '515608851@qq.com', '18518226890', 'platform', null, null, '2018-12-10 13:20:45', '1', null, '2018-12-10 13:20:45', '2018-12-10 13:20:45');

-- ----------------------------
-- Table structure for base_user_account
-- ----------------------------
DROP TABLE IF EXISTS `base_user_account`;
CREATE TABLE `base_user_account` (
  `account_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户Id',
  `account` varchar(255) DEFAULT NULL COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
  `password` varchar(255) DEFAULT NULL COMMENT '密码凭证：站内的保存密码、站外的不保存或保存token）',
  `account_type` varchar(255) DEFAULT NULL COMMENT '登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等',
  PRIMARY KEY (`account_id`),
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `base_user_account_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-登录账号';

-- ----------------------------
-- Records of base_user_account
-- ----------------------------
INSERT INTO `base_user_account` VALUES ('521677655368531968', '521677655146233856', 'admin', '$2a$10$A7EHximvrsa4ESX1uSlkJupbg2PLO2StzDzy67NX4YV25MxmbGvXu', 'username');

-- ----------------------------
-- Table structure for base_user_account_logs
-- ----------------------------
DROP TABLE IF EXISTS `base_user_account_logs`;
CREATE TABLE `base_user_account_logs` (
  `id` bigint(20) NOT NULL,
  `login_time` datetime NOT NULL,
  `login_ip` varchar(255) NOT NULL COMMENT '登录Ip',
  `login_agent` varchar(500) NOT NULL COMMENT '登录设备',
  `login_nums` int(11) NOT NULL COMMENT '登录次数',
  `user_id` bigint(20) NOT NULL,
  `account` varchar(100) NOT NULL,
  `account_type` varchar(50) NOT NULL,
  `account_id` bigint(20) NOT NULL COMMENT '账号ID',
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`) USING BTREE,
  CONSTRAINT `base_user_account_logs_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `base_user_account` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-登录日志';

-- ----------------------------
-- Records of base_user_account_logs
-- ----------------------------

-- ----------------------------
-- Table structure for base_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `base_user_authority`;
CREATE TABLE `base_user_authority` (
  `authority_id` bigint(20) NOT NULL COMMENT '权限ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  KEY `authority_id` (`authority_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `base_user_authority_ibfk_1` FOREIGN KEY (`authority_id`) REFERENCES `base_authority` (`authority_id`),
  CONSTRAINT `base_user_authority_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-授权';

-- ----------------------------
-- Records of base_user_authority
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_access_logs
-- ----------------------------
DROP TABLE IF EXISTS `gateway_access_logs`;
CREATE TABLE `gateway_access_logs` (
  `access_id` bigint(20) NOT NULL COMMENT '访问ID',
  `path` varchar(255) DEFAULT NULL COMMENT '访问路径',
  `params` text COMMENT '请求数据',
  `headers` text COMMENT '请求头',
  `ip` varchar(500) DEFAULT NULL COMMENT '请求IP',
  `http_status` varchar(100) DEFAULT NULL COMMENT '响应状态',
  `method` varchar(50) DEFAULT NULL,
  `request_time` datetime DEFAULT NULL COMMENT '访问时间',
  `response_time` datetime DEFAULT NULL,
  `use_time` bigint(20) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `access_desc` varchar(255) DEFAULT NULL COMMENT '异常',
  `authentication` text COMMENT '认证信息',
  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP',
  PRIMARY KEY (`access_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-访问日志';

-- ----------------------------
-- Records of gateway_access_logs
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_ip_limit
-- ----------------------------
DROP TABLE IF EXISTS `gateway_ip_limit`;
CREATE TABLE `gateway_ip_limit` (
  `policy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `policy_name` varchar(100) NOT NULL COMMENT '策略名称',
  `policy_type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '策略类型:0-拒绝/黑名单 1-允许/白名单',
  `ip_address` varchar(255) NOT NULL COMMENT 'ip地址/IP段:多个用,隔开,最多10个',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最近一次修改时间',
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-IP访问控制-策略';

-- ----------------------------
-- Records of gateway_ip_limit
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_ip_limit_api
-- ----------------------------
DROP TABLE IF EXISTS `gateway_ip_limit_api`;
CREATE TABLE `gateway_ip_limit_api` (
  `policy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `api_id` bigint(20) NOT NULL COMMENT '接口资源ID',
  KEY `policy_id` (`policy_id`) USING BTREE,
  KEY `api_id` (`api_id`) USING BTREE,
  CONSTRAINT `gateway_ip_limit_api_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_ip_limit` (`policy_id`),
  CONSTRAINT `gateway_ip_limit_api_ibfk_2` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-IP访问控制-API接口';

-- ----------------------------
-- Records of gateway_ip_limit_api
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit`;
CREATE TABLE `gateway_rate_limit` (
  `policy_id` bigint(20) NOT NULL,
  `service_id` varchar(0) NOT NULL COMMENT '服务名',
  `limit` bigint(20) NOT NULL DEFAULT '0' COMMENT '流量限制',
  `interval_unit` varchar(10) NOT NULL DEFAULT 'second' COMMENT '时间单位:second-秒,minute-分钟,hour-小时,day-天',
  `limit_type` varchar(255) DEFAULT NULL COMMENT '限流规则类型:url,origin,user',
  `limit_desc` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-策略';

-- ----------------------------
-- Records of gateway_rate_limit
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit_api
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit_api`;
CREATE TABLE `gateway_rate_limit_api` (
  `policy_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
  `api_id` bigint(11) NOT NULL DEFAULT '1' COMMENT '时间间隔(秒)',
  KEY `policy_id` (`policy_id`) USING BTREE,
  KEY `api_id` (`api_id`) USING BTREE,
  CONSTRAINT `gateway_rate_limit_api_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_rate_limit` (`policy_id`),
  CONSTRAINT `gateway_rate_limit_api_ibfk_2` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-API接口';

-- ----------------------------
-- Records of gateway_rate_limit_api
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit_origin
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit_origin`;
CREATE TABLE `gateway_rate_limit_origin` (
  `policy_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
  `origins` bigint(11) NOT NULL DEFAULT '1' COMMENT '来源域名',
  KEY `policy_id` (`policy_id`) USING BTREE,
  CONSTRAINT `gateway_rate_limit_origin_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_rate_limit` (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-来源限流';

-- ----------------------------
-- Records of gateway_rate_limit_origin
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_rate_limit_role
-- ----------------------------
DROP TABLE IF EXISTS `gateway_rate_limit_role`;
CREATE TABLE `gateway_rate_limit_role` (
  `policy_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
  `role_id` bigint(11) NOT NULL DEFAULT '1' COMMENT '角色Id',
  KEY `policy_id` (`policy_id`) USING BTREE,
  KEY `role_id` (`role_id`) USING BTREE,
  CONSTRAINT `gateway_rate_limit_role_ibfk_1` FOREIGN KEY (`policy_id`) REFERENCES `gateway_rate_limit` (`policy_id`),
  CONSTRAINT `gateway_rate_limit_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `base_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-流量控制-角色限流';

-- ----------------------------
-- Records of gateway_rate_limit_role
-- ----------------------------

-- ----------------------------
-- Table structure for gateway_route
-- ----------------------------
DROP TABLE IF EXISTS `gateway_route`;
CREATE TABLE `gateway_route` (
  `id` bigint(20) NOT NULL,
  `route_id` varchar(100) DEFAULT NULL COMMENT '路由ID',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `service_id` varchar(255) DEFAULT NULL COMMENT '服务ID',
  `url` varchar(255) DEFAULT NULL COMMENT '完整地址',
  `strip_prefix` tinyint(3) NOT NULL DEFAULT '1' COMMENT '忽略前缀',
  `retryable` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0-不重试 1-重试',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `route_desc` varchar(255) DEFAULT NULL,
  `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='开放网关-路由';

-- ----------------------------
-- Records of gateway_route
-- ----------------------------

-- ----------------------------
-- Table structure for notify_http_logs
-- ----------------------------
DROP TABLE IF EXISTS `notify_http_logs`;
CREATE TABLE `notify_http_logs` (
  `msg_id` varchar(50) NOT NULL,
  `retry_nums` decimal(8,0) NOT NULL COMMENT '重试次数',
  `total_nums` decimal(8,0) DEFAULT NULL COMMENT '通知总次数',
  `delay` decimal(16,0) NOT NULL COMMENT '延迟时间',
  `result` decimal(1,0) NOT NULL COMMENT '通知结果',
  `url` text NOT NULL COMMENT '通知地址',
  `type` varchar(255) DEFAULT NULL COMMENT '通知类型',
  `data` longtext COMMENT '请求数据',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='消息通知-Http异步通知日志';

-- ----------------------------
-- Records of notify_http_logs
-- ----------------------------

