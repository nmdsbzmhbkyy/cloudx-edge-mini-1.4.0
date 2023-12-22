drop table if exists aurine.open_app_info;

/*==============================================================*/
/* Table: open_app_info                                         */
/*==============================================================*/
/* 开放平台应用表，管理开放平台的接口调用权限 */
create table aurine.open_app_info
(
   seq                  int unsigned not null auto_increment,
   appUUID              varchar(32) not null comment '应用UUID，uuid，标识内部唯一',
   appId                int not null default '' comment '应用id，对外提供的应用id',
   appName              varchar(255) not null default '' comment '应用名称',
   appDesc              varchar(1024) not null default '' comment '应用描述',
   appType              varchar(5) not null default '' comment '应用类型（预留字段）',
   tenant_id            int not null default 1 comment '租户ID',
   operator             int not null default 1 comment '操作人',
   createTime           timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   updateTime           timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (seq)
);

alter table aurine.open_app_info comment '开放平台应用信息表';

/*==============================================================*/
/* Index: uidx_app_uuid                                         */
/*==============================================================*/
create unique index uidx_app_uuid on aurine.open_app_info
(
   appUUID
);
