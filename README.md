<p align="center">
  <a target="_blank" href="https://nacos.io/en-us/"><img src="https://img.shields.io/badge/Nacos-0.2.1-blue.svg" alt="Nacos"></a>
  <a><img src="https://img.shields.io/badge/Spring%20Cloud-%20Finchley.SR2-brightgreen.svg" alt="SpringCloud"></a>
  <a><img src="https://img.shields.io/badge/Spring%20Boot-2.0.8-brightgreen.svg" alt="SpringBoot"></a>
  <a><img src="https://img.shields.io/badge/Redis-orange.svg"></a>
  <a><img src="https://img.shields.io/badge/RabbitMq-orange.svg"></a>
  <a target="_blank" href="https://www.iviewui.com/docs/guide/install"><img src="https://img.shields.io/badge/iview-3.1.3-brightgreen.svg?style=flat-square" alt="iview"></a>
  <a><img src="https://img.shields.io/badge/vue-2.5.10-brightgreen.svg?style=flat-square" alt="vue"></a>
  <a><img src="https://img.shields.io/npm/l/express.svg" alt="License"></a>
</p>  

# 微服务开放平台

---
#### 简介
搭建基于OAuth2的开放平台、为APP端提供统一接口管控平台、为第三方合作伙伴的业务对接提供授信可控的技术对接平台.
+ 统一API网关、访问鉴权、参数验签、外部调用更安全.
+ 分布式架构,基于服务发现,Fegin(伪RPC)方式内部调用,更便捷.
+ 深度整合SpringCloud+SpringSecurity+Oauth2,更细粒度、灵活的ABAC权限控制.
+ 前后端分离方式开发应用，分工合作更高效!
+ 代码合理封装、简单易懂、   

<a target="_blank" href="http://39.106.187.125/admin">在线访问</a>
  
默认登录账号:admin 123456
测试登录账号:test 123456

#### 代码仓库

##### 请随手给个Star! 感谢支持！

<a target="_blank" href="https://gitee.com/liuyadu">服务端源码-码云</a>  

<a target="_blank" href="https://github.com/liuyadu/">服务端源码-Github</a>  

<a target="_blank" href="https://gitee.com/liuyadu/open-admin-ui">前端ui源码</a>

<a target="_blank" href="https://gitee.com/liuyadu/open-cloud/wikis/pages">使用手册</a>  


#### 代码结构
``` lua
open-cloud
├── docs
    ├── bin           -- 执行脚本  
    ├── config        -- 公共配置,用于导入到nacos配置中心   
    ├── generator     -- mapper生成器  
    ├── sql           -- sql文件
    
├── opencloud-common  -- 公共类和jar包依赖
    ├── opencloud-common-core    -- 提供微服务相关依赖包、工具类、全局异常解析等...
    ├── opencloud-common-starter -- SpringBoot自动扫描
    
├── opencloud-gateway  -- API网关模块
    ├── opencloud-gateway-client    -- API网关接口
    ├── opencloud-gateway-provider  -- API网关(port = 8888)  
    
├── opencloud-upms    --  通用权限模块
    ├── opencloud-base-client    -- 平台基础服务接口
    ├── opencloud-base-provider  -- 平台基础服务(port = 8233)  
    ├── opencloud-auth-client    -- 平台认证服务接口
    ├── opencloud-auth-provider  -- 平台认证服务(port = 8211)  
    
├── opencloud-app    -- 应用服务模块
    ├── opencloud-admin-provider  -- 运营后台服务(port = 8301)  

├── opencloud-msg     -- 公共消息模块 
    ├── opencloud-msg-client    -- 消息服务接口
    ├── opencloud-msg-provider  -- 消息服务(port = 8266)  
    
├── opencloud-bpm     -- 公共工作流模块...  
    ├── opencloud-bpm-client   -- 工作流接口
    ├── opencloud-bpm-provider -- 工作流服务(port = 8255)
    
├── opencloud-zipkin  -- 链路追踪 
```
#### 系统结构图

![springcloud](/docs/springcloud.jpg)  

#### 数据模型

##### 基础权限模型  

![base](/docs/base.png)  

##### 网关访问限制模型  

![gateway](/docs/gateway.png)  

#### 快速开始
上手难度：★★★

本项目基于springCloud打造的分布式快速开发框架. 需要了解SpringCloud,SpringBoot开发,分布式原理。

#### 注：Nacos版本选择V0.9.0以下版本. 1.0.0以上版本暂未测试!

1. 准备环境
    + Java1.8
    + 阿里巴巴Nacos服务发现和注册中心 <a href="https://nacos.io/zh-cn/">nacos.io</a>
    + Redis
    + RabbitMq
    + Mysql
    + Maven
    + Nodejs
   
2. 导入sql脚本
    + docs/sql/oauth2.sql
    + docs/sql/base.sql
    + docs/sql/gateway.sql
    + docs/sql/zipkin.sql
    
3. 导入Nacos公共配置
    + 访问 http://localhost:8848/nacos/index.html 
    + 新建配置 
        + docs/config/db.properties  > db.properties
        + docs/config/rabbitmq.properties > rabbitmq.properties
        + docs/config/redis.properties > redis.properties
        + docs/config/common.properties  > common.properties
        
4. 修改主pom.xml  

    初始化
    ``` bush
        maven clean install
    ```
    本地启动,默认不用修改
    ``` xml
        <!--Nacos配置中心地址-->
        <config.server-addr>127.0.0.1:8848</config.server-addr>
        <!--Nacos配置中心命名空间,用于支持多环境.这里必须使用ID，不能使用名称,默认为空-->
        <config.namespace></config.namespace>
        <!--Nacos服务发现地址-->
        <discovery.server-addr>127.0.0.1:8848</discovery.server-addr>
    ```
    
5. 本地启动
     + AuthApplication
     + BaseApplication
     + GatewayApplication
     + AdminApplication   
   4个服务启动成功后。就可以依赖这些服务进行微服务开发了。  
   访问 http://localhost:8888
     
6. 前端启动
    ```bush
        npm install 
        npm run dev
    ``` 
    访问 http://localhost:8080
    
7. 项目打包部署  

     maven多环境打包
   ```bush
     mvn clean install package -P {dev|test|online}
   ```
    项目启动
    ```bush
    ./docs/bin/startup.sh {start|stop|restart|status} open-base-provider.jar
    ./docs/bin/startup.sh {start|stop|restart|status} open-auth-provider.jar
    ./docs/bin/startup.sh {start|stop|restart|status} open-gateway-provider.jar
    ./docs/bin/startup.sh {start|stop|restart|status} open-admin-provider.jar
    ```
    
#### 第三方接口调用 
   
#### 更新日志
    v-1.0.0 2019-03-18
        1. 重构项目结构
        2. 重构表结构
        3. 重构授权逻辑
        4. 提取公共配置,并迁移到Nacos配置中心
        5. 优化功能
#### 问题反馈 
交流群:760809808  <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=b45f53bc72df5935af588df50a0f651285020356d1daa05f90ee3fb95a0607c9"><img  border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="open-cloud学习交流群" title="open-cloud学习交流群"></a>  
![760809808](/docs/1548831206525.png)  

