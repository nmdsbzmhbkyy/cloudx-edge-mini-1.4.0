package com.aurine.cloudx.wjy.pojo;

import lombok.Data;

/**
 * @author ： huangjj
 * @date ： 2021/5/17
 * @description： 我家云待推送数据
 */
@Data
public class PushData {
    //数据类型,1:楼栋,2:单元,3:房间,4:客户,5:房客关系
    private Integer dataType;
    //操作类型,1:新增,2:修改,3:删除
    private Integer operateType;
    //数据
    private String data;
    private Integer success;
    //返回结果
    private String result;
    //提交时间 yyyy-MM-dd HH:mm:ss
    private String time;

    public enum DataType {
        Building(1,"楼栋"),
        Unit(2,"单元"),
        Room(3,"房间"),
        Customer(4,"客户"),
        Relationship(5,"房客关系"),
        Worker(6,"员工"),
        ;
        private Integer code;
        private String desc;
        DataType(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode(){
            return code;
        }
        public String getDesc(){
            return desc;
        }
    }

    public enum OperateType {
        Insert(1,"新增"),
        Update(2,"修改"),
        Delete(3,"删除"),
        ;
        private Integer code;
        private String desc;
        OperateType(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode(){
            return code;
        }
        public String getDesc(){
            return desc;
        }
    }
}