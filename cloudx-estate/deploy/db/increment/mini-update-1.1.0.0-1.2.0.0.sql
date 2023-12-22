use aurine;

begin;

-- 2023-03-08 WEEK 02

-- 开放角色管理菜单  需求：3646
INSERT IGNORE INTO `pigxx`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 10442);

-- 字典管理菜单 需求：3645
INSERT IGNORE INTO `pigxx`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 2000);
INSERT IGNORE INTO `pigxx`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (1, 2200);


-- 2023-03-13 WEEK 03
-- 疫情记录菜单 需求：3743
update `pigxx`.`sys_menu` set del_flag='0' where name = '疫情记录';


-- 疫情记录菜单配置 20230316
-- 管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (1,11058);
-- 项目管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (358,11058);
-- 项目管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (402,11058);

-- 修复车场品牌字典乱码问题
UPDATE `pigxx`.`sys_dict` SET description = '车场品牌',remarks='车场品牌 冠林，博西尼' where type = 'parking_company';
UPDATE `pigxx`.`sys_dict_item` SET label='博西尼车场',description='博西尼车场',remarks='博西尼车场' WHERE id=2985;



-- 修正车场Nacos配置
--  Auto-generated SQL script #202303161848
UPDATE pigxx_config.config_info
SET content='test:
  test: test
server:
  local-project-id: 1000000708
  #重复提交过滤器, 相同 token + url + queryParam + body 长度情况下进行过滤
  duplicate-submit-interceptor:
    enable: true
    #校验模式，true为默认校验全部,false 只有@DuplicateSubmit 的方法会被校验
    validate-all: true
    # 重复间隔，单位毫秒
    ttl: 1000
    # 过滤方法，GET,POST,PUT,DELETE等，ALL表示过滤全部方法
    methods: POST,PUT,DELETE
  feign:
    url: localhost:8080/estate

