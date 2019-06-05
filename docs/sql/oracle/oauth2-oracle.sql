-- Create table
create table oauth_access_token
(
  token_id          varchar2(256),
  token             clob,
  authentication_id varchar2(128),
  user_name         varchar2(256),
  client_id         varchar2(256),
  authentication    clob,
  refresh_token     varchar2(256)
)
;
-- Add comments to the table
comment on table oauth_access_token
  is 'oauth2访问令牌';


-- Create table
create table oauth_approvals
(
  userid         varchar2(256),
  clientid       varchar2(256),
  scope          varchar2(256),
  status         varchar2(10),
  expiresat      timestamp,
  lastmodifiedat timestamp
)
;
-- Add comments to the table
comment on table oauth_approvals
  is 'oauth2已授权客户端';


-- Create table
create table OAUTH_CLIENT_DETAILS
(
  client_id               VARCHAR2(128) not null,
  resource_ids            VARCHAR2(256),
  client_secret           VARCHAR2(256),
  scope                   VARCHAR2(1024),
  authorized_grant_types  VARCHAR2(256),
  web_server_redirect_uri VARCHAR2(256),
  authorities             VARCHAR2(2048),
  access_token_validity   NUMBER(11),
  refresh_token_validity  NUMBER(11),
  additional_information  CLOB default '{}',
  autoapprove             VARCHAR2(256)
);
-- Add comments to the table
comment on table OAUTH_CLIENT_DETAILS
  is 'oauth2客户端信息';
-- Add comments to the columns
comment on column OAUTH_CLIENT_DETAILS.client_id
  is '应用标识';
comment on column OAUTH_CLIENT_DETAILS.resource_ids
  is '资源限定串(逗号分割)';
comment on column OAUTH_CLIENT_DETAILS.client_secret
  is '应用密钥(bcyt) 加密';
comment on column OAUTH_CLIENT_DETAILS.scope
  is '范围';
comment on column OAUTH_CLIENT_DETAILS.authorized_grant_types
  is '5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)';
comment on column OAUTH_CLIENT_DETAILS.web_server_redirect_uri
  is '回调地址';
comment on column OAUTH_CLIENT_DETAILS.authorities
  is '权限';
comment on column OAUTH_CLIENT_DETAILS.access_token_validity
  is 'access_token有效期';
comment on column OAUTH_CLIENT_DETAILS.refresh_token_validity
  is 'refresh_token有效期';
comment on column OAUTH_CLIENT_DETAILS.additional_information
  is '{}';
comment on column OAUTH_CLIENT_DETAILS.autoapprove
  is '是否自动授权 是-true';
-- Create/Recreate primary, unique and foreign key constraints
alter table OAUTH_CLIENT_DETAILS
  add constraint PK_OAUTH_CLIENT_DETAILS_CID primary key (CLIENT_ID);


INSERT INTO oauth_client_details VALUES ('1552274783265', '$2a$10$xxfI6N5kSKINJXipQb9dJuS1Z7T2z4h/IZrhNnx3zNzCViCQMDtfq', '', 'userProfile', 'authorization_code,client_credentials,password', 'http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html', '', '43200', '2592000', '{\"appIcon\":\"\",\"website\":\"http://www.baidu.com\",\"appName\":\"资源服务器\",\"appType\":\"server\",\"appDesc\":\"资源服务器\",\"appId\":\"1552274783265\",\"appNameEn\":\"ResourceServer\",\"updateTime\":1553011137731,\"userType\":\"platform\",\"userId\":521677655146233856,\"appOs\":\"\",\"status\":1}', '');
INSERT INTO oauth_client_details VALUES ('1552294656514', '$2a$10$UAzdXTnT9DAyfzSNInoX4.bt/8V0zdn23m7uQiwsyorHLucf4ftfO', '', 'userProfile', 'authorization_code,client_credentials,password', 'http://localhost:8888/oauth/admin/callback', '', '43200', '2592000', '{\"appIcon\":\"\",\"website\":\"http://www.baidu.com\",\"appName\":\"运营后台\",\"appType\":\"pc\",\"appDesc\":\"运营后台\",\"appId\":\"1552294656514\",\"appNameEn\":\"Admin\",\"updateTime\":1553011144275,\"userType\":\"platform\",\"userId\":521677655146233856,\"appOs\":\"\",\"status\":1}', '');


  -- Create table
create table OAUTH_CLIENT_TOKEN
(
  token_id          VARCHAR2(256),
  token             CLOB,
  authentication_id VARCHAR2(128) not null,
  user_name         VARCHAR2(256),
  client_id         VARCHAR2(256)
);
-- Add comments to the table
comment on table OAUTH_CLIENT_TOKEN
  is 'oauth2客户端令牌';
-- Create/Recreate primary, unique and foreign key constraints
alter table OAUTH_CLIENT_TOKEN
  add constraint PK_OAUTH_CLIENT_TOKEN_AUTHID primary key (AUTHENTICATION_ID);

-- Create table
create table oauth_code
(
  code           varchar2(256),
  authentication clob
)
;
-- Add comments to the table
comment on table oauth_code
  is 'oauth2授权码';



-- Create table
create table oauth_refresh_token
(
  token_id       varchar2(256),
  token          clob,
  authentication clob
)
;
-- Add comments to the table
comment on table oauth_refresh_token
  is 'oauth2刷新令牌';
