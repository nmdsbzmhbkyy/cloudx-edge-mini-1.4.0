CREATE TABLE `project_passcode_register_record`
(
    `id`            bigint      NOT NULL AUTO_INCREMENT,
    `isDeleted`    tinyint      DEFAULT NULL COMMENT '逻辑删除标识',
    `operator`      bigint      DEFAULT NULL COMMENT '操作人',
    `createTime`   datetime    DEFAULT NULL COMMENT '创建时间',
    `updateTime`   datetime    DEFAULT NULL COMMENT '更新时间',
    `projectId`    int      NOT NULL COMMENT '项目ID',
    `roomId`       varchar(32)  DEFAULT NULL COMMENT '房屋ID',
    `deviceId`     varchar(32)      DEFAULT NULL COMMENT '设备ID',
    `passenger`     varchar(128) DEFAULT NULL COMMENT '通行人员',
    `phone`         varchar(11)  DEFAULT NULL COMMENT '人员电话',
    `registerTime` bigint      NOT NULL COMMENT '注册时间',
    `expireTime`   bigint      NOT NULL COMMENT '过期时间',
    `passcode`      varchar(32) NOT NULL COMMENT '通行码',
    `registerType` tinyint     NOT NULL COMMENT '注册类型',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
CREATE TABLE `project_passcode_use_flow`
(
    `id`               bigint                                                 NOT NULL AUTO_INCREMENT,
    `isDeleted`       tinyint                                                 DEFAULT NULL COMMENT '逻辑删除标识',
    `operator`         bigint                                                 DEFAULT NULL COMMENT '操作人',
    `createTime`      datetime                                               DEFAULT NULL COMMENT '创建时间',
    `updateTime`      datetime                                               DEFAULT NULL COMMENT '更新时间',
    `projectId`       int                                                 DEFAULT NULL COMMENT '项目ID',
    `roomId`          varchar(32)                                             DEFAULT NULL COMMENT '房屋ID',
    `deviceId`        varchar(32)                                                 DEFAULT NULL COMMENT '设备ID',
    `passenger`        varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '通行人员',
    `phone`            varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL COMMENT '人员电话',
    `registerTime`    bigint                                                 NOT NULL COMMENT '注册时间',
    `expireTime`      bigint                                                 NOT NULL COMMENT '过期时间',
    `passcode`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通行码',
    `registerType`    tinyint                                                NOT NULL COMMENT '注册类型',
    `passTime`        bigint                                                 NOT NULL COMMENT '通行时间',
    `deviceInfoJson` varchar(1024)                                          NOT NULL COMMENT '设备信息JSON',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



-- auth: zouyu  用于存储第三方楼栋、房屋等信息，以及和平台的业务关联信息
CREATE TABLE `project_thirdparty_data`
(
    `seq`            int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增序列',
    `uuid`           varchar(32) NOT NULL COMMENT '自动生成的uuid',
    `thirdpartyUuid` varchar(32) NOT NULL COMMENT '第三方uuid',
    `type`           varchar(1)  NOT NULL COMMENT '类型 1楼栋',
    `associatedUuid` varchar(32) DEFAULT NULL COMMENT '关联的uuid',
    `data`           text        NOT NULL COMMENT '传入的数据',
    `projectId`      int         NOT NULL COMMENT '项目ID',
    `tenantId`       int         NOT NULL DEFAULT '1' COMMENT '租户ID',
    `createTime`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`     timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`seq`) USING BTREE,
    UNIQUE KEY `uidx_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COMMENT='存储第三方楼栋、房屋等信息，以及和平台的业务关联信息';

-- zn300x 相关设备 20230630
INSERT INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (24,NULL, 'GL-N006-多门门禁驱动', 'GL-N006', '3E99ZNZ01', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, '3E99ZNZ01','020320');
INSERT INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (25,NULL, 'GL8-Z10-单门门禁设备', 'GL8-Z10', 'TD004M1001', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, 'TD004M1001','020302');
INSERT INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (26,NULL, 'GL8-Z20-双门门禁设备', 'GL8-Z20', 'TD004M2001', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, 'TD004M2001','020302');
INSERT INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (27,NULL, 'GL8-Z40-四门门禁设备', 'GL8-Z40', 'TD004M4001', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, 'TD004M4001','020302');
INSERT INTO edge_iaas.middle_adaption (id,name,code,create_time,iot_manufacturer,app_id,project_id,api_url,device_server_url,jar_path,class_name,driver_class_name,url,user_name,pass_word)
VALUES (4, 'zn300x', 'zn300x', '2021-09-19 09:38:14', 'zn300x', NULL, NULL, NULL, NULL, '/usr/local/soft/driver/zn300x.jar', 'com.aurine.edge.zn300xdriver.Zn300xDriver', 'org.sqlite.JDBC', 'jdbc:sqlite:db/zn300x.db', NULL, NULL);
-- zn300x 相关设备配置 20230630
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES ( NULL, '3E99ZNZ01', '3E99ZNZ01', NULL, 'GL-N006-多门门禁驱动', 'GL-N006', '3E99ZNZ01', '米立', NULL, NULL, 'json', NULL, '[]', NULL, NULL, '4', NULL, 1, NULL, '2022-03-21 11:07:12', '2023-02-14 08:41:45', '020320');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES ( NULL, 'TD004M1001', 'TD004M1001', NULL, 'GL8-Z10-单门门禁设备', 'GL8-Z10', 'TD004M1001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES ( NULL, 'TD004M2001', 'TD004M2001', NULL, 'GL8-Z20-双门门禁设备', 'GL8-Z20', 'TD004M2001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES ( NULL, 'TD004M4001', 'TD004M4001', NULL, 'GL8-Z40-四门门禁设备', 'GL8-Z40', 'TD004M4001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', 'TD004M1001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'TD004M1001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:28');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', 'TD004M2001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'TD004M2001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', 'TD004M4001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'TD004M4001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '13', '3E99ZNZ01', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');

-- 更新nacos配置
UPDATE `pigxx_config`.`config_info` SET  `group_id` = 'DEFAULT_GROUP',`content` =
'spring:
  datasource:
    dynamic:
      primary: master
      strict: false
      datasource:
        master:
          url: jdbc:mysql://${MYSQL-HOST:pigx-mysql}:${MYSQL-PORT:3306}/${MYSQL-DB:edge_iaas}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true
          username: ${MYSQL-USER:aurine}
          password: ${MYSQL-PWD:aurKf5b_2019}
          driver-class-name: com.mysql.cj.jdbc.Driver
  redis:
    host: ${REDIS_HOST:pigx-redis}
    port: ${REDIS_PORT:6379}
    database: 4
#    password: yf5b_Redis
    # password: yf5b_Redis
    timeout: 5000
    jedis:
      pool:
        max-idle: 8
        min-idle: 0
        max-active: 8
        max-wait: -1

winServerIp: 10.110.18.198
winServerPort: 8086

security:
  oauth2:
    client:
      client-id: pig
      client-secret: pig
      scope: server
      ignore-urls:
        - /druid/**
        - /actuator/**
        - /v2/api-docs
        - /**

# Swagger
swagger:
  ui-config:
    # Swagger Tag
    tags-sorter: method
    # Swagger
    operations-sorter: method
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  # configuration:
    # local-cache-scope: session
    # cache-enabled: true
    # log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    # log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl
zn300x:
  bind-port: 60002' WHERE `data_id` = 'iotmiddle-biz-dev.yml';
