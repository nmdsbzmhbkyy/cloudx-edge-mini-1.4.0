use aurine;

begin;


--  新增冠林车场
INSERT IGNORE INTO `pigxx`.`sys_dict_item` (dict_id,value,label,`type`,description,sort,remarks,tenant_id) VALUES (307,'iotparking','冠林车场','parking_company','冠林车场',2,'冠林车场',1);
-- 新增车辆道闸产品
INSERT IGNORE  INTO aurine.sys_device_product_map (seq, deviceTypeId, productId, productCode, productType, productName, productModel, modelId, manufacture, productDesc, protocal, dataFormat, industry, capabilities, capability, adaptionId, thirdPartyNo, projectId, tenant_id, operator, createTime, updateTime, modelType) VALUES(246, NULL, 'VT99T001', 'VT99T001', NULL, 'GL-T001-车辆通行', 'GL-T001', 'VT99T001', '米立', NULL, NULL, 'json', NULL, '[]', NULL, NULL, '4', NULL, 1, NULL, '2022-07-27 14:25:40', '2022-09-19 17:07:03', '020401');
INSERT IGNORE INTO aurine.sys_device_type_model (seq, deviceTypeId, productId, tenant_id, operator, createTime, updateTime) VALUES(5650, '22', 'VT99T001', 1, 1, '2022-07-27 14:25:41', '2022-08-24 14:25:35');


-- 车场相关菜单 需求3822 3823 3824 3825
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11091, '车场设置-new', NULL, '/parking/base/setting/parking-setting/index', 10051,NULL, 2, '0', '0', '2022-05-12 10:27:56', '2022-05-12 10:35:06', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11095, '车位管理-new', NULL, '/parking/base/setting/parking/index', 10051, NULL, 999, '0', '0', '2022-05-13 11:06:22', NULL, '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11112, '车位区域设置-new', NULL, '/parking/base/setting/parking-area/index', 10051, NULL, 998, '0', '0', '2022-06-09 09:57:26', '2022-06-09 09:59:37', '0', 1, '0,1,2,3');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11097, '车位归属登记-new', NULL, '/parking/base/setting/parking-manager/index', 11012, NULL, 999, '0', '0', '2022-05-13 11:07:45', '2022-05-13 11:07:54', '0', 1, '');



-- 车场相关菜单配置 20230316
-- 管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (1,11091),(1,11112),(1,11095),(1,11097);
-- 项目管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (358,11091),(358,11112),(358,11095),(358,11097);
-- 项目管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (402,11091),(402,11112),(402,11095),(402,11097);

-- 添加车场设备配置
INSERT IGNORE INTO `aurine`.`sys_thirdparty_interface_config` (seq, thirdPartyNo, name, version, `type`, jsonConfig, subscribeStatus, remark, projectId, tenant_id, operator, createTime, updateTime) VALUES(40, '9', 'AURINE_PARKING', '1', '1', '', '0', '车场服务', NULL, 1, NULL, '2023-04-12 18:13:20', '2023-04-12 18:15:40');

-- 2023-04-10  需求 #3899（入云）事件中心-人行记录在边侧的人行记录中添加【访问地址】字段，与云端保持一致
delimiter //
CREATE PROCEDURE pro_AddColumn() BEGIN
  IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='project_entrance_event' AND COLUMN_NAME in ('addrDesc')) THEN
Alter TABLE aurine.project_entrance_event
    ADD COLUMN addrDesc varchar(255) DEFAULT NULL COMMENT '地址描述' after updateTime;
END IF;
END//
delimiter ;
CALL pro_AddColumn();
DROP PROCEDURE IF EXISTS pro_AddColumn;


-- 2023-04-10 需求 #3847 新增必填项，新增“公共楼层设置”
delimiter //
CREATE PROCEDURE pro_AddColumn() BEGIN
  IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='project_building_info' AND COLUMN_NAME in ('floorNumber')) THEN
ALTER TABLE `aurine`.`project_building_info`
    ADD COLUMN `floorNumber` varchar(600) NULL COMMENT '楼层编号关系' AFTER `publicFloors`;