## spring security 配置
security:
  oauth2:
    client:
      client-id: ENC(ltJPpR50wT0oIY9kfOe1Iw==)
      client-secret: ENC(ltJPpR50wT0oIY9kfOe1Iw==)
      scope: server
      ignore-urls:
        - /druid/**
        - /actuator/**
        - /v2/api-docs
        - /api/callback/device/**
        - /sysDeviceProductMapController/**
        - /notify
        - /ws/**
        - /monitor/**
# 数据源
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: ${MYSQL_USER:aurine}
      password: ${MYSQL_PWD:aurKf5b_2019}
      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:aurine_parking}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        #login-username: admin
        #login-password: admin
      filter:
        stat:
          enabled: true
          log-slow-sql: true
          slow-sql-millis: 10000
          merge-sql: false
        wall:
          config:
            multi-statement-allow: true
  # MQ-Kafka相关配置
  kafka:
    bootstrap-servers: pigx-kafka:9092
    test-bootstrap-servers: pigx-kafka:9092
    listener:
      #允许监听不存在的主题，避免主题还未发布时导致的消费者启动失败，无法启动服务
      missing-topics-fatal: false
    template:
      default-topic: PUSHER
    producer:
      #设置大于0的值，则客户端会将发送失败的记录重新发送
      retries: 0
      #批量发送延迟5ms执行
      linger-ms: 5
      # 每次批量发送消息的数量
      batch-size: 100
      # buffer-memory: 33554432
      buffer-memory: 524288
      # 指定消息key和消息体的编解码方式 UTF-8 key,value均设置为String
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      security:
        enable: false
        protocol: SASL_PLAINTEXT
        sasl-mechanism: SCRAM-SHA-256
        sasl-jaas-config: org.apache.kafka.common.security.scram.ScramLoginModule required username="admin" password="admin123";
    consumer:
      group-id: aurine-group01
      client-id: webConsumer
      #禁止自动提交，以使用ACK
      enable-auto-commit: true
      auto-commit-interval: 1000
      session-timeout: 20000
      #消费策略
      auto-offset-reset: latest
      #消费线程数
      concurrency: 1
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      security:
        enable: false
        protocol: SASL_PLAINTEXT
        sasl-mechanism: SCRAM-SHA-256
        sasl-jaas-config: org.apache.kafka.common.security.scram.ScramLoginModule required username="admin" password="admin123";
  # mybatis组件配置

mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml,classpath*:webMapper/*.xml
  tenant-enable: false
  tenant-and-project-enable: true
  configuration:
    map-underscore-to-camel-case: false
    # log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

minio:
  url: http://pigx-minio:9000
  access-key: minioadmin
  secret-key: ptjs#Minio

# Logger Config
logging:
  level:
    com.pig4cloud.pigx.admin.mapper: debug

    #Push Config

#第三方接口
thirdparty:
  #RestTemplateFactory 超时配置
  rest:
    connection:
      connection-request-timeout: 10000
      connect-timeout: 10000
      read-timeout: 10000
    #RestTemplate 代理配置
    proxy:
      enabled: false
      host: 127.0.0.1
      port: 80
  parking:
    iotparking:
      baseUrl: http://edgeparking:8099
# 任务
xxl:
  job:
    admin:
      addresses: http://${XXL_HOST:xxljob}:${XXL_HOST:8080}/xxl-job-admin
    executor:
      appname: cloudx-job

# 租户表维护
pigx:
  tenant:
    column: tenant_id
    tables:
      - project_parking_info
      - project_vehicles_entry_exit
      - project_entry_exit_lane
      - project_parking_place
      - project_parking_place_his
      - project_park_billing_rule
      - project_park_car_type
      - project_vehicles_entry_exit
      - project_entry_exit_lane
      - project_car_info
      - project_car_pre_register
      - project_par_car_register
      - project_park_region
      - project_plate_number_device
      - project_open_lane_his
      - project_park_entrance_his
      - project_blacklist
cloudx:
  project:
    column: projectId
    tables:
      - project_parking_info
      - project_parking_place
      - project_parking_place_his
      - project_park_region
      - project_par_car_register
      - project_car_info
      - project_park_billing_rule
      - project_park_car_type
      - project_vehicles_entry_exit
      - project_entry_exit_lane
      - project_car_pre_register
      - project_park_region
      - project_plate_number_device
      - project_open_lane_his
      - project_park_entrance_his
      - project_blacklist
'
WHERE data_id='parking-service-dev.yml';

-- nacos脚本 20230322 第四周
--  Auto-generated SQL script #202303221642
UPDATE pigxx_config.config_info
SET content='opentracing:
  jaeger:
    enabled: true
    udp-sender:
      host: localhost
      port: 6831
licenseFilePath: /opt/Auedge/license/
snPath: /opt/public/machineSN
server:
  undertow:
    io-threads: 8
    worker-threads: 256
  # 入云生产环境（默认）
  cloud-uri: http://10.110.18.200:9999
  cloud-ip: 124.70.92.136
  # 如需切换入云测试环境，修改如下：
  #cloud-uri: https://testcloud.aurine.cn
  #cloud-ip: 124.70.92.136

  base-uri: http://pigx-web
  edge-center-uri: http://pigx-iaas:14302
  local-project-id: 1000000708
  #mac边缘网关的网卡
  local-mac: 08:3a:88:91:d7:3d
  #重复提交过滤器, 相同 token + url + queryParam + body 长度情况下进行过滤
  duplicate-submit-interceptor:
    enable: false
    #校验模式，true为默认校验全部,false 只有@DuplicateSubmit 的方法会被校验
    validate-all: true
    # 重复间隔，单位毫秒
    ttl: 200
    # 过滤方法，GET,POST,PUT,DELETE等，ALL表示过滤全部方法
    methods: POST,PUT,DELETE
## spring security 配置
security:
  oauth2:
    client:
      client-id: ENC(ltJPpR50wT0oIY9kfOe1Iw==)
      client-secret: ENC(ltJPpR50wT0oIY9kfOe1Iw==)
      scope: server
      ignore-urls:
        - /druid/**
        - /actuator/**
        - /v2/api-docs
        - /api/callback/device/**
        - /sysDeviceProductMapController/**
        - /adown/**
ignore:
  clients:
    - cert-adown
feign:
  httpclient:
    connection-timeout: 20000
# 数据源
spring:
  main:
    lazy-initialization: false
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: ${MYSQL_USER:aurine}
      password: ${MYSQL_PWD:aurKf5b_2019}
      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:aurine}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        #login-username: admin
        #login-password: admin
      filter:
        stat:
          enabled: true
          log-slow-sql: true
          slow-sql-millis: 10000
          merge-sql: false
        wall:
          config:
            multi-statement-allow: true
# MongoDB相关配置
  data:
    mongodb:
      database: aurine
      host: pigx-mongodb
      port: 27017
  rabbitmq:
    host: 127.0.0.1
    port: 5672
    virtual-host: /
    username: admin
    password: admin123456
# MQ-Kafka相关配置
  kafka:
    bootstrap-servers: pigx-kafka:9092
    test-bootstrap-servers: pigx-kafka:9092
    listener:
      #允许监听不出在的主题，避免主题还未发布时导致的消费者启动失败，无法启动服务
      missing-topics-fatal: false
    template:
      default-topic: PUSHER
    producer:
      #设置大于0的值，则客户端会将发送失败的记录重新发送
      retries: 0
      #批量发送延迟5ms执行
      linger-ms: 5
      # 每次批量发送消息的数量
      batch-size: 100
      # buffer-memory: 33554432
      buffer-memory: 524288
      # 指定消息key和消息体的编解码方式 UTF-8 key,value均设置为String
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      security:
        enable: false
        protocol: SASL_SSL
        sasl-mechanism: SCRAM-SHA-256
        sasl-jaas-config: org.apache.kafka.common.security.scram.ScramLoginModule required username="admin" password="admin123";
    consumer:
      group-id: cloudx-group
      #禁止自动提交，以使用ACK
      enable-auto-commit: true
      auto-commit-interval: 100
      session-timeout: 20000
      max-poll-records: 20
      #消费策略
      auto-offset-reset: latest
      #消费线程数
      concurrency: 1
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      security:
        enable: false
        protocol: SASL_SSL
        sasl-mechanism: SCRAM-SHA-256
        sasl-jaas-config: org.apache.kafka.common.security.scram.ScramLoginModule required username="admin" password="admin123";

# mybatis组件配置
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml,classpath*:webMapper/*.xml
  tenant-enable: false
  tenant-and-project-enable: true
  configuration:
    map-underscore-to-camel-case: false
    # log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# # 文件系统 (提供测试环境，不要乱传)
minio:
  url: http://pigx-minio:9000
  access-key: minioadmin
  secret-key: ptjs#Minio
# 文件系统 (提供测试环境，不要乱传) 测试
# minio:
#   url: http://192.168.0.90:9000
#   access-key: BL662S73F8UYCJR0CPPB
#   secret-key: BV0zccJdW94qAWIUUL5EYmk8tdUt748ps7OuwVe9

# Logger Config
logging:
  level:
    com.aurine.cloudx.estate.mapper: debug

#Push Config
push:
  email:
    host: smtp.qq.com
    username: ahomelife
    password: bsmmzjfvptdadaah
    from: ahomelife@qq.com
    from-name: 智能生活
    #默认邮件标题
    title: TEST Email
  sms:
    url: http://http.yunsms.cn/tx/
    userid: 2205001
    password: kpqpq8
    #验证码模板
    template:
      verification-code: 【米立科技】您的验证码是：%s，请尽快验证！ 如非本人操作请忽略本短信。
  app:
    gtui:
      server-url: http://sdk.open.api.igexin.com/apiex.htm
      app-list:
        - name: test
          appId: peQYk8SdKv9ctlMlzFKl85
          appSecret: appSecret
          appKey: JoF779Dj2Z73fXf9XRRfA9
          masterSecret: 86tzefEYMSAqRBZE7Uu6h
          enabled: true
#第三方接口
thirdparty:
  #RestTemplateFactory超时配置
  rest:
    connection:
      connection-request-timeout: 30000
      connect-timeout: 30000
      read-timeout: 30000
  #RestTemplate 代理配置
    proxy:
      enabled: false
      host: 192.168.0.97
      port: 40010
  #停车场接口
  parking:
    #赛菲姆
    # sfirm:
    #   appId: yma08a7438fe89643c
    #   appSecrets: a978b00891544ef59a4fb1550f2b4355
    #   baseUrl: http://openapi.ymiot.net
    #   version: 1
    sfirm:
      appId: ymcef1d2949101da76
      appSecrets: f70db08ece81451f9f93bbbf0ae24e9c
      baseUrl: http://openapi.szymzh.com
      version: 1
    iotparking:
      baseUrl: pigx-iaas:8099
  #云对讲
  intercom:
    #咚咚
    dongdong:
      userId: 113956
      appKey: KoiuvRSvjKllVsFkeuIk
      secretKey: qwERP4Qh6Hi91Ny7MnKo
      baseUrl: http://yun.miligc.com/wuye_api/2.0/
      version: 2
    #腾讯
    tencent:
  #中台
  middleplatform:
    #是否启用对接，如果启用，未配置的项目将无法添加设备和通行凭证
    enable: true
    #华为中台 3.0
    huawei:
      apiEndPoint: http://121.36.19.192:30880
      apiVersion: v1
      apiResUrl: gwiot/api
      appKey: vtLIlaIRqZOoxpD4
      appSecret: 5iL4p6NgEVen0YtDCxUMk5b2ge7X9Wqd
      kafkaCliendId: huawei01
    #冠林中台2.0
    aurine:
      projectList:
        - projectid: 1000000232
          apiEndPoint: http://220.250.30.50:11111
          apiVersion: v1
          apiResUrl: gwiot/api
          appKey: HVzOtlZN9iWoCuQV
          appSecret: Muppm50raHPoSsocGpSXgZuHUpEaTTw1
          kafkaCliendId: aurine01
      apiEndPoint: http://dev.miligc.com:19986
      apiVersion: 2.1
      appKey: 9000001
      appSecret: 8DD2AA97A5C2F4EAF56235592910D594
      appType: 2
      userName: User001
      kafkaCliendId: aurine01
  #WR20
  wr20:
    #项目-WR20网关配置
    projectList:
      - projectid: 1000000232
        wr20id: 5ebb5524f2d7e4035a3327a7_7000ABF6-98D1-4B7E-91E3-EFB9981C8FEF
        enable: true
  #边缘网关
  edge-gateway:
    #阿里边缘网关
    ali:
      tokenEndPoint: http://220.250.30.50:11111
      tokenVersion: v1
      tokenResUrl: gwiot/api
      tokenKey: HVzOtlZN9iWoCuQV
      tokenSecret: Muppm50raHPoSsocGpSXgZuHUpEaTTw1
      apiEndPoint: http://dev.miligc.com:18088
      apiVersion: v1
      apiResUrl: sinking
      appKey: HVzOtlZN9iWoCuQV
      appSecret: Muppm50raHPoSsocGpSXgZuHUpEaTTw1

aliyun:
  vsc:
    accessKeyId: LTAI4G3eodsFH9i74SHDXkpt
    accessSecret: 60bbXpBtMJPaTzokRfjm55pAgRkjz6
# 任务
xxl:
  job:
    admin:
      addresses: http://${XXL_HOST:pigx-xxljob}:${XXL_HOST:8080}/xxl-job-admin
    executor:
      appname: cloudx-job
      port: 22222
sync:
  serviceName:
    - project_entity_level_cfg
    - project_house_design
    - project_pass_plan
    - project_building_info
    - project_unit_info
    - project_house_info
    - project_frame_info
    - project_device_region
    - project_device_info
    - project_person_info
    - project_house_person_rel
    - project_face_resources
    - project_card
    - project_passwd
    - project_person_plan_rel
    - project_pass_plan_policy_rel
    - project_physical_pass_policy
    - project_logic_pass_policy
    - project_person_device
    - project_right_device
    #- project_entrance_alarm_event
    - project_alarm_handle
    #- project_entrance_event
    - project_visitor_his
    - project_visitor
    - project_label_config
    - project_house_person_change_his
    - project_staff
    - project_device_rel
    - project_lift_event
    - project_person_lift_rel
    - project_device_param_info
    - project_card_his
    - project_config
  parkingServiceName:
    - project_blacklist
    - project_car_info
    - project_entry_exit_lane
    - project_open_lane_his
    - project_par_car_register
    - project_park_billing_info
    - project_park_billing_rule
    - project_park_car_type
    - project_park_entrance_his
    - project_park_region
    - project_parking_info
    - project_parking_place
    - project_parking_place_his
    - project_plate_number_device
    - project_vehicles_entry_exit
    - project_car_pre_register
  parkingNum: 16
  number: 100
# 租户表维护
pigx:
  tenant:
    column: tenant_id
    tables:
      - sys_company
      - sys_project_group
      - project_info
      - project_house_design
      - sys_project_dept
      - project_building_info
      - project_frame_info
      - project_parking_info
      - project_vehicles_entry_exit
      - project_entry_exit_lane
      - project_parking_place
      - project_house_info
      - project_label_config
      - project_house_person_rel
      - project_person_info
      - project_focus_person_attr
      - project_house_person_change_his
      - project_parking_place_his
      - project_person_label
      - project_staff
      - project_pass_plan
      - project_logic_pass_policy
      - project_physical_pass_policy
      - project_pass_plan_policy_rel
      - project_person_device
      - project_person_plan_rel
      - project_device_info
      - project_device_subsystem
      - project_card
      - project_fingerprints
      - project_face_resources
      - project_passwd
      - project_device_region
      - project_notice
      - project_notice_device
      - project_right_device
      - project_floor_pic
      - project_visitor
      - project_visitor_his
      - project_building_batch_add_template
      - project_house_batch_add_template
      - project_unit_batch_add_template
      - sys_service_cfg
      - sys_service_device_classify
      - project_service
      - project_house_service
      - sys_device_classify
      - project_media_ad
      - project_media_ad_dev_cfg
      - project_media_ad_playlist
      - project_media_repo
      - project_alarm_handle
      - project_unit_info
      - project_device_collect
      - project_device_collect_cfg
      - project_device_location
      - project_staff_notice
      - project_staff_notice_object
      - project_person_attr_conf
      - project_person_attr
      - project_device_attr_conf
      - project_device_attr
      - project_park_billing_rule
      - project_park_region
      - project_par_car_register
      - project_car_info
      - project_config
      - project_park_entrance_his
      - project_complaint_record
      - project_complaint_communicate_his
      - project_fee_conf
      - project_bill_day_conf
      - project_billing_info
      - project_house_fee_item
      - project_payment_record
      - project_promotion_conf
      - project_promotion_fee_rel
      - project_repair_record
      - project_shift_conf
      - project_inspect_point_conf
      - project_inspect_point_device_rel
      - project_inspect_quota_conf
      - project_device_monitor_conf
      - project_inspect_route_conf
      - project_inspect_plan
      - project_inspect_plan_shift1
      - project_inspect_plan_shift2
      - project_inspect_plan_shift3
      - project_inspect_route_point_conf
      - project_inspect_plan_shift_staff
      - project_inspect_task
      - project_inspect_detail_device
      - project_inspect_task_staff
      - project_inspect_detail_check_item
      - project_inspect_task_detail
      - project_shift_plan
      - project_shift_plan_staff
      - project_staff_shift_detail
      - project_staff_rota
      - project_staff_rota_detail
      - project_patrol_route_conf
      - project_patrol_route_point_conf
      - project_patrol_point_conf
      - project_patrol_detail
      - project_patrol_info
      - project_patrol_person
      - project_patrol_person_point
      - project_patrol_route_staff
      - project_employer_info
      - project_property_contact
      - sys_device_product_map
      - project_dock_module_conf
      - project_person_notice_plan
      - project_notice_plan_target
      - project_notice_template
      - project_device_param_his
      - project_training_file_db
      - project_carousel_conf
      - project_carousel
      - project_training
      - project_training_staff
      - project_training_file
      - project_training_staff_detail
      - project_device_replace_info
      - project_notice_object
      - project_device_call_event
      - project_attendance_point
      - project_car_pre_register
      - project_attendance
      - project_device_load_log
      - project_device_load_log_detail
      - project_perimeter_alarm_area
      - project_perimeter_alarm_event
      - project_pay_account_conf
      - project_device_monitor_rel
      - project_device_modify_log
      - project_device_abnormal
      - project_device_gather_alarm_rule
      - project_device_rel
      - project_lift_plan
      - project_logic_lift_policy
      - project_lift_plan_policy_rel
      - project_person_lift_rel
      - project_person_lift_plan_rel
      - project_broadcast_config
      - project_broadcast_retry_times
      - project_broadcast_task
      - project_broadcast_task_device_rel
      - project_broadcast_task_region_rel
      - project_broadcast_task_repoid_rel
      - project_perimeter_alarm_record
      - project_card_issue_record
      - project_epidemic_event


cloudx:
  project:
    column: projectId
    tables:
      - project_building_info
      - project_frame_info
      - project_parking_place
      - project_house_info
      - project_house_person_rel
      - project_person_info
      - project_focus_person_attr
      - project_house_person_change_his
      - project_parking_place_his
      - project_person_label
      - project_pass_plan
      - project_logic_pass_policy
      - project_physical_pass_policy
      - project_pass_plan_policy_rel
      - project_person_device
      - project_person_plan_rel
      - project_device_info
      - project_device_subsystem
      - project_card
      - project_card_his
      - project_fingerprints
      - project_face_resources
      - project_passwd
      - project_device_region
      - project_notice
      - project_notice_device
      - project_right_device
      - project_label_config
      - project_visitor
      - project_visitor_his
      - project_building_batch_add_template
      - project_house_batch_add_template
      - project_unit_batch_add_template
      - project_media_ad
      - project_media_ad_dev_cfg
      - project_media_ad_playlist
      - project_media_repo
      - project_house_service
      - project_alarm_handle
      - project_unit_info
      - project_device_location
      - project_staff_notice
      - project_staff_notice_object
      - project_park_region
      - project_par_car_register
      - project_car_info
      - project_config
      - project_complaint_record
      - project_complaint_communicate_his
      - project_fee_conf
      - project_bill_day_conf
      - project_billing_info
      - project_house_fee_item
      - project_payment_record
      - project_promotion_conf
      - project_promotion_fee_rel
      - project_repair_record
      - project_shift_conf
      - project_inspect_point_conf
      - project_inspect_point_device_rel
      - project_inspect_quota_conf
      - project_device_monitor_conf
      - project_inspect_route_conf
      - project_inspect_plan
      - project_inspect_plan_shift1
      - project_inspect_plan_shift2
      - project_inspect_plan_shift3
      - project_inspect_route_point_conf
      - project_inspect_plan_shift_staff
      - project_inspect_task
      - project_inspect_detail_device
      - project_inspect_detail_check_item
      - project_inspect_task_detail
      - project_inspect_task_staff
      - project_shift_plan
      - project_shift_plan_staff
      - project_staff_shift_detail
      - project_staff_rota
      - project_staff_rota_detail
      - project_patrol_route_conf
      - project_patrol_route_point_conf
      - project_patrol_detail
      - project_patrol_info
      - project_patrol_person
      - project_patrol_person_point
      - project_patrol_route_staff
      - project_vehicles_entry_exit
      - project_entry_exit_lane
      - project_employer_info
      - project_property_contact
      - project_staff_region
      - project_person_notice_plan
      - project_notice_plan_target
      - project_notice_template
      - project_device_param_his
      - project_training_file_db
      - project_carousel_conf
      - project_carousel
      - project_training
      - project_training_staff
      - project_training_file
      - project_training_staff_detail
      - project_device_replace_info
      - project_notice_object
      - project_device_call_event
      - project_attendance_point
      - project_car_pre_register
      - project_attendance
      - project_device_load_log
      - project_device_load_log_detail
      - project_perimeter_alarm_area
      - project_perimeter_alarm_event
      - project_pay_account_conf
      - project_device_monitor_rel
      - project_device_modify_log
      - project_device_abnormal
      - project_device_gather_alarm_rule
      - project_device_rel
      - project_lift_plan
      - project_logic_lift_policy
      - project_lift_plan_policy_rel
      - project_person_lift_rel
      - project_person_lift_plan_rel
      - project_broadcast_config
      - project_broadcast_retry_times
      - project_broadcast_task
      - project_broadcast_task_device_rel
      - project_broadcast_task_region_rel
      - project_broadcast_task_repoid_rel
      - project_perimeter_alarm_record
      - project_card_issue_record
      - project_epidemic_event
  job:
    circle:
      visitor: 20
  # 射频卡参数
  rf:
    type: m1
    appId: 68ab0797d987db7f
    appSecret: 5d89902a27bd30d6
    projectSecret: EEC668129557
rtsp:
  server: pigx-iaas:9977
yushi:
  subscribe:
    enable: true
    ip: 127.0.0.1
    port: 9999

#凭证调度
cert-adown:
  #调度线程频率
  dispatchThreadRate: 1000
  #令牌生成频率（流量限制为N,配置参数为1000/n）
  generateTokenRate: 20
  #下发线程操作频率（流量限制为N,配置参数为1000/n）
  adownThreadRate: 20
  #监控线程频率
  monitorThreadRate: 200
  #令牌桶容量
  tokenBucketSize: 500
  #队列入栈阈值，当队列容量小于阈值时，执行入栈操作
  queueMinThresholdValue: 5
  #每次入栈的数据长度
  queuePreSize: 50
  #下发超时时间 秒
  adownTimeoutValue: 10
  #下发超时时间 重试次数
  adownTimeoutRetryTime: 1
  #SQL缓存阈值
  sqlCacheSize: 50
  #SQL缓存提交频率
  sqlCacheFlushRate: 2000
  #下发线程池容量 需重启服务
  adownThreadPoolSize: 20
'
WHERE data_id='cloudx-estate-biz-dev.yml';


-- 添加userId字段用于同步二维码开门事件用户的id,对应人员表和访客记录表的seq
-- 添加扩展字段，用于存储json字符串
delimiter //
CREATE PROCEDURE pro_AddColumn() BEGIN
  IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='project_entrance_event' AND COLUMN_NAME in ('userId','extStr')) THEN
  Alter TABLE aurine.project_entrance_event
    ADD COLUMN userId int COMMENT '对应人员表和访客记录表的seq',
    ADD COLUMN extStr text COMMENT '扩展字段，用于存储json字符串';
END IF;
END//
delimiter ;
CALL pro_AddColumn();
DROP PROCEDURE IF EXISTS pro_AddColumn;

commit;