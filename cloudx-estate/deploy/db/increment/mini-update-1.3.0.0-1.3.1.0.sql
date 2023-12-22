use aurine;

begin;
-- 1.3.1.0 新增ZN300,二维码


-- zn300x 相关设备 20230630
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (24,NULL, 'GL-N005-ZN多门门禁驱动', 'GL-N005', '3E99ZNZ01', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, '3E99ZNZ01','020320');
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (25,NULL, 'GL8-Z10-单门门禁设备', 'GL8-Z10', 'TD004M1001', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, 'TD004M1001','020302');
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (26,NULL, 'GL8-Z20-双门门禁设备', 'GL8-Z20', 'TD004M2001', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, 'TD004M2001','020302');
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (27,NULL, 'GL8-Z40-四门门禁设备', 'GL8-Z40', 'TD004M4001', 10, NULL, 4, 'json', NULL, NULL, 4, NULL, NULL, NULL, 0, 'TD004M4001','020302');
INSERT IGNORE INTO edge_iaas.middle_adaption (id,name,code,create_time,iot_manufacturer,app_id,project_id,api_url,device_server_url,jar_path,class_name,driver_class_name,url,user_name,pass_word)
VALUES (4, 'zn300x', 'zn300x', '2021-09-19 09:38:14', 'zn300x', NULL, NULL, NULL, NULL, '/usr/local/soft/driver/zn300x.jar', 'com.aurine.edge.zn300xdriver.Zn300xDriver', 'org.sqlite.JDBC', 'jdbc:sqlite:db/zn300x.db', NULL, NULL);
-- zn300x 相关设备配置 20230630
INSERT IGNORE INTO `aurine`.`sys_device_product_map` (`seq`, `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES (250, NULL, '3E99ZNZ01', '3E99ZNZ01', NULL, 'GL-N005-ZN多门门禁驱动', 'GL-N005', '3E99ZNZ01', '米立', NULL, NULL, 'json', NULL, '[]', NULL, NULL, '4', NULL, 1, NULL, '2022-03-21 11:07:12', '2023-02-14 08:41:45', '020320');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` (`seq`, `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES (251, NULL, 'TD004M1001', 'TD004M1001', NULL, 'GL8-Z10-单门门禁设备', 'GL8-Z10', 'TD004M1001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` (`seq`, `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES (252, NULL, 'TD004M2001', 'TD004M2001', NULL, 'GL8-Z20-双门门禁设备', 'GL8-Z20', 'TD004M2001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` (`seq`, `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES (253, NULL, 'TD004M4001', 'TD004M4001', NULL, 'GL8-Z40-四门门禁设备', 'GL8-Z40', 'TD004M4001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5838, '2', 'TD004M1001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5839, '3', 'TD004M1001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:28');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5840, '2', 'TD004M2001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5841, '3', 'TD004M2001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5842, '2', 'TD004M4001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5843, '3', 'TD004M4001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5844, '13', '3E99ZNZ01', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');

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
commit;