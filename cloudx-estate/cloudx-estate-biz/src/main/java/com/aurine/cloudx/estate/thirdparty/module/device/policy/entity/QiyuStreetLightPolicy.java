package com.aurine.cloudx.estate.thirdparty.module.device.policy.entity;

import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 骐驭路灯策略类
 * </p>
 *
 * 反序列化：{"commandList":[{"commandType":"ON","time":"15:32"},{"commandType":"BRIGHTNESS","commandValue":100,"time":"16:32"}],"snList":[{"sn":"866971039105809"}]}
 * 序列化：{"commandList":[{"commandType":"ON","time":"15:32"},{"commandType":"BRIGHTNESS","commandValue":"100","time":"16:32"}],"imeiList":[{"imei":"866971039105809"}]}
 * @author : 王良俊
 * @date : 2021-08-19 17:25:29
 */
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class QiyuStreetLightPolicy extends BaseIotPolicy {

    @Override
    public String toString() {
        return "ShangrunStreetLightPolicy{" +
                "commandList=" + commandList +
                ", imeiList=" + imeiList +
                '}';
    }

    /**
     * 命令列表
     * */
    private List<Command> commandList;

    /**
     * 设备的SN对象列表
     * */
    private List<ImeiItem> imeiList;

    public List<Command> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<Command> commandList) {
        this.commandList = commandList;
    }

    @JsonProperty("imeiList")
    public List<ImeiItem> getImeiList() {
        return imeiList;
    }

    @JsonProperty("snList")
    public void setImeiList(List<ImeiItem> imeiList) {
        this.imeiList = imeiList;
    }

    /**
     * <p>
     * 具体策略指令
     * </p>
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Command {

        /*
        * ON、OFF
        * */
        private String commandType;

        /*
        * 亮度调节时的亮度百分比 commandValue %
        * */
        private String commandValue;

        /*
        * 12:00
        * */
        private String time;

    }

   /**
    * <p>
    * 路灯设备的imei对象
    * </p>
    */
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ImeiItem {

        /**
         * 设备的sn
         * */
        private String imei;

        /**
         * 主线：1；辅线：2；默认为1
         * */
        @Getter
        @Setter
        private Integer circuit;

       @JsonProperty("imei")
       public String getImei() {
           return imei;
       }

       @JsonProperty("sn")
       public void setImei(String imei) {
           this.imei = imei;
       }

   }

   /**
   * <p>
   * 获取用于策略指令下发的params json
   * </p>
   *
   * @return {"strategy":{"commandList":[{"commandType":"ON","time":"15:32"},{"commandType":"BRIGHTNESS","commandValue":"100","time":"16:32"}],"imeiList":[{"imei":"866971039105809"}]}}
   * @author: 王良俊
   */
   @JsonIgnore
   @Override
   public String getParams() {
       ObjectMapper objectMapper = ObjectMapperUtil.instance();
       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
       ObjectNode value = null;
       try {
           value = objectMapper.readValue(objectMapper.writeValueAsString(this), ObjectNode.class);
           ObjectNode objectNode = objectMapper.createObjectNode();
           objectNode.putPOJO("strategy", value);
           return objectNode.toString();
       } catch (JsonProcessingException e) {
           log.error("[华为中台]骐驭 策略json获取失败：{}", this);
           e.printStackTrace();

       }
       return "";
   }

    @Override
    public String getSn() {
        return this.imeiList.get(0).imei;
    }
}
