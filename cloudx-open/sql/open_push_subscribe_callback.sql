drop table if exists aurine.open_push_subscribe_callback;

/*==============================================================*/
/* Table: open_push_subscribe_callback                          */
/*==============================================================*/
/* 开放平台推送订阅回调表，调用方（应用方）可以根据不同项目不同回调类型订阅回调地址 */
create table aurine.open_push_subscribe_callback
(
   seq                  int unsigned not null auto_increment comment 'seq',
   callbackId           varchar(32) not null default '' comment '回调ID，uuid',
   callbackType         char(1) not null default '9' comment '回调类型，0-配置类；1-级联入云类；2-操作类；3-指令类；4-事件类；5-反馈类；9-其他',
   callbackMode         char(1) not null default '0' comment '回调方式，0-url方式；1-topic方式，默认为0',
   callbackUrl          varchar(1024) default '' comment '回调地址，回调接口（服务）地址',
   callbackTopic        varchar(255) default '' comment '回调主题，回调发送到该主题',
   callbackHeaderParam  text comment '回调请求头参数，参数名为Param（预留字段）',
   callbackDesc         varchar(255) default '' comment '回调说明',
   projectUUID          varchar(32) not null default '' comment '项目UUID；如果回调类型是配置类可为空',
   appId                varchar(32) not null default '' comment '应用ID',
   projectType          varchar(5) default '' comment '项目类型，0-云平台端；1-边缘网关；2-WR2X（预留字段）',
   tenant_id            int not null default 1 comment '租户ID',
   operator             int not null default 1 comment '操作人',
   createTime           timestamp not null default CURRENT_TIMESTAMP comment '创建时间',
   updateTime           timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (seq)
)  comment '开放平台推送服务订阅回调接口表';

/*==============================================================*/
/* Index: uidx_callback_id                                      */
/*==============================================================*/
create unique index uidx_callback_id on aurine.open_push_subscribe_callback
(
   callbackId
);
