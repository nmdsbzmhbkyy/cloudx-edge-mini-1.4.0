use
aurine;

begin;

-- 中台数据库增量SQL
INSERT
IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (28,NULL, 'ZL-N001-电梯状态监测驱动', 'ZL-N001', '3E99ZLZT01', 10, NULL, 4, 'json', NULL, NULL, 5, NULL, NULL, NULL, 0, '3E99ZLZT01','020320');
INSERT
IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (29,NULL, 'GL8-EWDZT10-电梯状态监测器', 'GL8-EWDZT10', 'EL00WDZL01', 10, NULL, 4, 'json', NULL, NULL, 5, NULL, NULL, NULL, 0, 'EL00WDZL01','020503');
INSERT
IGNORE INTO edge_iaas.middle_adaption (id,name,code,create_time,iot_manufacturer,app_id,project_id,api_url,device_server_url,jar_path,class_name,driver_class_name,url,user_name,pass_word)
VALUES (5, 'zhLiftState', 'zhLiftState', '2021-09-19 09:38:14', 'zhLiftState', NULL, NULL, NULL, NULL, '/usr/local/soft/driver/zhLiftState.jar', 'com.aurine.edge.zhLiftStateDriver.ZhLiftStateDriver', 'org.sqlite.JDBC', 'jdbc:sqlite:db/zhLiftState.db', NULL, NULL);


-- 字典表 新增设备类型：电梯状态检测器 （26）
INSERT
IGNORE INTO `pigxx`.`sys_dict_item` (`id`, `dict_id`, `value`, `label`, `type`, `description`, `sort`, `create_time`, `update_time`, `remarks`, `del_flag`, `tenant_id`)
    VALUES (3011, 138, '26', '电梯状态检测器', 'device_type', '电梯状态检测器', 18, '2023-10-19 11:35:18', '2023-10-19 11:35:18', '电梯状态检测器', '0', 1);

