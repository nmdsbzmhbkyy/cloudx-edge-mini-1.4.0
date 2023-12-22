use aurine;

begin;
-- 1.3.0.0菜单调整 20230705

-- 删除设备子系统配置中车道一体机类型
DELETE FROM `aurine`.`project_device_subsystem` WHERE `subsystemCode` = '22' AND  `projectId`= 1000000708;
-- 删除设备类型字典中车道一体机类型
UPDATE `pigxx`.`sys_dict_item` SET `del_flag` = '1' WHERE TYPE = 'device_type' and `value` = '22';

-- 来访登记位置移动到认证登记菜单下
UPDATE `pigxx`.`sys_menu` SET `parent_id` = '11012' WHERE `name` = '来访登记' AND `menu_id` = '10550';
-- 功能卡发卡位置移动到门禁通行菜单下
UPDATE `pigxx`.`sys_menu` SET `parent_id` = '10471' WHERE `name` = '功能卡发卡' AND `menu_id` = '11130';

-- 删除访客管理菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '访客管理' AND menu_id = '11015';

-- 删除车场相关菜单资源
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '停车区域设置-new' AND menu_id = '11094';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '停车区域设置' AND menu_id = '10080';
-- 停车区域设置按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '10080' AND `type` = '1';


UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车场设置' AND menu_id = '10680';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车场设置-new' AND menu_id = '11091';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车位区域设置-new' AND menu_id = '11112';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车位管理-new' AND menu_id = '11095';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车位管理' AND menu_id = '10410';
-- 车位管理按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '10410' AND `type` = '1';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车辆登记-new' AND menu_id = '11096';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车辆登记' AND menu_id = '10619';
-- 车辆登记按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '10619' AND `type` = '1';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车位归属登记-new' AND menu_id = '11097';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车位归属登记' AND menu_id = '10461';
-- 车位归属登记按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '10461' AND `type` = '1';


UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车辆审核' AND menu_id = '11053';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车辆审核-new' AND menu_id = '11098';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '电梯通行方案管理' AND menu_id = '11076';
-- 电梯通行方案管理按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '11076' AND `type` = '1';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车辆通行-new' AND menu_id = '11108';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '黑名单管理' AND menu_id = '11113';



UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车行记录' AND `menu_id` = '10610';
-- 车行记录按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '10610' AND `type` = '1';


UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车场明细表-new' AND `menu_id` = '11101';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '场内车辆记录-new' AND `menu_id` = '11102';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '手动开闸记录-new' AND `menu_id` = '11110';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '手动开闸记录-new' AND `menu_id` = '11119';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车行记录-new' AND `menu_id` = '11099';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车场发行记录-new' AND `menu_id` = '11107';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车场管理' AND `menu_id` = '10438';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车辆类型设置-new' AND `menu_id` = '11090';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '计费规则设置-new' AND `menu_id` = '11086';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '计费规则设置' AND `menu_id` = '10606';
-- 计费规则设置按钮菜单
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `parent_id` = '10606' AND `type` = '1';

UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '场内车辆' AND `menu_id` = '11060';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '场内车辆-new' AND `menu_id` = '11092';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '车场全局设置-new' AND `menu_id` = '11111';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '缴费记录-new' AND `menu_id` = '11131';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '缴费记录-new' AND `menu_id` = '11132';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '缴费记录-new' AND `menu_id` = '11133';



-- 删除1.3.0多余相关菜单资源
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '在线监控-new' AND `menu_id` = '11118';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '小区广播中心' AND `menu_id` = '11085';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '门禁卡管理' AND menu_id = '11140';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '住户审核' AND `menu_id` = '10689';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '布防/撤防记录' AND `menu_id` = '11120';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '结构化事件记录' AND `menu_id` = '11136';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '周界报警' AND `menu_id` = '11059';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '安防管理' AND `menu_id` = '10433';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '智能监控' AND `menu_id` = '11018';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '视频监控' AND `menu_id` = '10679';
UPDATE `pigxx`.`sys_menu` SET `del_flag` = '1' WHERE `name` = '视频回放' AND `menu_id` = '10691';
-- 增加呼叫记录菜单
INSERT IGNORE INTO `pigxx`.`sys_menu` (`menu_id`, `name`, `permission`, `path`, `parent_id`, `icon`, `sort`, `keep_alive`, `type`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `menu_filter`) VALUES (11137, '呼叫记录', NULL, '/estate/service/call-event/index', 11016, NULL, 999, '0', '0', '2022-11-01 16:29:11', '2022-11-07 16:30:48', '0', 1, '');

-- 修改中台订阅地址
update edge_iaas.middle_subscribe set callback_url = 'http://pigx-web:4001/api/callback/device/aurine-edge/device/command/response?sn=aurine-edge_MIDDLE&ver=v1' where id = 76;
update edge_iaas.middle_subscribe set callback_url = 'http://pigx-web:4001/api/callback/device/aurine-edge/device/status/update?sn=aurine-edge_MIDDLE&ver=v1' where id = 77;
update edge_iaas.middle_subscribe set callback_url = 'http://pigx-web:4001/api/callback/device/aurine-edge/device/active?sn=aurine-edge_MIDDLE&ver=v1' where id = 78;
update edge_iaas.middle_subscribe set callback_url = 'http://pigx-web:4001/api/callback/device/aurine-edge/device/data/update?sn=aurine-edge_MIDDLE&ver=v1' where id = 79;
update edge_iaas.middle_subscribe set callback_url = 'http://pigx-web:4001/api/callback/device/aurine-edge/device/objmanager/report?sn=aurine-edge_MIDDLE&ver=v1' where id = 80;
update edge_iaas.middle_subscribe set callback_url = 'http://pigx-web:4001/api/callback/device/aurine-edge/device/event/report?sn=aurine-edge_MIDDLE&ver=v1' where id = 81;

commit;