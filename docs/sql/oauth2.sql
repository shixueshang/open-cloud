
-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2访问令牌';

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) DEFAULT NULL,
  `clientId` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expiresAt` datetime DEFAULT NULL,
  `lastModifiedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2已授权客户端';

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `scope` varchar(1024) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(2048) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端信息';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('1552274783265', '$2a$10$xxfI6N5kSKINJXipQb9dJuS1Z7T2z4h/IZrhNnx3zNzCViCQMDtfq', '', 'userProfile', 'refresh_token,password,client_credentials,authorization_code', 'http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html', 'actuator', '43200', '604800', '{\"website\":\"http://www.baidu.com\",\"appName\":\"开发平台资源服务器\",\"updateTime\":1547130166355,\"userId\":0,\"appOs\":\"\",\"redirectUrls\":\"http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html\",\"appIcon\":\"\",\"appType\":\"server\",\"appDesc\":\"开放云平台\",\"appId\":\"gateway\",\"appNameEn\":\"ApiGateway\",\"userType\":\"platform\",\"status\":1}', '');
INSERT INTO `oauth_client_details` VALUES ('1552294656514', '$2a$10$UAzdXTnT9DAyfzSNInoX4.bt/8V0zdn23m7uQiwsyorHLucf4ftfO', '', 'userProfile', 'refresh_token,password,client_credentials,authorization_code', 'http://localhost:8080/login/callback', '', '43200', '604800', '{\"website\":\"http://www.baidu.com\",\"appName\":\"开放平台管理后台\",\"updateTime\":1547130166355,\"userId\":0,\"appOs\":\"\",\"redirectUrls\":\"http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html\",\"appIcon\":\"\",\"appType\":\"pc\",\"appDesc\":\"开放平台管理后台\",\"appId\":\"gateway\",\"appNameEn\":\"ApiGateway\",\"userType\":\"platform\",\"status\":1}', '');


-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端令牌';

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2授权码';

-- ----------------------------
-- Records of oauth_code
-- ----------------------------

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2刷新令牌';

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
SET FOREIGN_KEY_CHECKS=1;