-- 设备第三方配置表 新增电梯状态检测器配置
INSERT
IGNORE INTO `aurine`.`sys_devicetype_thirdparty_config` (`seq`, `deviceTypeId`, `thirdPartyNo`, `isDefault`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (214, '26', '4', '1', 1000000708, 1, NULL, '2023-10-19 11:35:18', '2023-10-19 11:35:18');

-- 设备子系统表 新增电梯状态检测器
INSERT
IGNORE INTO `aurine`.`project_device_subsystem` (`seq`, `projectId`, `subsystemId`, `subsystemCode`, `subsystemName`, `pid`, `sort`, `rLevel`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (688, 1000000708, '2a5d5dd6ad4c00b3e38160bf2c1ffd38', '26', '电梯状态检测器', 'f56f9049a21947c3b5fb256ed70dc7c6', NULL, '3', 1, 1, '2023-10-19 11:35:18', '2023-10-19 11:35:18');

-- 设备产品表 新增产品：
INSERT
IGNORE INTO `aurine`.`sys_device_product_map` (`seq`, `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES (260, NULL, '3E99ZLZT01', '3E99ZLZT01', NULL, 'ZL-N001-电梯状态监测驱动', 'ZL-N001', '3E99ZLZT01', '米立', NULL, NULL, 'json', NULL, '[]', NULL, NULL, '4', NULL, 1, NULL, '2023-10-19 11:35:18', '2023-10-19 11:35:18', '020320');
INSERT
IGNORE INTO `aurine`.`sys_device_product_map` (`seq`, `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`)
    VALUES (261, NULL, 'EL00WDZL01', 'EL00WDZL01', NULL, 'GL8-EWDZT10-电梯状态监测器', 'GL8-EWDZT10', 'EL00WDZL01', '米立', NULL, NULL, 'json', NULL, '[]', NULL, NULL, '4', NULL, 1, NULL, '2023-10-19 11:35:18', '2023-10-19 11:35:18', '020503');

-- 设备类型表 新增设备类型：电梯状态检测器
INSERT
IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5848, '13', '3E99ZLZT01', 1, 1, '2023-10-19 11:35:18', '2023-10-19 11:35:18');
INSERT
IGNORE INTO `aurine`.`sys_device_type_model` (`seq`, `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`)
    VALUES (5849, '26', 'EL00WDZL01', 1, 1, '2023-10-19 11:35:18', '2023-10-19 11:35:18');

DROP TABLE IF EXISTS project_blacklist_attr;
CREATE TABLE project_blacklist_attr
(
    `id`            INT                                                    NOT NULL AUTO_INCREMENT COMMENT 'id',
    `isDeleted`    tinyint                                                NOT NULL COMMENT '逻辑删除标识',
    `operator`      INT                                                             DEFAULT NULL COMMENT '操作人',
    `createTime`   timestamp                                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   timestamp                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `projectId`     INT                                                    NOT NULL COMMENT '项目ID',
    `tenant_id`     INT                                                    NOT NULL NOT NULL COMMENT '租户ID',
    `faceId`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '人脸库ID',
    `thirdFaceId`  VARCHAR(255) COMMENT '第三方人脸ID',
    `mobile`        VARCHAR(255) COMMENT '联系人员手机号',
    `name`          VARCHAR(255) COMMENT '联系人员名称',
    `credentialNo` VARCHAR(255) COMMENT '联系人员身份证',
    PRIMARY KEY (id)
) COMMENT = '黑名单属性';

DROP TABLE IF EXISTS project_qr_passcode_use_flow;
CREATE TABLE project_qr_passcode_use_flow
(
    `id`            INT         NOT NULL AUTO_INCREMENT COMMENT 'id',
    `isDeleted`    tinyint     NOT NULL COMMENT '逻辑删除标识',
    `operator`      INT                  DEFAULT NULL COMMENT '操作人',
    `createTime`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `projectId`    INT         NOT NULL COMMENT '项目ID',
    `tenant_id`     INT         NOT NULL NOT NULL COMMENT '租户ID',

    `passenger`     VARCHAR(255) COMMENT '通行人员名称',
    `phone`         VARCHAR(255) COMMENT '通行人员电话',
    `credentialNo` VARCHAR(255) COMMENT '通行人员电话号码',
    `startTime`    bigint COMMENT '放行开始时间',
    `endTime`      bigint COMMENT '放行结束时间',
    `times`         INT COMMENT '可通行次数',
    `uniqueCode`   VARCHAR(2048) COMMENT '二维码校验字符串',
    `recordId`     bigint      NOT NULL COMMENT '二维码记录ID',
    `deviceNo`     VARCHAR(32) NOT NULL COMMENT '通行设备编号',
    `deviceId`     VARCHAR(32) NOT NULL COMMENT '通行设备ID',
    `passTime`     bigint      NOT NULL COMMENT '通行时间',
    `result`        tinyint     NOT NULL COMMENT '识别结果;1 识别成功 2 无效二维码 3 过期二维码',
    PRIMARY KEY (id)
) COMMENT = '通行二维码使用记录';

DROP TABLE IF EXISTS project_qr_passcode_record;
CREATE TABLE project_qr_passcode_record
(
    `id`            INT       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `isDeleted`    tinyint   NOT NULL COMMENT '逻辑删除标识',
    `operator`      INT                DEFAULT NULL COMMENT '操作人',
    `createTime`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `projectId`    INT       NOT NULL COMMENT '项目ID',
    `tenant_id`     INT       NOT NULL NOT NULL COMMENT '租户ID',

    `passenger`     VARCHAR(255) COMMENT '通行人员名称',
    `phone`         VARCHAR(255) COMMENT '通行人员电话',
    `credentialNo` VARCHAR(255) COMMENT '通行人员电话号码',
    `startTime`    bigint COMMENT '放行开始时间',
    `endTime`      bigint COMMENT '放行结束时间',
    `times`         INT COMMENT '可通行次数',
    `uniqueCode`   VARCHAR(2048) COMMENT '二维码校验字符串',
    PRIMARY KEY (id)
) COMMENT = '通行二维码注册记录表';

delimiter //
CREATE PROCEDURE project_config_AddColumn() BEGIN
  IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='project_config' AND COLUMN_NAME in ('qrReconWay','blackOpen')) THEN
Alter TABLE aurine.project_config
    ADD qrReconWay TINYINT NOT NULL DEFAULT 0 COMMENT '二维码识别类型0->冠林自有算法，1->表结构识别,2->透传识别',
    ADD blackOpen TINYINT NOT NULL DEFAULT 0 COMMENT '黑名单是否开门：0->关，1->开';
END IF;
END//
delimiter ;
CALL project_config_AddColumn();
DROP PROCEDURE IF EXISTS project_config_AddColumn;

-- ALTER TABLE project_config ADD qrReconWay TINYINT NOT NULL DEFAULT 0 COMMENT '二维码识别类型0->冠林自有算法，1->表结构识别,2->透传识别';
-- ALTER TABLE project_config ADD blackOpen TINYINT NOT NULL DEFAULT 0 COMMENT '黑名单是否开门：0->关，1->开';

-- 新增介质类型,表注释同步修改
ALTER TABLE `project_right_device` MODIFY COLUMN `certMedia`varchar(5) NOT NULL COMMENT '认证介质 1 指纹 2 人脸 3 卡 5 黑名单人脸';

-- 新增人员类型,注释同步修改,人员联系长度从1一位改为2位
ALTER TABLE `project_right_device` MODIFY COLUMN `personType` char(2) DEFAULT NULL COMMENT '人员类型 1 住户 2 员工 3 访客 10 黑名单';
ALTER TABLE `project_face_resources` MODIFY COLUMN `personType` char(2)  DEFAULT NULL COMMENT '人员类型 1 住户 2 员工 3 访客 10 黑名单';

-- 新增图片来源类型,表注释同步修改
ALTER TABLE `project_face_resources` MODIFY COLUMN `origin` char(1)  NOT NULL DEFAULT '1' COMMENT '图片来源 1 web端 2 小程序 3 app 4第三方传入';

-- 人员id改为可以为空,黑名单人脸没有personId
ALTER TABLE `project_face_resources` MODIFY COLUMN `personId` varchar(32) DEFAULT NULL COMMENT '人员id, 根据人员类型取对应表id';

-- 新增人员黑名单菜单及admin人员黑名单菜单权限
INSERT IGNORE INTO  `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11141, '人员黑名单', NULL, '/estate/service/personnel-black-list/index', 11012, NULL, 7, '0', '0', '2023-11-13 11:26:02', '2023-11-13 11:27:49', '0', 1, '');
INSERT IGNORE INTO  `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 11141);
INSERT IGNORE INTO  `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (358, 11141);
INSERT IGNORE INTO  `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (376, 11141);
-- 新增通行二维码菜单及admin通行二维码菜单权限
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11142, '通行二维码', NULL, '/estate/service/access-qrcode/index', 11012, NULL, 8, '0', '0', '2023-11-14 09:35:08', '2023-11-14 09:37:36', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 11142);
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (358, 11142);
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (376, 11142);

commit;