END IF;
END//
delimiter ;
CALL pro_AddColumn();
DROP PROCEDURE IF EXISTS pro_AddColumn;


-- 2023-04-13   #3802 设备管理-“新增乘梯识别终端”模块   #3801 设备管理-“新增电梯分层器”模块
INSERT IGNORE INTO `aurine`.`project_device_subsystem`(`seq`, `projectId`, `subsystemId`, `subsystemCode`, `subsystemName`, `pid`, `sort`, `rLevel`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES (675, 1000000708, '41029d21574f32e49556cb6e9aa7b5e3', '17', '电梯分层控制器', 'f56f9049a21947c3b5fb256ed70dc7c6', NULL, '3', 1, 1311, '2022-07-18 09:47:09', '2022-07-18 09:47:09');
INSERT IGNORE INTO `aurine`.`project_device_subsystem`(`seq`, `projectId`, `subsystemId`, `subsystemCode`, `subsystemName`, `pid`, `sort`, `rLevel`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES (676, 1000000708, '8100ebbd52ca61a8210933d89f4f693a', '18', '乘梯识别终端', 'f56f9049a21947c3b5fb256ed70dc7c6', NULL, '3', 1, 1311, '2022-07-18 09:47:10', '2022-07-18 09:47:10');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf`( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '18', '4de61807d34d45ed8b0cee5e2f41a482', 'companyCode', '厂商信用代码', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-02 16:58:38', '2022-09-02 16:59:12');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf`( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '17', '4de61807d34d45ed8b0cee5e2f41a481', 'companyCode', '厂商信用代码', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-02 16:58:38', '2022-09-02 16:59:12');



-- 2023-04-14  #3804 设备管理-“电梯管理”模块
-- 电梯管理改成电梯组管理
UPDATE `pigxx`.`sys_menu` SET NAME = "电梯组管理" WHERE menu_id = 11075;
-- 给默认角色加上电梯组菜单
INSERT IGNORE INTO `pigxx`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (358, 11075),(376, 11075),(377, 11075),(400, 11075),(401, 11075),(403, 11075);

-- 2023-04-17 #3849 事件中心-增加乘梯记录
-- 给默认角色加上乘梯记录菜单
INSERT IGNORE INTO `pigxx`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (358, 11121),(376, 11121),(377, 11121),(400, 11121),(401, 11121),(403, 11121);


-- 添加双门 四门控制器配置 20230426
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`) VALUES ( NULL, '3E99DMMJ01', '3E99DMMJ01', NULL, 'GL-N005-多门门禁驱动', 'GL-N005', '3E99DMMJ01', '米立', NULL, NULL, 'json', NULL, '[]', NULL, NULL, '4', NULL, 1, NULL, '2022-03-21 11:07:12', '2023-02-14 08:41:45', '020320');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`) VALUES ( NULL, 'TD003M2001', 'TD003M2001', NULL, 'GL8-M20-双门门禁设备', 'GL8-M20', 'TD003M2001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_product_map` ( `deviceTypeId`, `productId`, `productCode`, `productType`, `productName`, `productModel`, `modelId`, `manufacture`, `productDesc`, `protocal`, `dataFormat`, `industry`, `capabilities`, `capability`, `adaptionId`, `thirdPartyNo`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`, `modelType`) VALUES ( NULL, 'TD003M4001', 'TD003M4001', NULL, 'GL8-M40-四门门禁设备', 'GL8-M40', 'TD003M4001', '米立', NULL, NULL, 'json', NULL, '[]', 'card', NULL, '4', NULL, 1, NULL, '2022-12-07 10:30:26', '2022-12-17 11:31:01', '020302');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', 'TD003M4001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'TD003M4001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:28');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', 'TD003M2001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'TD003M2001', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');
INSERT IGNORE INTO `aurine`.`sys_device_type_model` ( `deviceTypeId`, `productId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '13', '3E99DMMJ01', 1, 1, '2022-12-16 10:53:40', '2023-04-24 14:31:47');

INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', '67df376dd7534cfa913537e673b0d181', 'passwd', '通行密码', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', '71085e3dbd574e8a8c8e4779d2d41574', 'doorNo', '设备门号', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', '6546372a073c475d9b3736cf2359edd4', 'netGate', '网关', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '2', 'b43dda3f7aa8440a8dd07d3901eb55a3', 'netMask', '子网掩码', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'db7bc1015fec45b68452de64dcbb9f41', 'passwd', '通行密码', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', 'bb1f767b40d7446bb4a910bed0b9e14f', 'doorNo', '设备门号', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', '12bc694cec5b4943a64fae50c6e7b21b', 'netGate', '网关', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');
INSERT IGNORE INTO `aurine`.`project_device_attr_conf` ( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '3', '78825a8a059e450095a9603521cddc1c', 'netMask', '子网掩码', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-09-29 13:45:16', '2022-10-11 17:31:34');



-- 车场其他菜单补充 20230412
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (10438, '车场管理', NULL, '/parking', '-1', 'icon-chechangguanli', 8, '0', '0', '2022-05-16 11:11:04', '2022-05-16 11:12:58', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11086, '计费规则设置-new', NULL, '/parking/parking/rule/index', 10438, NULL, 2, '0', '0', '2022-05-07 16:14:01', '2022-05-07 16:16:57', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11090, '车辆类型设置-new', NULL, '/parking/parking/park-car-type/index', 10438, NULL, 1, '0', '0', '2022-05-10 14:40:18', '2022-05-10 14:40:29', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11092, '场内车辆-new', NULL, '/parking/parking/entrance-car/index', 10438, NULL, 999, '0', '0', '2022-05-13 11:03:02', NULL, '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11099, '车行记录-new', NULL, '/parking/parking/entrance/index', 11101, NULL, 999, '0', '0', '2022-05-13 11:07:45', '2022-05-24 16:37:05', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11102, '场内车辆记录-new', NULL, '/parking/parking/event-entrance-car/index', 11101, NULL, 3, '0', '0', '2022-05-16 11:12:48', '2022-05-16 11:24:26', '0', 1, '0,2,3,1');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11107, '车场发行记录-new', NULL, '/parking/parking/park-Issue-record/index', 11101, NULL, 999, '0', '0', '2022-05-24 13:38:34', '2022-05-24 13:47:01', '0', 1, '0,1,2,3');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11109, '车牌授权状态管理-new', NULL, '/parking/parking/plate-number-status/index', 11108, NULL, 999, '0', '0', '2022-05-24 13:46:32', '2022-05-24 13:46:43', '0', 1, '0,1,2,3');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11118, '在线监控-new', NULL, '/parking/parking/monitoring/index', 10438, NULL, 998, '0', '0', '2022-06-16 16:41:55', NULL, '0', 1, '0,1,2,3');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11108, '车辆通行-new', NULL, '/parking/parking', 10436, NULL, 999, '0', '0', '2022-05-24 13:40:42', NULL, '0', 1, '0,1,2,3');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11101, '车场明细表-new', NULL, '/parking/entrance', 11016, NULL, 4, '0', '0', '2022-05-16 11:11:04', '2022-05-16 11:12:58', '0', 1, '0,1,2,3');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11111, '车场全局设置-new', NULL, '/parking/parking/global-setting/index', 10438, NULL, 999, '0', '0', '2022-05-16 11:11:04', '2022-05-16 11:12:58', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11133, '缴费记录-new', NULL, '/parking/parking/billing-info/index', 10438, NULL, 999, '0', '0', '2022-05-16 11:11:04', '2022-05-16 11:12:58', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11113, '黑名单管理', NULL, '/estate/service/blacklist/index', 11012, NULL, 7, '0', '0', '2020-09-25 13:51:45', '2020-09-25 14:15:56', '0', 1, '');
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11110, '手动开闸记录-new', NULL, '/parking/parking/open-lane-his/index', 11101, NULL, 7, '0', '0', '2020-09-25 13:51:45', '2020-09-25 14:15:56', '0', 1, '');

-- 车场相关菜单角色权限配置 20230412
-- 管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (1,10438),(1,11086),(1,11090),(1,11092),(1,11099),(1,11102),(1,11107),(1,11109),(1,11118),(1,11108),(1,11101),(1,11111),(1,11133),(1,11096),(1,11097),(1,11098),(1,11092),(1,11113),(1,11110);
-- 项目管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (358,10438),(358,11086),(358,11090),(358,11092),(358,11099),(358,11102),(358,11107),(358,11109),(358,11118),(358,11108),(358,11101),(358,11111),(358,11133),(358,11096),(358,11097),(358,11098),(358,11092),(358,11113),(358,11110);
-- 项目管理员
INSERT IGNORE INTO `pigxx`.`sys_role_menu` (`role_id`, `menu_id`) VALUES (402,10438),(402,11086),(402,11090),(402,11092),(402,11099),(402,11102),(402,11107),(402,11109),(402,11118),(402,11108),(402,11101),(402,11111),(402,11133),(402,11096),(402,11097),(402,11098),(402,11092),(402,11113),(402,11110);

delimiter //
CREATE PROCEDURE pro_AddColumn() BEGIN
    IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='project_park_entrance_his' AND COLUMN_NAME ='personName') THEN
        Alter TABLE aurine_parking.project_park_entrance_his
            ADD COLUMN personName varchar(32) COMMENT '车主姓名';
    END IF;
END//
delimiter ;
CALL pro_AddColumn();
DROP PROCEDURE IF EXISTS pro_AddColumn;


delimiter //
CREATE PROCEDURE drop_index_if_exists() BEGIN
	DECLARE index_count INT;
SELECT COUNT(*) INTO index_count FROM information_schema.statistics WHERE table_schema = 'aurine_parking' AND table_name = 'project_plate_number_device' AND index_name = 'uidx_platenumber_deviceid_unique';
IF index_count > 0 THEN
ALTER TABLE aurine_parking.project_plate_number_device DROP INDEX uidx_platenumber_deviceid_unique;
END IF;
END //
delimiter ;
CALL drop_index_if_exists();
DROP PROCEDURE IF EXISTS drop_index_if_exists;


-- 2023-04-10  # 5259 （入云）事件中心-人行记录在边侧的人行记录中添加【访问地址】字段，与云端保持一致
delimiter //
CREATE PROCEDURE pro_AddColumn() BEGIN
  IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='project_entrance_event' AND COLUMN_NAME in ('addrDesc')) THEN
Alter TABLE aurine.project_entrance_event
    ADD COLUMN addrDesc varchar(255) DEFAULT NULL COMMENT '地址描述' after updateTime;
END IF;
END//
delimiter ;
CALL pro_AddColumn();
DROP PROCEDURE IF EXISTS pro_AddColumn;


delimiter //
CREATE PROCEDURE pro_AddTable () BEGIN
IF NOT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_name = 'project_right_device_cache' ) THEN
CREATE TABLE `project_right_device_cache` (
 `seq` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 `uuid` VARCHAR ( 32 ) NOT NULL COMMENT 'uuid',
 `type` VARCHAR ( 1 ) DEFAULT NULL COMMENT '2人脸 3卡片',
 `deviceId` VARCHAR ( 32 ) NOT NULL COMMENT '设备uuid',
 `deviceThirdCode` VARCHAR ( 64 ) NOT NULL COMMENT '设备第三方编号',
 `passNo` VARCHAR ( 32 ) CHARACTER
     SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '凭证编号',
 `state` VARCHAR ( 5 ) NOT NULL COMMENT '凭证状态',
 `tenant_id` INT NOT NULL DEFAULT '1' COMMENT '租户ID',
 `operator` INT DEFAULT NULL COMMENT '操作人',
 `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `updateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY ( `seq` ),
 UNIQUE KEY `uidx_uuid` ( `uuid` ) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 5314 DEFAULT CHARSET = utf8mb3 COMMENT = '凭证信息缓存表';
END IF;
END //
delimiter ;
CALL pro_AddTable ();
DROP PROCEDURE IF EXISTS pro_AddTable;



UPDATE pigxx_config.config_info SET data_id='cloudx-estate-biz-dev.yml', group_id='DEFAULT_GROUP', content='opentracing:
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
# feign:
#   httpclient:
#     connection-timeout: 20000
feign:
  client:
    config:
      default:
        connectTimeout: 50000
        readTimeout: 50000
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
    - project_device_attr
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
', md5='c70039e0f2a1690a68c394d85544faea', gmt_create='2020-04-30 15:11:59', gmt_modified='2023-04-14 11:55:55', src_user=NULL, src_ip='10.110.18.6', app_name='', tenant_id='', c_desc='null', c_use='null', effect='null', `type`='yaml', c_schema='null' WHERE data_id='cloudx-estate-biz-dev.yml';


-- 车道一体机产品添加脚本补充 llx
INSERT IGNORE INTO aurine.sys_devicetype_modeltype_config
(configUUid, productModelType, deviceTypeId, platformName, remark, tenant_id, operator, createTime, updateTime)
VALUES('', '020401', '22', 'AURINE_EDGE_MIDDLE', '', 1, 1, '2022-07-21 08:54:20', '2022-07-21 08:54:22');

-- INSERT IGNORE INTO aurine.sys_device_product_map
-- (deviceTypeId, productId, productCode, productType, productName, productModel, modelId, manufacture, productDesc, protocal, dataFormat, industry, capabilities, capability, adaptionId, thirdPartyNo, projectId, tenant_id, operator, createTime, updateTime, modelType)
-- VALUES(NULL, 'VT99T001', 'VT99T001', NULL, '赛菲姆一体机', 'VT99T001', 'VT99T001', '米立', NULL, '1', 'json', NULL, '[]', NULL, 'TEST', '4', NULL, 1, 1, '2022-07-15 12:00:23', '2022-09-20 10:12:07', '020401');
--
-- INSERT IGNORE INTO aurine.sys_device_type_model
-- (deviceTypeId, productId, tenant_id, operator, createTime, updateTime)
-- VALUES('22', 'VT99T001', 1, 1, '2022-07-27 14:25:41', '2022-08-24 14:25:35');

INSERT IGNORE INTO aurine.project_device_subsystem (seq, projectId, subsystemId, subsystemCode, subsystemName, pid, sort, rLevel, tenant_id, operator, createTime, updateTime) VALUES(677, 1000000708, 'e87652a45d82c2748fdd27f7e01a21d2', '22', '车道一体机', 'f56f9049a21947c3b5fb256ed70dc7c6', NULL, '3', 1, 1, '2023-05-18 17:33:25', '2023-05-18 17:33:25');


-- 功能卡发卡模块
INSERT IGNORE INTO `pigxx`.`sys_role_menu`(`role_id`, `menu_id`) VALUES (358, 11130),(376, 11130),(377, 11130),(400, 11130),(401, 11130),(403, 11130);



-- 双门/四门控制器 小中台
INSERT IGNORE INTO edge_iaas.middle_adaption (id,name,code,create_time,iot_manufacturer,app_id,project_id,api_url,device_server_url,jar_path,class_name,driver_class_name,url,user_name,pass_word)
VALUES (3, 'j2x', 'j2x', '2021-09-19 09:38:14', 'j2x', NULL, NULL, NULL, NULL, '/usr/local/soft/driver/j2x.jar', 'com.aurine.edge.j2xdriver.driver.J2XDriver', 'org.sqlite.JDBC', 'jdbc:sqlite:db/j2x.db', NULL, NULL);
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (21,NULL, 'GL-N005-多门门禁驱动', 'GL-N005', '3E99DMMJ01', 10, NULL, 4, 'json', NULL, NULL, 3, NULL, NULL, NULL, 0, '3E99DMMJ01','020320');
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (22,NULL, 'GL8-M20-双门门禁设备', 'GL8-M20', 'TD003M2001', 10, NULL, 4, 'json', NULL, NULL, 3, NULL, NULL, NULL, 0, 'TD003M2001','020302');
INSERT IGNORE INTO edge_iaas.middle_product (id,`type`,name,model_name,model_id,manufacturer_id,`desc`,protocol_id,data_format,industry,capability_content,adaption_id,product_type_id,capability,adaption_product_id,pid,product_key,model_type) VALUES
    (23,NULL, 'GL8-M40-四门门禁设备', 'GL8-M40', 'TD003M4001', 10, NULL, 4, 'json', NULL, NULL, 3, NULL, NULL, NULL, 0, 'TD003M4001','020302');

UPDATE pigxx_config.config_info SET data_id='iotmiddle-biz-dev.yml', group_id='DEFAULT_GROUP', content='spring:
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

winServerIp: 10.110.18.4
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
  configuration:
    local-cache-scope: session
    cache-enabled: true
    # log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl' WHERE data_id='iotmiddle-biz-dev.yml';


-- 配置设备类型 乘梯识别终端和电梯分层器
INSERT IGNORE INTO `aurine`.`sys_devicetype_thirdparty_config`( `deviceTypeId`, `thirdPartyNo`, `isDefault`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '18', '4', '1', 1000000708, 1, NULL, '2023-05-25 10:37:05', '2023-05-25 10:37:05');
INSERT IGNORE INTO `aurine`.`sys_devicetype_thirdparty_config`( `deviceTypeId`, `thirdPartyNo`, `isDefault`, `projectId`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ( '17', '4', '1', 1000000708, 1, NULL, '2023-05-25 10:37:52', '2023-05-25 10:37:52');


-- 电梯分层器产品配置
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftDeviceParamsObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftNetworkParamsObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftHardwareParamsObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftBaseParamsObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftPeriodManageParamsObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftBeautyPeriodParamsObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftFloorObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');
INSERT IGNORE INTO `aurine`.`sys_product_service`(`productId`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES ('EL00WD24HA', 'LiftSysOperateObj', NULL, NULL, NULL, NULL, '0', '1', '0', '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '0', 1, 1, '2022-07-26 16:33:12', '2022-08-24 14:25:35');


-- 添加唯一索引 避免重复执行IGNORE不生效的问题 造成重复字段
delimiter //
CREATE PROCEDURE pro_AddIndexName() BEGIN
  IF NOT EXISTS(SELECT 1 FROM information_schema.statistics WHERE table_schema = 'aurine' AND table_name = 'project_device_attr_conf' AND index_name = 'index_name') THEN
ALTER TABLE project_device_attr_conf ADD UNIQUE INDEX index_name (attrId);
END IF;
END//
delimiter ;
CALL pro_AddIndexName();
DROP PROCEDURE IF EXISTS pro_AddIndexName;

-- bug #12946 基础服务-设备管理-设备管理-乘梯识别终端，新增页面的【设备版本】字段值会自动填充【软件版本】输入的值；【硬件版本】字段值会自动填充【设备固件版本】输入的值
INSERT IGNORE INTO `aurine`.`project_device_attr_conf`( `deviceTypeId`, `attrId`, `attrCode`, `attrName`, `remark`, `projectId`, `valueType`, `valueMin`, `valueMax`, `valuePrecision`, `valueDesc`, `unit`, `tenant_id`, `operator`, `createTime`, `updateTime`) VALUES ('18', '265c0b8fe41547e992a58281e7f8ff22', 'hardwareversion', '硬件版本', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-05-31 11:47:28', '2023-05-31 11:47:31');

-- HA80驱动参数增加厂商
INSERT IGNORE INTO `aurine`.`sys_service_param_conf`(`seq`, `serviceId`, `serviceName`, `paramId`, `paramName`, `isMandatory`, `isSync`, `isModify`, `isReboot`, `isVisible`, `columnType`, `defaultValue`, `valueRange`, `errorMsg`, `inputType`, `dictMap`, `remark`, `parServId`, `parParamId`, `servLevel`, `tenant_id`, `createTime`, `updateTime`) VALUES (82, 'EdgeSysParamObj', '边缘网关-系统参数', 'manufacturer', '设备使用厂商', '0', '0', '1', '0', '1', 'int', '', NULL, NULL, 'select', '0:米立,10:保利,11:天恒地产', '', '0', '0', 1, 1, '2021-10-20 15:44:57', '2023-07-03 16:59:25');


commit;