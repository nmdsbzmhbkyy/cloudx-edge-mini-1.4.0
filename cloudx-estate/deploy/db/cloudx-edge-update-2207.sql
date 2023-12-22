-- 任务 #3162 设备异常表增加异常处理方案 王良俊
ALTER TABLE aurine.project_device_abnormal ADD advice varchar(255) DEFAULT '' NOT NULL COMMENT '处理意见';
