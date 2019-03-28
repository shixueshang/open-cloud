/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : open-platform

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2019-03-18 04:59:21
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
INSERT INTO `base_app` VALUES ('1552274783265', '2cde1eaa60fe4af1987f94caa13f29a2', '资源服务器', 'ResourceServer', '', 'server', '资源服务器', '', 'http://www.baidu.com', '521677655146233856', 'platform', '2018-11-12 17:48:45', '2019-03-16 00:29:42', '1', '1');
INSERT INTO `base_app` VALUES ('1552294656514', '74a02bade18a42388c3127751b96e1f7', '运营后台', 'Admin', '', 'pc', '运营后台', '', 'http://www.baidu.com', '521677655146233856', 'platform', '2018-11-12 17:48:45', '2019-03-16 00:24:26', '1', '1');

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
INSERT INTO `base_app_authority` VALUES ('555852320928169984', '1552274783265', null);
INSERT INTO `base_app_authority` VALUES ('2', '1552274783265', null);
INSERT INTO `base_app_authority` VALUES ('555852320928169984', '1552294656514', null);

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
                                `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态',
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
INSERT INTO `base_authority` VALUES ('1', 'all', null, '1', null, '1');
INSERT INTO `base_authority` VALUES ('2', 'actuator', null, '2', null, '1');
INSERT INTO `base_authority` VALUES ('3', 'menu:system', '1', null, null, '1');
INSERT INTO `base_authority` VALUES ('4', 'menu:gatewayIpLimit', '2', null, null, '1');
INSERT INTO `base_authority` VALUES ('5', 'menu:systemMenu', '3', null, null, '1');
INSERT INTO `base_authority` VALUES ('6', 'menu:systemMonitor', '4', null, null, '1');
INSERT INTO `base_authority` VALUES ('7', 'menu:gatewayRoute', '5', null, null, '1');
INSERT INTO `base_authority` VALUES ('8', 'menu:systemApi', '6', null, null, '1');
INSERT INTO `base_authority` VALUES ('9', 'menu:gatewayTrace', '7', null, null, '1');
INSERT INTO `base_authority` VALUES ('10', 'menu:systemRolei', '8', null, null, '1');
INSERT INTO `base_authority` VALUES ('11', 'menu:systemApp', '9', null, null, '1');
INSERT INTO `base_authority` VALUES ('12', 'menu:systemUser', '10', null, null, '1');
INSERT INTO `base_authority` VALUES ('13', 'menu:apiDebug', '11', null, null, '1');
INSERT INTO `base_authority` VALUES ('14', 'menu:gatewayLogs', '12', null, null, '1');
INSERT INTO `base_authority` VALUES ('15', 'menu:gateway', '13', null, null, '1');
INSERT INTO `base_authority` VALUES ('16', 'menu:help', '14', null, null, '1');
INSERT INTO `base_authority` VALUES ('17', 'menu:wiki', '15', null, null, '1');
INSERT INTO `base_authority` VALUES ('555416595988480000', 'operation:systemBrowse', null, null, '555416595535495168', '1');
INSERT INTO `base_authority` VALUES ('555416596055588864', 'operation:systemCreate', null, null, '555416596017840128', '1');
INSERT INTO `base_authority` VALUES ('555416596135280640', 'operation:systemEdit', null, null, '555416596101726208', '1');
INSERT INTO `base_authority` VALUES ('555416596202389504', 'operation:systemRemove', null, null, '555416596168835072', '1');
INSERT INTO `base_authority` VALUES ('555416596298858496', 'operation:systemDetail', null, null, '555416596252721152', '1');
INSERT INTO `base_authority` VALUES ('555416596403716096', 'operation:systemAuthBrowse', null, null, '555416596340801536', '1');
INSERT INTO `base_authority` VALUES ('555416596491796480', 'operation:systemAuthCreate', null, null, '555416596449853440', '1');
INSERT INTO `base_authority` VALUES ('555416596571488256', 'operation:systemAuthEdit', null, null, '555416596529545216', '1');
INSERT INTO `base_authority` VALUES ('555416596651180032', 'operation:systemAuthRemove', null, null, '555416596600848384', '1');
INSERT INTO `base_authority` VALUES ('555416596714094592', 'operation:systemAuthDetail', null, null, '555416596688928768', '1');
INSERT INTO `base_authority` VALUES ('555416596810563584', 'operation:systemMenuBrowse', null, null, '555416596760231936', '1');
INSERT INTO `base_authority` VALUES ('555416596936392704', 'operation:systemMenuCreate', null, null, '555416596877672448', '1');
INSERT INTO `base_authority` VALUES ('555416597087387648', 'operation:systemMenuEdit', null, null, '555416596986724352', '1');
INSERT INTO `base_authority` VALUES ('555416597326462976', 'operation:systemMenuRemove', null, null, '555416597179662336', '1');
INSERT INTO `base_authority` VALUES ('555416597448097792', 'operation:systemMenuDetail', null, null, '555416597393571840', '1');
INSERT INTO `base_authority` VALUES ('555416597531983872', 'operation:systemMonitorBrowse', null, null, '555416597490040832', '1');
INSERT INTO `base_authority` VALUES ('555416597586509824', 'operation:systemMonitorCreate', null, null, '555416597565538304', '1');
INSERT INTO `base_authority` VALUES ('555416597628452864', 'operation:systemMonitorEdit', null, null, '555416597607481344', '1');
INSERT INTO `base_authority` VALUES ('555416597691367424', 'operation:systemMonitorRemove', null, null, '555416597666201600', '1');
INSERT INTO `base_authority` VALUES ('555416597750087680', 'operation:systemMonitorDetail', null, null, '555416597712338944', '1');
INSERT INTO `base_authority` VALUES ('555416597800419328', 'operation:gatewayRouteBrowse', null, null, '555416597775253504', '1');
INSERT INTO `base_authority` VALUES ('555416597846556672', 'operation:gatewayRouteCreate', null, null, '555416597821390848', '1');
INSERT INTO `base_authority` VALUES ('555416597905276928', 'operation:gatewayRouteEdit', null, null, '555416597875916800', '1');
INSERT INTO `base_authority` VALUES ('555416597963997184', 'operation:gatewayRouteRemove', null, null, '555416597943025664', '1');
INSERT INTO `base_authority` VALUES ('555416598014328832', 'operation:gatewayRouteDetail', null, null, '555416597989163008', '1');
INSERT INTO `base_authority` VALUES ('555416598064660480', 'operation:systemApiBrowse', null, null, '555416598035300352', '1');
INSERT INTO `base_authority` VALUES ('555416598106603520', 'operation:systemApiCreate', null, null, '555416598089826304', '1');
INSERT INTO `base_authority` VALUES ('555416598165323776', 'operation:systemApiEdit', null, null, '555416598127575040', '1');
INSERT INTO `base_authority` VALUES ('555416598215655424', 'operation:systemApiRemove', null, null, '555416598194683904', '1');
INSERT INTO `base_authority` VALUES ('555416598261792768', 'operation:systemApiDetail', null, null, '555416598236626944', '1');
INSERT INTO `base_authority` VALUES ('555416598307930112', 'operation:gatewayTraceBrowse', null, null, '555416598282764288', '1');
INSERT INTO `base_authority` VALUES ('555416598349873152', 'operation:gatewayTraceCreate', null, null, '555416598328901632', '1');
INSERT INTO `base_authority` VALUES ('555416598391816192', 'operation:gatewayTraceEdit', null, null, '555416598370844672', '1');
INSERT INTO `base_authority` VALUES ('555416598437953536', 'operation:gatewayTraceRemove', null, null, '555416598412787712', '1');
INSERT INTO `base_authority` VALUES ('555416598479896576', 'operation:gatewayTraceDetail', null, null, '555416598458925056', '1');
INSERT INTO `base_authority` VALUES ('555416598526033920', 'operation:systemRoleBrowse', null, null, '555416598505062400', '1');
INSERT INTO `base_authority` VALUES ('555416598572171264', 'operation:systemRoleCreate', null, null, '555416598547005440', '1');
INSERT INTO `base_authority` VALUES ('555416598609920000', 'operation:systemRoleEdit', null, null, '555416598588948480', '1');
INSERT INTO `base_authority` VALUES ('555416598660251648', 'operation:systemRoleRemove', null, null, '555416598635085824', '1');
INSERT INTO `base_authority` VALUES ('555416598693806080', 'operation:systemRoleDetail', null, null, '555416598677028864', '1');
INSERT INTO `base_authority` VALUES ('555416598723166208', 'operation:systemAppBrowse', null, null, '555416598706388992', '1');
INSERT INTO `base_authority` VALUES ('555416598819635200', 'operation:systemAppCreate', null, null, '555416598802857984', '1');
INSERT INTO `base_authority` VALUES ('555416598857383936', 'operation:systemAppEdit', null, null, '555416598836412416', '1');
INSERT INTO `base_authority` VALUES ('555416598903521280', 'operation:systemAppRemove', null, null, '555416598874161152', '1');
INSERT INTO `base_authority` VALUES ('555416598937075712', 'operation:systemAppDetail', null, null, '555416598924492800', '1');
INSERT INTO `base_authority` VALUES ('555416598966435840', 'operation:systemUserBrowse', null, null, '555416598953852928', '1');
INSERT INTO `base_authority` VALUES ('555416598995795968', 'operation:systemUserCreate', null, null, '555416598979018752', '1');
INSERT INTO `base_authority` VALUES ('555416599029350400', 'operation:systemUserEdit', null, null, '555416599016767488', '1');
INSERT INTO `base_authority` VALUES ('555416599067099136', 'operation:systemUserRemove', null, null, '555416599046127616', '1');
INSERT INTO `base_authority` VALUES ('555416599092264960', 'operation:systemUserDetail', null, null, '555416599079682048', '1');
INSERT INTO `base_authority` VALUES ('555416599125819392', 'operation:apiDebugBrowse', null, null, '555416599109042176', '1');
INSERT INTO `base_authority` VALUES ('555416599159373824', 'operation:apiDebugCreate', null, null, '555416599142596608', '1');
INSERT INTO `base_authority` VALUES ('555416599188733952', 'operation:apiDebugEdit', null, null, '555416599176151040', '1');
INSERT INTO `base_authority` VALUES ('555416599226482688', 'operation:apiDebugRemove', null, null, '555416599209705472', '1');
INSERT INTO `base_authority` VALUES ('555416599255842816', 'operation:apiDebugDetail', null, null, '555416599243259904', '1');
INSERT INTO `base_authority` VALUES ('555416599293591552', 'operation:gatewayLogsBrowse', null, null, '555416599281008640', '1');
INSERT INTO `base_authority` VALUES ('555416599322951680', 'operation:gatewayLogsCreate', null, null, '555416599310368768', '1');
INSERT INTO `base_authority` VALUES ('555416599364894720', 'operation:gatewayLogsEdit', null, null, '555416599343923200', '1');
INSERT INTO `base_authority` VALUES ('555416599394254848', 'operation:gatewayLogsRemove', null, null, '555416599381671936', '1');
INSERT INTO `base_authority` VALUES ('555416599432003584', 'operation:gatewayLogsDetail', null, null, '555416599415226368', '1');
INSERT INTO `base_authority` VALUES ('555416599461363712', 'operation:gatewayBrowse', null, null, '555416599444586496', '1');
INSERT INTO `base_authority` VALUES ('555416599486529536', 'operation:gatewayCreate', null, null, '555416599473946624', '1');
INSERT INTO `base_authority` VALUES ('555416599515889664', 'operation:gatewayEdit', null, null, '555416599499112448', '1');
INSERT INTO `base_authority` VALUES ('555416599549444096', 'operation:gatewayRemove', null, null, '555416599532666880', '1');
INSERT INTO `base_authority` VALUES ('555416599582998528', 'operation:gatewayDetail', null, null, '555416599562027008', '1');
INSERT INTO `base_authority` VALUES ('555416599612358656', 'operation:helpBrowse', null, null, '555416599599775744', '1');
INSERT INTO `base_authority` VALUES ('555416599650107392', 'operation:helpCreate', null, null, '555416599633330176', '1');
INSERT INTO `base_authority` VALUES ('555416599683661824', 'operation:helpEdit', null, null, '555416599666884608', '1');
INSERT INTO `base_authority` VALUES ('555416599721410560', 'operation:helpRemove', null, null, '555416599704633344', '1');
INSERT INTO `base_authority` VALUES ('555416599754964992', 'operation:helpDetail', null, null, '555416599742382080', '1');
INSERT INTO `base_authority` VALUES ('555416599784325120', 'operation:wikiBrowse', null, null, '555416599771742208', '1');
INSERT INTO `base_authority` VALUES ('555416599809490944', 'operation:wikiCreate', null, null, '555416599796908032', '1');
INSERT INTO `base_authority` VALUES ('555416599838851072', 'operation:wikiEdit', null, null, '555416599826268160', '1');
INSERT INTO `base_authority` VALUES ('555416599935320064', 'operation:wikiRemove', null, null, '555416599918542848', '1');
INSERT INTO `base_authority` VALUES ('555416599968874496', 'operation:wikiDetail', null, null, '555416599952097280', '1');
INSERT INTO `base_authority` VALUES ('555416600002428928', 'operation:nacosBrowse', null, null, '555416599985651712', '1');
INSERT INTO `base_authority` VALUES ('555416600044371968', 'operation:nacosCreate', null, null, '555416600023400448', '1');
INSERT INTO `base_authority` VALUES ('555416600082120704', 'operation:nacosEdit', null, null, '555416600065343488', '1');
INSERT INTO `base_authority` VALUES ('555416600119869440', 'operation:nacosRemove', null, null, '555416600103092224', '1');
INSERT INTO `base_authority` VALUES ('555416600161812480', 'operation:nacosDetail', null, null, '555416600145035264', '1');
INSERT INTO `base_authority` VALUES ('555416688917479424', 'menu:nacos', '555410979983196160', null, null, '1');
INSERT INTO `base_authority` VALUES ('555507213150453760', 'menu:gatewayRateLimit', '555507213116899328', null, null, '1');
INSERT INTO `base_authority` VALUES ('555507213225951232', 'operation:gatewayRateLimitBrowse', null, null, '555507213175619584', '1');
INSERT INTO `base_authority` VALUES ('555507213280477184', 'operation:gatewayRateLimitCreate', null, null, '555507213246922752', '1');
INSERT INTO `base_authority` VALUES ('555507213339197440', 'operation:gatewayRateLimitEdit', null, null, '555507213305643008', '1');
INSERT INTO `base_authority` VALUES ('555507213393723392', 'operation:gatewayRateLimitRemove', null, null, '555507213364363264', '1');
INSERT INTO `base_authority` VALUES ('555507213452443648', 'operation:gatewayRateLimitDetail', null, null, '555507213423083520', '1');
INSERT INTO `base_authority` VALUES ('560160743010336768', 'com.github.lyd.base.provider.controller.BaseApiController.addApi', null, '560160742251167744', null, '1');
INSERT INTO `base_authority` VALUES ('560160743203274752', 'com.github.lyd.base.provider.controller.BaseApiController.removeApi', null, '560160743131971584', null, '1');
INSERT INTO `base_authority` VALUES ('560160743354269696', 'com.github.lyd.base.provider.controller.BaseApiController.updateApi', null, '560160743278772224', null, '1');
INSERT INTO `base_authority` VALUES ('560160743505264640', 'com.github.lyd.base.provider.controller.BaseApiController.getApiAllList', null, '560160743425572864', null, '1');
INSERT INTO `base_authority` VALUES ('560160743744339968', 'com.github.lyd.base.provider.controller.BaseApiController.getApi', null, '560160743580762112', null, '1');
INSERT INTO `base_authority` VALUES ('560160743861780480', 'com.github.lyd.base.provider.controller.BaseApiController.getApiList', null, '560160743803060224', null, '1');
INSERT INTO `base_authority` VALUES ('560160743962443776', 'com.github.lyd.base.provider.controller.BaseAppController.updateAppClientInfo', null, '560160743916306432', null, '1');
INSERT INTO `base_authority` VALUES ('560160744205713408', 'com.github.lyd.base.provider.controller.BaseAppController.resetAppSecret', null, '560160744151187456', null, '1');
INSERT INTO `base_authority` VALUES ('560160744377679872', 'com.github.lyd.base.provider.controller.BaseAppController.removeApp', null, '560160744327348224', null, '1');
INSERT INTO `base_authority` VALUES ('560160744474148864', 'com.github.lyd.base.provider.controller.BaseAppController.updateApp', null, '560160744432205824', null, '1');
INSERT INTO `base_authority` VALUES ('560160744583200768', 'com.github.lyd.base.provider.controller.BaseAppController.getAppListPage', null, '560160744537063424', null, '1');
INSERT INTO `base_authority` VALUES ('560160744700641280', 'com.github.lyd.base.provider.controller.BaseAppController.getAppClientInfo', null, '560160744662892544', null, '1');
INSERT INTO `base_authority` VALUES ('560160744797110272', 'com.github.lyd.base.provider.controller.BaseAppController.getApp', null, '560160744755167232', null, '1');
INSERT INTO `base_authority` VALUES ('560160744897773568', 'com.github.lyd.base.provider.controller.BaseAppController.addApp', null, '560160744855830528', null, '1');
INSERT INTO `base_authority` VALUES ('560160745031991296', 'com.github.lyd.base.provider.controller.BaseAuthorityController.grantUserAuthority', null, '560160744990048256', null, '1');
INSERT INTO `base_authority` VALUES ('560160745178791936', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedRoleAuthority', null, '560160745107488768', null, '1');
INSERT INTO `base_authority` VALUES ('560160745279455232', 'com.github.lyd.base.provider.controller.BaseAuthorityController.grantRoleAuthority', null, '560160745237512192', null, '1');
INSERT INTO `base_authority` VALUES ('560160745375924224', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedUserAuthority', null, '560160745333981184', null, '1');
INSERT INTO `base_authority` VALUES ('560160745472393216', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedAppAuthority', null, '560160745430450176', null, '1');
INSERT INTO `base_authority` VALUES ('560160745589833728', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getAccessAuthorityList', null, '560160745543696384', null, '1');
INSERT INTO `base_authority` VALUES ('560160745703079936', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getMenuAuthorityList', null, '560160745656942592', null, '1');
INSERT INTO `base_authority` VALUES ('560160745807937536', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getApiAuthorityList', null, '560160745770188800', null, '1');
INSERT INTO `base_authority` VALUES ('560160745896017920', 'com.github.lyd.base.provider.controller.BaseAuthorityController.grantAppAuthority', null, '560160745858269184', null, '1');
INSERT INTO `base_authority` VALUES ('560160746130898944', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedMyMenuAuthority', null, '560160746088955904', null, '1');
INSERT INTO `base_authority` VALUES ('560160746223173632', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenuOperationList', null, '560160746185424896', null, '1');
INSERT INTO `base_authority` VALUES ('560160746374168576', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenuAllList', null, '560160746332225536', null, '1');
INSERT INTO `base_authority` VALUES ('560160746474831872', 'com.github.lyd.base.provider.controller.BaseMenuController.removeMenu', null, '560160746437083136', null, '1');
INSERT INTO `base_authority` VALUES ('560160746567106560', 'com.github.lyd.base.provider.controller.BaseMenuController.updateMenu', null, '560160746525163520', null, '1');
INSERT INTO `base_authority` VALUES ('560160746655186944', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenu', null, '560160746617438208', null, '1');
INSERT INTO `base_authority` VALUES ('560160746743267328', 'com.github.lyd.base.provider.controller.BaseMenuController.addMenu', null, '560160746705518592', null, '1');
INSERT INTO `base_authority` VALUES ('560160746835542016', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenuListPage', null, '560160746797793280', null, '1');
INSERT INTO `base_authority` VALUES ('560160746932011008', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperationAllList', null, '560160746894262272', null, '1');
INSERT INTO `base_authority` VALUES ('560160747024285696', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperationListPage', null, '560160746986536960', null, '1');
INSERT INTO `base_authority` VALUES ('560160747112366080', 'com.github.lyd.base.provider.controller.BaseOperationController.addOperation', null, '560160747074617344', null, '1');
INSERT INTO `base_authority` VALUES ('560160747200446464', 'com.github.lyd.base.provider.controller.BaseOperationController.addOperationApi', null, '560160747162697728', null, '1');
INSERT INTO `base_authority` VALUES ('560160747343052800', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperation', null, '560160747305304064', null, '1');
INSERT INTO `base_authority` VALUES ('560160747431133184', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperationApi', null, '560160747389190144', null, '1');
INSERT INTO `base_authority` VALUES ('560160747603099648', 'com.github.lyd.base.provider.controller.BaseOperationController.removeOperation', null, '560160747481464832', null, '1');
INSERT INTO `base_authority` VALUES ('560160747691180032', 'com.github.lyd.base.provider.controller.BaseOperationController.updateOperation', null, '560160747653431296', null, '1');
INSERT INTO `base_authority` VALUES ('560160747779260416', 'com.github.lyd.base.provider.controller.BaseRoleController.getRole', null, '560160747741511680', null, '1');
INSERT INTO `base_authority` VALUES ('560160747867340800', 'com.github.lyd.base.provider.controller.BaseRoleController.addRole', null, '560160747829592064', null, '1');
INSERT INTO `base_authority` VALUES ('560160747955421184', 'com.github.lyd.base.provider.controller.BaseRoleController.getRoleUsers', null, '560160747917672448', null, '1');
INSERT INTO `base_authority` VALUES ('560160748051890176', 'com.github.lyd.base.provider.controller.BaseRoleController.updateRole', null, '560160748014141440', null, '1');
INSERT INTO `base_authority` VALUES ('560160748148359168', 'com.github.lyd.base.provider.controller.BaseRoleController.addUserRoles', null, '560160748114804736', null, '1');
INSERT INTO `base_authority` VALUES ('560160748232245248', 'com.github.lyd.base.provider.controller.BaseRoleController.getRoleListPage', null, '560160748198690816', null, '1');
INSERT INTO `base_authority` VALUES ('560160748320325632', 'com.github.lyd.base.provider.controller.BaseRoleController.getRoleAllList', null, '560160748282576896', null, '1');
INSERT INTO `base_authority` VALUES ('560160748412600320', 'com.github.lyd.base.provider.controller.BaseRoleController.removeRole', null, '560160748370657280', null, '1');
INSERT INTO `base_authority` VALUES ('560160748504875008', 'com.github.lyd.base.provider.controller.BaseUserController.updateUser', null, '560160748462931968', null, '1');
INSERT INTO `base_authority` VALUES ('560160748597149696', 'com.github.lyd.base.provider.controller.BaseUserController.updatePassword', null, '560160748555206656', null, '1');
INSERT INTO `base_authority` VALUES ('560160748681035776', 'com.github.lyd.base.provider.controller.BaseUserController.addUser', null, '560160748643287040', null, '1');
INSERT INTO `base_authority` VALUES ('560160748764921856', 'com.github.lyd.base.provider.controller.BaseUserController.getUserRoles', null, '560160748727173120', null, '1');
INSERT INTO `base_authority` VALUES ('560160748853002240', 'com.github.lyd.base.provider.controller.BaseUserController.addUserRoles', null, '560160748815253504', null, '1');
INSERT INTO `base_authority` VALUES ('560160748941082624', 'com.github.lyd.base.provider.controller.BaseUserController.getUserAllList', null, '560160748903333888', null, '1');
INSERT INTO `base_authority` VALUES ('560160749020774400', 'com.github.lyd.base.provider.controller.BaseUserController.restMyPassword', null, '560160748987219968', null, '1');
INSERT INTO `base_authority` VALUES ('560160749113049088', 'com.github.lyd.base.provider.controller.BaseUserController.getUserList', null, '560160749071106048', null, '1');
INSERT INTO `base_authority` VALUES ('560160749196935168', 'com.github.lyd.base.provider.controller.BaseUserController.updateMyUserInfo', null, '560160749159186432', null, '1');
INSERT INTO `base_authority` VALUES ('560160749264044032', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController.registerThirdPartyAccount', null, '560160749234683904', null, '1');
INSERT INTO `base_authority` VALUES ('560160749326958592', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController.resetPassword', null, '560160749297598464', null, '1');
INSERT INTO `base_authority` VALUES ('560160749427621888', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController.localLogin', null, '560160749398261760', null, '1');
INSERT INTO `base_authority` VALUES ('560160755559694336', 'com.github.lyd.auth.provider.controller.ApiController.getUserProfile', null, '560160755526139904', null, '1');
INSERT INTO `base_authority` VALUES ('560160755710689280', 'com.github.lyd.auth.provider.controller.ApiController.getLoginOtherConfig', null, '560160755601637376', null, '1');
INSERT INTO `base_authority` VALUES ('560160760202788864', 'com.github.lyd.admin.provider.controller.AdminController.sign', null, '560160760165040128', null, '1');
INSERT INTO `base_authority` VALUES ('560160760282480640', 'com.github.lyd.admin.provider.controller.AdminController.getLoginToken', null, '560160760248926208', null, '1');
INSERT INTO `base_authority` VALUES ('560160815676653568', 'com.github.lyd.gateway.provider.controller.GatewayAccessLogsController.getAccessLogListPage', null, '560160815638904832', null, '1');
INSERT INTO `base_authority` VALUES ('560160815936700416', 'com.github.lyd.gateway.provider.controller.GatewayController.getServiceList', null, '560160815911534592', null, '1');
INSERT INTO `base_authority` VALUES ('560160816611983360', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.removeIpLimit', null, '560160816150609920', null, '1');
INSERT INTO `base_authority` VALUES ('560160816775561216', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.addIpLimitApis', null, '560160816750395392', null, '1');
INSERT INTO `base_authority` VALUES ('560160816876224512', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.getIpLimit', null, '560160816846864384', null, '1');
INSERT INTO `base_authority` VALUES ('560160816985276416', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.updateIpLimit', null, '560160816955916288', null, '1');
INSERT INTO `base_authority` VALUES ('560160817102716928', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.addIpLimit', null, '560160817056579584', null, '1');
INSERT INTO `base_authority` VALUES ('560160817253711872', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.getIpLimitListPage', null, '560160817228546048', null, '1');
INSERT INTO `base_authority` VALUES ('560160817459232768', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.getIpLimitApiList', null, '560160817404706816', null, '1');
INSERT INTO `base_authority` VALUES ('560160817689919488', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.addRateLimit', null, '560160817656365056', null, '1');
INSERT INTO `base_authority` VALUES ('560160818050629632', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.removeRateLimit', null, '560160818025463808', null, '1');
INSERT INTO `base_authority` VALUES ('560160818373591040', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.addRateLimitApis', null, '560160818344230912', null, '1');
INSERT INTO `base_authority` VALUES ('560160818558140416', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.getRateLimit', null, '560160818528780288', null, '1');
INSERT INTO `base_authority` VALUES ('560160818776244224', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.updateRateLimit', null, '560160818751078400', null, '1');
INSERT INTO `base_authority` VALUES ('560160818923044864', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.getRateLimitListPage', null, '560160818893684736', null, '1');
INSERT INTO `base_authority` VALUES ('560160819199868928', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.getRateLimitApiList', null, '560160819174703104', null, '1');
INSERT INTO `base_authority` VALUES ('560160819355058176', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.removeRoute', null, '560160819325698048', null, '1');
INSERT INTO `base_authority` VALUES ('560160819631882240', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.addRoute', null, '560160819602522112', null, '1');
INSERT INTO `base_authority` VALUES ('560160819829014528', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.updateRoute', null, '560160819803848704', null, '1');
INSERT INTO `base_authority` VALUES ('560160820097449984', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.getRouteListPage', null, '560160820063895552', null, '1');
INSERT INTO `base_authority` VALUES ('560160820424605696', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.getRoute', null, '560160820395245568', null, '1');

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
INSERT INTO `base_resource_api` VALUES ('1', 'all', '全部', 'default', '所有请求', 'get,post', 'opencloud-gateway-provider', '/**', '0', '1', '2019-03-07 21:52:17', '2019-03-14 21:41:28', '1', '1', '1');
INSERT INTO `base_resource_api` VALUES ('2', 'actuator', '监控端点', 'default', '监控端点', 'post', 'opencloud-gateway-provider', '/actuator/**', '0', '1', '2019-03-07 21:52:17', '2019-03-14 21:41:28', '1', '1', '1');
INSERT INTO `base_resource_api` VALUES ('560160742251167744', 'com.github.lyd.base.provider.controller.BaseApiController.addApi', '添加接口资源', 'default', '添加接口资源', 'post', 'opencloud-base-provider', '/api/add', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160743131971584', 'com.github.lyd.base.provider.controller.BaseApiController.removeApi', '移除接口资源', 'default', '移除接口资源', 'post', 'opencloud-base-provider', '/api/remove', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160743278772224', 'com.github.lyd.base.provider.controller.BaseApiController.updateApi', '编辑接口资源', 'default', '编辑接口资源', 'post', 'opencloud-base-provider', '/api/update', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160743425572864', 'com.github.lyd.base.provider.controller.BaseApiController.getApiAllList', '获取所有接口列表', 'default', '获取所有接口列表', 'get', 'opencloud-base-provider', '/api/all', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:13:54', '1', '1', '1');
INSERT INTO `base_resource_api` VALUES ('560160743580762112', 'com.github.lyd.base.provider.controller.BaseApiController.getApi', '获取接口资源', 'default', '获取接口资源', 'get', 'opencloud-base-provider', '/api/{apiId}/info', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:13:09', '1', '1', '1');
INSERT INTO `base_resource_api` VALUES ('560160743803060224', 'com.github.lyd.base.provider.controller.BaseApiController.getApiList', '获取分页接口列表', 'default', '获取分页接口列表', 'get', 'opencloud-base-provider', '/api', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160743916306432', 'com.github.lyd.base.provider.controller.BaseAppController.updateAppClientInfo', '完善应用开发信息', 'default', '完善应用开发信息', 'post', 'opencloud-base-provider', '/app/client/update', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744151187456', 'com.github.lyd.base.provider.controller.BaseAppController.resetAppSecret', '重置应用秘钥', 'default', '重置应用秘钥', 'post', 'opencloud-base-provider', '/app/reset', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744327348224', 'com.github.lyd.base.provider.controller.BaseAppController.removeApp', '删除应用信息', 'default', '删除应用信息', 'post', 'opencloud-base-provider', '/app/remove', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744432205824', 'com.github.lyd.base.provider.controller.BaseAppController.updateApp', '编辑应用信息', 'default', '编辑应用信息', 'post', 'opencloud-base-provider', '/app/update', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744537063424', 'com.github.lyd.base.provider.controller.BaseAppController.getAppListPage', '获取分页应用列表', 'default', '获取分页应用列表', 'get', 'opencloud-base-provider', '/app', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744662892544', 'com.github.lyd.base.provider.controller.BaseAppController.getAppClientInfo', '获取应用开发配置信息', 'default', '获取应用开发配置信息', 'get', 'opencloud-base-provider', '/app/client/{appId}/info', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744755167232', 'com.github.lyd.base.provider.controller.BaseAppController.getApp', '获取应用详情', 'default', '仅限系统内部调用', 'get', 'opencloud-base-provider', '/app/{appId}/info', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744855830528', 'com.github.lyd.base.provider.controller.BaseAppController.addApp', '添加应用信息', 'default', '添加应用信息', 'post', 'opencloud-base-provider', '/app/add', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160744990048256', 'com.github.lyd.base.provider.controller.BaseAuthorityController.grantUserAuthority', '分配用户权限', 'default', '分配用户权限', 'post', 'opencloud-base-provider', '/authority/grant/user', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745107488768', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedRoleAuthority', '获取角色已分配权限', 'default', '获取角色已分配权限', 'get', 'opencloud-base-provider', '/authority/granted/role', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745237512192', 'com.github.lyd.base.provider.controller.BaseAuthorityController.grantRoleAuthority', '分配角色权限', 'default', '分配角色权限', 'post', 'opencloud-base-provider', '/authority/grant/role', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745333981184', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedUserAuthority', '获取用户已分配权限', 'default', '获取用户已分配权限', 'get', 'opencloud-base-provider', '/authority/granted/user', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745430450176', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedAppAuthority', '获取应用已分配接口权限', 'default', '获取应用已分配接口权限', 'get', 'opencloud-base-provider', '/authority/granted/app', '0', '1', '2019-03-26 17:58:48', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745543696384', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getAccessAuthorityList', '获取所有访问权限列表', 'default', '获取所有访问权限列表', 'get', 'opencloud-base-provider', '/authority/access/list', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:26', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745656942592', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getMenuAuthorityList', '获取菜单权限列表', 'default', '获取菜单权限列表', 'get', 'opencloud-base-provider', '/authority/menu/list', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745770188800', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getApiAuthorityList', '获取接口权限列表', 'default', '获取接口权限列表', 'get', 'opencloud-base-provider', '/authority/api/list', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160745858269184', 'com.github.lyd.base.provider.controller.BaseAuthorityController.grantAppAuthority', '分配应用权限', 'default', '分配应用权限', 'post', 'opencloud-base-provider', '/authority/grant/app', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746088955904', 'com.github.lyd.base.provider.controller.BaseAuthorityController.getGrantedMyMenuAuthority', '获取登陆用户已分配权限', 'default', '获取登陆用户已分配权限', 'get', 'opencloud-base-provider', '/authority/granted/me/menu', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746185424896', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenuOperationList', '获取菜单下所有操作', 'default', '获取菜单下所有操作', 'get', 'opencloud-base-provider', '/menu/operation', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746332225536', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenuAllList', '菜单所有资源列表', 'default', '菜单所有资源列表', 'get', 'opencloud-base-provider', '/menu/all', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746437083136', 'com.github.lyd.base.provider.controller.BaseMenuController.removeMenu', '移除菜单资源', 'default', '移除菜单资源', 'post', 'opencloud-base-provider', '/menu/remove', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746525163520', 'com.github.lyd.base.provider.controller.BaseMenuController.updateMenu', '编辑菜单资源', 'default', '编辑菜单资源', 'post', 'opencloud-base-provider', '/menu/update', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746617438208', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenu', '获取菜单资源详情', 'default', '获取菜单资源详情', 'get', 'opencloud-base-provider', '/menu/{menuId}/info', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746705518592', 'com.github.lyd.base.provider.controller.BaseMenuController.addMenu', '添加菜单资源', 'default', '添加菜单资源', 'post', 'opencloud-base-provider', '/menu/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746797793280', 'com.github.lyd.base.provider.controller.BaseMenuController.getMenuListPage', '获取分页菜单资源列表', 'default', '获取分页菜单资源列表', 'get', 'opencloud-base-provider', '/menu', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746894262272', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperationAllList', '获取菜单下所有操作', 'default', '获取菜单下所有操作', 'get', 'opencloud-base-provider', '/operation/menu', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160746986536960', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperationListPage', '获取分页操作列表', 'default', '获取分页操作列表', 'get', 'opencloud-base-provider', '/operation', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747074617344', 'com.github.lyd.base.provider.controller.BaseOperationController.addOperation', '添加操作资源', 'default', '添加操作资源', 'post', 'opencloud-base-provider', '/operation/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747162697728', 'com.github.lyd.base.provider.controller.BaseOperationController.addOperationApi', '操作资源绑定API', 'default', '操作资源绑定API', 'post', 'opencloud-base-provider', '/operation/api/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747305304064', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperation', '获取操作资源详情', 'default', '获取操作资源详情', 'get', 'opencloud-base-provider', '/operation/{operationId}/info', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747389190144', 'com.github.lyd.base.provider.controller.BaseOperationController.getOperationApi', '获取操作资源已绑定API', 'default', '获取操作资源已绑定API', 'get', 'opencloud-base-provider', '/operation/api', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747481464832', 'com.github.lyd.base.provider.controller.BaseOperationController.removeOperation', '移除操作资源', 'default', '移除操作资源', 'post', 'opencloud-base-provider', '/operation/remove', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747653431296', 'com.github.lyd.base.provider.controller.BaseOperationController.updateOperation', '编辑操作资源', 'default', '添加操作资源', 'post', 'opencloud-base-provider', '/operation/update', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747741511680', 'com.github.lyd.base.provider.controller.BaseRoleController.getRole', '获取角色详情', 'default', '获取角色详情', 'get', 'opencloud-base-provider', '/role/{roleId}/info', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747829592064', 'com.github.lyd.base.provider.controller.BaseRoleController.addRole', '添加角色', 'default', '添加角色', 'post', 'opencloud-base-provider', '/role/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160747917672448', 'com.github.lyd.base.provider.controller.BaseRoleController.getRoleUsers', '查询角色成员', 'default', '查询角色成员', 'get', 'opencloud-base-provider', '/role/users', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748014141440', 'com.github.lyd.base.provider.controller.BaseRoleController.updateRole', '编辑角色', 'default', '编辑角色', 'post', 'opencloud-base-provider', '/role/update', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748114804736', 'com.github.lyd.base.provider.controller.BaseRoleController.addUserRoles', '角色添加成员', 'default', '角色添加成员', 'post', 'opencloud-base-provider', '/role/users/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748198690816', 'com.github.lyd.base.provider.controller.BaseRoleController.getRoleListPage', '获取分页角色列表', 'default', '获取分页角色列表', 'get', 'opencloud-base-provider', '/role', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748282576896', 'com.github.lyd.base.provider.controller.BaseRoleController.getRoleAllList', '获取所有角色列表', 'default', '获取所有角色列表', 'get', 'opencloud-base-provider', '/role/all', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748370657280', 'com.github.lyd.base.provider.controller.BaseRoleController.removeRole', '删除角色', 'default', '删除角色', 'post', 'opencloud-base-provider', '/role/remove', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748462931968', 'com.github.lyd.base.provider.controller.BaseUserController.updateUser', '更新系统用户', 'default', '更新系统用户', 'post', 'opencloud-base-provider', '/user/update', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748555206656', 'com.github.lyd.base.provider.controller.BaseUserController.updatePassword', '修改用户密码', 'default', '修改用户密码', 'post', 'opencloud-base-provider', '/user/update/password', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748643287040', 'com.github.lyd.base.provider.controller.BaseUserController.addUser', '添加系统用户', 'default', '添加系统用户', 'post', 'opencloud-base-provider', '/user/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748727173120', 'com.github.lyd.base.provider.controller.BaseUserController.getUserRoles', '获取用户已分配角色', 'default', '获取用户已分配角色', 'get', 'opencloud-base-provider', '/user/roles', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748815253504', 'com.github.lyd.base.provider.controller.BaseUserController.addUserRoles', '用户分配角色', 'default', '用户分配角色', 'post', 'opencloud-base-provider', '/user/roles/add', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748903333888', 'com.github.lyd.base.provider.controller.BaseUserController.getUserAllList', '获取所有用户列表', 'default', '获取所有用户列表', 'get', 'opencloud-base-provider', '/user/all', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160748987219968', 'com.github.lyd.base.provider.controller.BaseUserController.restMyPassword', '修改当前登录用户密码', 'default', '修改当前登录用户密码', 'get', 'opencloud-base-provider', '/user/me/rest/password', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160749071106048', 'com.github.lyd.base.provider.controller.BaseUserController.getUserList', '系统分页用户列表', 'default', '系统分页用户列表', 'get', 'opencloud-base-provider', '/user', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160749159186432', 'com.github.lyd.base.provider.controller.BaseUserController.updateMyUserInfo', '修改当前登录用户基本信息', 'default', '修改当前登录用户基本信息', 'post', 'opencloud-base-provider', '/user/me/update', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160749234683904', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController.registerThirdPartyAccount', '注册第三方登录账号', 'default', '仅限系统内部调用', 'post', 'opencloud-base-provider', '/account/register/thirdParty', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160749297598464', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController.resetPassword', '重置密码', 'default', '重置密码', 'post', 'opencloud-base-provider', '/account/reset/password', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160749398261760', 'com.github.lyd.base.provider.controller.BaseUserUserAccountController.localLogin', '获取账号登录信息', 'default', '仅限系统内部调用', 'post', 'opencloud-base-provider', '/account/localLogin', '0', '1', '2019-03-26 17:58:49', '2019-03-26 18:09:27', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160755526139904', 'com.github.lyd.auth.provider.controller.ApiController.getUserProfile', '获取用户基础信息', 'default', '获取用户基础信息', 'get', 'opencloud-auth-provider', '/user/me', '0', '1', '2019-03-26 17:58:51', '2019-03-26 17:58:51', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160755601637376', 'com.github.lyd.auth.provider.controller.ApiController.getLoginOtherConfig', '获取第三方登录配置', 'default', '任何人都可访问', 'get', 'opencloud-auth-provider', '/login/other/config', '0', '1', '2019-03-26 17:58:51', '2019-03-26 17:58:51', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160760165040128', 'com.github.lyd.admin.provider.controller.AdminController.sign', '内部应用请求签名', 'default', '仅限系统内部调用', 'post', 'opencloud-admin-provider', '/sign', '0', '1', '2019-03-26 17:58:52', '2019-03-26 17:58:52', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160760248926208', 'com.github.lyd.admin.provider.controller.AdminController.getLoginToken', '获取用户访问令牌', 'default', '基于oauth2密码模式登录,无需签名,返回access_token', 'post', 'opencloud-admin-provider', '/login/token', '0', '1', '2019-03-26 17:58:52', '2019-03-26 17:58:52', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160815638904832', 'com.github.lyd.gateway.provider.controller.GatewayAccessLogsController.getAccessLogListPage', '获取分页访问日志列表', 'default', '获取分页访问日志列表', 'get', 'opencloud-gateway-provider', '/gateway/access/logs', '0', '1', '2019-03-26 17:59:05', '2019-03-26 17:59:05', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160815911534592', 'com.github.lyd.gateway.provider.controller.GatewayController.getServiceList', '获取服务列表', 'default', '获取服务列表', 'get', 'opencloud-gateway-provider', '/service/list', '0', '1', '2019-03-26 17:59:05', '2019-03-26 17:59:05', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160816150609920', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.removeIpLimit', '移除IP限制', 'default', '移除IP限制', 'post', 'opencloud-gateway-provider', '/gateway/limit/ip/remove', '0', '1', '2019-03-26 17:59:05', '2019-03-26 17:59:05', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160816750395392', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.addIpLimitApis', '绑定API', 'default', '一个API只能绑定一个策略', 'post', 'opencloud-gateway-provider', '/gateway/limit/ip/api/add', '0', '1', '2019-03-26 17:59:05', '2019-03-26 17:59:05', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160816846864384', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.getIpLimit', '获取IP限制', 'default', '获取IP限制', 'get', 'opencloud-gateway-provider', '/gateway/limit/ip/{policyId}/info', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160816955916288', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.updateIpLimit', '编辑IP限制', 'default', '编辑IP限制', 'post', 'opencloud-gateway-provider', '/gateway/limit/ip/update', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160817056579584', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.addIpLimit', '添加IP限制', 'default', '添加IP限制', 'post', 'opencloud-gateway-provider', '/gateway/limit/ip/add', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160817228546048', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.getIpLimitListPage', '获取分页接口列表', 'default', '获取分页接口列表', 'get', 'opencloud-gateway-provider', '/gateway/limit/ip', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160817404706816', 'com.github.lyd.gateway.provider.controller.GatewayIpLimitController.getIpLimitApiList', '查询策略已绑定API列表', 'default', '获取分页接口列表', 'get', 'opencloud-gateway-provider', '/gateway/limit/ip/api/list', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160817656365056', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.addRateLimit', '添加流量控制', 'default', '添加流量控制', 'post', 'opencloud-gateway-provider', '/gateway/limit/rate/add', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160818025463808', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.removeRateLimit', '移除流量控制', 'default', '移除流量控制', 'post', 'opencloud-gateway-provider', '/gateway/limit/rate/remove', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160818344230912', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.addRateLimitApis', '绑定API', 'default', '一个API只能绑定一个策略', 'post', 'opencloud-gateway-provider', '/gateway/limit/rate/api/add', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160818528780288', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.getRateLimit', '获取流量控制', 'default', '获取流量控制', 'get', 'opencloud-gateway-provider', '/gateway/limit/rate/{policyId}/info', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160818751078400', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.updateRateLimit', '编辑流量控制', 'default', '编辑流量控制', 'post', 'opencloud-gateway-provider', '/gateway/limit/rate/update', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160818893684736', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.getRateLimitListPage', '获取分页接口列表', 'default', '获取分页接口列表', 'get', 'opencloud-gateway-provider', '/gateway/limit/rate', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160819174703104', 'com.github.lyd.gateway.provider.controller.GatewayRateLimitController.getRateLimitApiList', '查询策略已绑定API列表', 'default', '获取分页接口列表', 'get', 'opencloud-gateway-provider', '/gateway/limit/rate/api/list', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160819325698048', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.removeRoute', '移除路由', 'default', '移除路由', 'post', 'opencloud-gateway-provider', '/gateway/route/remove', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160819602522112', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.addRoute', '添加路由', 'default', '添加路由', 'post', 'opencloud-gateway-provider', '/gateway/route/add', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160819803848704', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.updateRoute', '编辑路由', 'default', '编辑路由', 'post', 'opencloud-gateway-provider', '/gateway/route/update', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160820063895552', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.getRouteListPage', '获取分页路由列表', 'default', '获取分页路由列表', 'get', 'opencloud-gateway-provider', '/gateway/route', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');
INSERT INTO `base_resource_api` VALUES ('560160820395245568', 'com.github.lyd.gateway.provider.controller.GatewayRouteController.getRoute', '获取路由', 'default', '获取路由', 'get', 'opencloud-gateway-provider', '/gateway/route/{routeId}/info', '0', '1', '2019-03-26 17:59:06', '2019-03-26 17:59:06', '1', '0', '1');

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
                                    `icon` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '菜单标题',
                                    `target` varchar(20) COLLATE utf8_bin NOT NULL DEFAULT '_self' COMMENT '打开方式:_self窗口内,_blank新窗口',
                                    `priority` bigint(20) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
                                    `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
                                    `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名',
                                    PRIMARY KEY (`menu_id`),
                                    UNIQUE KEY `menu_code` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-菜单信息';

-- ----------------------------
-- Records of base_resource_menu
-- ----------------------------
INSERT INTO `base_resource_menu` VALUES ('1', '0', 'system', '系统管理', '系统管理', '/', '', 'md-folder', '_self', '1', '1', '2018-07-29 21:20:10', '2019-03-18 04:44:04', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('2', '13', 'gatewayIpLimit', 'IP访问控制', 'iP访问控制,白名单、黑名单', '/', 'gateway/ip-limit/index', 'md-document', '_self', '1', '1', '2018-07-29 21:20:13', '2019-03-13 21:48:21', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('3', '1', 'systemMenu', '菜单资源', '菜单资源', '/', 'system/menus/index', 'md-menu', '_self', '3', '1', '2018-07-29 21:20:13', '2019-03-18 04:39:10', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('4', '0', 'systemMonitor', '服务管理', '服务管理', '/', '', 'md-folder', '_self', '4', '1', '2018-07-29 21:20:13', '2019-03-18 04:44:13', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('5', '13', 'gatewayRoute', '智能路由', 'zuul动态路由', '/', 'gateway/route/index', 'md-document', '_self', '5', '1', '2018-07-29 21:20:13', '2019-02-25 00:15:23', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('6', '13', 'systemApi', 'API列表', 'API接口资源', '/', 'system/api/index', 'md-document', '_self', '0', '1', '2018-07-29 21:20:13', '2019-03-13 21:48:12', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('7', '4', 'gatewayTrace', '服务追踪', '服务追踪', 'http://', 'localhost:7080', 'md-document', '_blank', '7', '1', '2018-11-30 02:11:18', '2019-03-16 03:56:36', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('8', '1', 'systemRolei', '角色信息', '角色信息', '/', 'system/role/index', 'md-people', '_self', '8', '1', '2018-12-27 15:26:54', '2019-03-18 04:39:23', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('9', '1', 'systemApp', '应用信息', '应用信息', '/', 'system/app/index', 'md-apps', '_self', '0', '1', '2018-12-27 15:41:52', '2019-03-18 04:34:10', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('10', '1', 'systemUser', '系统用户', '系统用户', '/', 'system/user/index', 'md-person', '_self', '0', '1', '2018-12-27 15:46:29', '2019-03-18 04:40:07', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('11', '13', 'apiDebug', '接口调试', 'swagger接口调试', 'http://', 'localhost:8888', 'md-document', '_self', '0', '1', '2019-01-10 20:47:19', '2019-02-25 00:27:27', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('12', '13', 'gatewayLogs', '访问日志', '', '/', 'gateway/logs/index', 'md-document', '_self', '0', '1', '2019-01-28 02:37:42', '2019-02-25 00:16:40', '1', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('13', '0', 'gateway', 'API网关', 'API网关', '/', '', 'md-folder', '_self', '0', '1', '2019-02-25 00:15:09', '2019-03-18 04:44:20', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('14', '0', 'help', '帮助文档', '帮助文档', '/', '', 'md-folder', '_self', '0', '1', '2019-02-25 00:26:44', '2019-03-18 04:45:04', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('15', '14', 'wiki', '使用手册', 'wiki', 'https://', 'gitee.com/liuyadu/open-cloud/wikis/pages', 'md-document', '_blank', '0', '1', '2019-02-25 01:02:44', '2019-03-13 23:02:49', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('555410979983196160', '4', 'nacos', '服务与配置', '服务与配置', 'http://', 'localhost:8848/nacos/index.html', 'md-pulse', '_blank', '0', '1', '2019-03-13 15:24:56', '2019-03-18 04:42:58', '0', 'opencloud-base-provider');
INSERT INTO `base_resource_menu` VALUES ('555507213116899328', '13', 'gatewayRateLimit', '流量控制', 'API限流', '/', 'gateway/rate-limit/index', 'md-document', '_self', '666', '1', '2019-03-13 21:47:20', '2019-03-13 22:13:10', '0', 'opencloud-base-provider');

-- ----------------------------
-- Table structure for base_resource_operation
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_operation`;
CREATE TABLE `base_resource_operation` (
                                         `operation_id` bigint(20) NOT NULL COMMENT '资源ID',
                                         `operation_code` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源编码',
                                         `operation_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '资源名称',
                                         `operation_desc` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '资源描述',
                                         `menu_id` bigint(20) DEFAULT NULL COMMENT '资源父节点',
                                         `priority` int(10) NOT NULL DEFAULT '0' COMMENT '优先级 越小越靠前',
                                         `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
                                         `create_time` datetime NOT NULL,
                                         `update_time` datetime DEFAULT NULL,
                                         `is_persist` tinyint(3) NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
                                         `api_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '绑定的API资源:多个用,号隔开',
                                         `service_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '服务名称',
                                         PRIMARY KEY (`operation_id`),
                                         UNIQUE KEY `operation_code` (`operation_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='系统资源-功能操作';

-- ----------------------------
-- Records of base_resource_operation
-- ----------------------------
INSERT INTO `base_resource_operation` VALUES ('555416595535495168', 'systemBrowse', '浏览', '查看列表', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596017840128', 'systemCreate', '创建', '新增数据', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596101726208', 'systemEdit', '编辑', '编辑数据', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596168835072', 'systemRemove', '删除', '删除数据', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596252721152', 'systemDetail', '详情', '查看详情', '1', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596340801536', 'systemAuthBrowse', '浏览', '查看列表', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596449853440', 'systemAuthCreate', '创建', '新增数据', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596529545216', 'systemAuthEdit', '编辑', '编辑数据', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596600848384', 'systemAuthRemove', '删除', '删除数据', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596688928768', 'systemAuthDetail', '详情', '查看详情', '2', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596760231936', 'systemMenuBrowse', '浏览', '查看列表', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-18 01:20:08', '1', '2,555567985696178176,555567986031722496,555567986245632000', 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596877672448', 'systemMenuCreate', '创建', '新增数据', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416596986724352', 'systemMenuEdit', '编辑', '编辑数据', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597179662336', 'systemMenuRemove', '删除', '删除数据', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597393571840', 'systemMenuDetail', '详情', '查看详情', '3', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597490040832', 'systemMonitorBrowse', '浏览', '查看列表', '4', '0', '1', '2019-03-13 15:47:15', '2019-03-13 15:47:15', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597565538304', 'systemMonitorCreate', '创建', '新增数据', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597607481344', 'systemMonitorEdit', '编辑', '编辑数据', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597666201600', 'systemMonitorRemove', '删除', '删除数据', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597712338944', 'systemMonitorDetail', '详情', '查看详情', '4', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597775253504', 'gatewayRouteBrowse', '浏览', '查看列表', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597821390848', 'gatewayRouteCreate', '创建', '新增数据', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597875916800', 'gatewayRouteEdit', '编辑', '编辑数据', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597943025664', 'gatewayRouteRemove', '删除', '删除数据', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416597989163008', 'gatewayRouteDetail', '详情', '查看详情', '5', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598035300352', 'systemApiBrowse', '浏览', '查看列表', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598089826304', 'systemApiCreate', '创建', '新增数据', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598127575040', 'systemApiEdit', '编辑', '编辑数据', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598194683904', 'systemApiRemove', '删除', '删除数据', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598236626944', 'systemApiDetail', '详情', '查看详情', '6', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598282764288', 'gatewayTraceBrowse', '浏览', '查看列表', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598328901632', 'gatewayTraceCreate', '创建', '新增数据', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598370844672', 'gatewayTraceEdit', '编辑', '编辑数据', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598412787712', 'gatewayTraceRemove', '删除', '删除数据', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598458925056', 'gatewayTraceDetail', '详情', '查看详情', '7', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598505062400', 'systemRoleBrowse', '浏览', '查看列表', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598547005440', 'systemRoleCreate', '创建', '新增数据', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598588948480', 'systemRoleEdit', '编辑', '编辑数据', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598635085824', 'systemRoleRemove', '删除', '删除数据', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598677028864', 'systemRoleDetail', '详情', '查看详情', '8', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598706388992', 'systemAppBrowse', '浏览', '查看列表', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598802857984', 'systemAppCreate', '创建', '新增数据', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598836412416', 'systemAppEdit', '编辑', '编辑数据', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598874161152', 'systemAppRemove', '删除', '删除数据', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598924492800', 'systemAppDetail', '详情', '查看详情', '9', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598953852928', 'systemUserBrowse', '浏览', '查看列表', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416598979018752', 'systemUserCreate', '创建', '新增数据', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599016767488', 'systemUserEdit', '编辑', '编辑数据', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599046127616', 'systemUserRemove', '删除', '删除数据', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599079682048', 'systemUserDetail', '详情', '查看详情', '10', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599109042176', 'apiDebugBrowse', '浏览', '查看列表', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599142596608', 'apiDebugCreate', '创建', '新增数据', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599176151040', 'apiDebugEdit', '编辑', '编辑数据', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599209705472', 'apiDebugRemove', '删除', '删除数据', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599243259904', 'apiDebugDetail', '详情', '查看详情', '11', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599281008640', 'gatewayLogsBrowse', '浏览', '查看列表', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599310368768', 'gatewayLogsCreate', '创建', '新增数据', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599343923200', 'gatewayLogsEdit', '编辑', '编辑数据', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599381671936', 'gatewayLogsRemove', '删除', '删除数据', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599415226368', 'gatewayLogsDetail', '详情', '查看详情', '12', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599444586496', 'gatewayBrowse', '浏览', '查看列表', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599473946624', 'gatewayCreate', '创建', '新增数据', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599499112448', 'gatewayEdit', '编辑', '编辑数据', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599532666880', 'gatewayRemove', '删除', '删除数据', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599562027008', 'gatewayDetail', '详情', '查看详情', '13', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599599775744', 'helpBrowse', '浏览', '查看列表', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599633330176', 'helpCreate', '创建', '新增数据', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599666884608', 'helpEdit', '编辑', '编辑数据', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599704633344', 'helpRemove', '删除', '删除数据', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599742382080', 'helpDetail', '详情', '查看详情', '14', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599771742208', 'wikiBrowse', '浏览', '查看列表', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599796908032', 'wikiCreate', '创建', '新增数据', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599826268160', 'wikiEdit', '编辑', '编辑数据', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599918542848', 'wikiRemove', '删除', '删除数据', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599952097280', 'wikiDetail', '详情', '查看详情', '15', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416599985651712', 'nacosBrowse', '浏览', '查看列表', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600023400448', 'nacosCreate', '创建', '新增数据', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600065343488', 'nacosEdit', '编辑', '编辑数据', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600103092224', 'nacosRemove', '删除', '删除数据', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555416600145035264', 'nacosDetail', '详情', '查看详情', '555410979983196160', '0', '1', '2019-03-13 15:47:16', '2019-03-13 15:47:16', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213175619584', 'gatewayRateLimitBrowse', '浏览', '查看列表', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213246922752', 'gatewayRateLimitCreate', '创建', '新增数据', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213305643008', 'gatewayRateLimitEdit', '编辑', '编辑数据', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213364363264', 'gatewayRateLimitRemove', '删除', '删除数据', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');
INSERT INTO `base_resource_operation` VALUES ('555507213423083520', 'gatewayRateLimitDetail', '详情', '查看详情', '555507213116899328', '0', '1', '2019-03-13 21:47:20', '2019-03-13 21:47:20', '1', null, 'opencloud-base-provider');

-- ----------------------------
-- Table structure for base_resource_operation_api
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_operation_api`;
CREATE TABLE `base_resource_operation_api` (
                                             `operation_id` bigint(20) NOT NULL COMMENT '操作ID',
                                             `api_id` bigint(20) NOT NULL COMMENT 'API',
                                             KEY `operation_id` (`operation_id`),
                                             KEY `api_id` (`api_id`),
                                             CONSTRAINT `base_resource_operation_api_ibfk_2` FOREIGN KEY (`api_id`) REFERENCES `base_resource_api` (`api_id`),
                                             CONSTRAINT `base_resource_operation_api_ibfk_1` FOREIGN KEY (`operation_id`) REFERENCES `base_resource_operation` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统操作资源-API关联表';

-- ----------------------------
-- Records of base_resource_operation_api
-- ----------------------------
INSERT INTO `base_resource_operation_api` VALUES ('555416596760231936', '2');
INSERT INTO `base_resource_operation_api` VALUES ('555416596760231936', '555567985696178176');

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
INSERT INTO `base_role` VALUES ('1', 'admin', '系统管理员', '1', '系统管理员', '2018-07-29 21:14:54', '2019-02-13 23:09:46', '1');
INSERT INTO `base_role` VALUES ('2', 'dev', '自研开发者', '1', '第三方开发者', '2018-07-29 21:14:54', '2019-02-19 22:44:21', '1');
INSERT INTO `base_role` VALUES ('3', 'isp', '服务提供商', '1', '第三方开发者', '2018-07-29 21:14:54', '2019-01-29 01:19:46', '1');

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
INSERT INTO `base_user` VALUES ('521677655146233856', 'admin', '超级管理员', '', '515608851@qq.com', '18518226890', 'platform', null, null, '2018-12-10 13:20:45', '1', '拥有所有权限,不受任何限制.仅限admin账号。', '2018-12-10 13:20:45', '2018-12-10 13:20:45');
INSERT INTO `base_user` VALUES ('557063237640650752', 'test', '测试用户', '', '', '', 'platform', null, null, '2019-03-18 04:50:25', '1', '', '2019-03-18 04:50:25', '2019-03-18 04:50:25');

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
INSERT INTO `base_user_account` VALUES ('557063237787451392', '557063237640650752', 'test', '$2a$10$V29RaVpl/zQepgdJXHoqUe2o8Rvrwuu2Xn7lRoXQPxRk4gfC5YwBy', 'username');

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
SET FOREIGN_KEY_CHECKS=1;
