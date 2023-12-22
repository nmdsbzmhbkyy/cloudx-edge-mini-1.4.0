package com.aurine.cloudx.common.core.constant.enums;

import com.aurine.cloudx.common.core.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author cyw
 * @Date 2023 04 23 13 41
 **/
@Getter
@AllArgsConstructor
public enum DoorControllerEnum {
    GL_M20("GL8-M20", "双门控制器", Arrays.asList("2", "3"), 2, DoorControllerAttrConf.baseAttrs()),
    GL8_M40("GL8-M40", "四门控制器", Arrays.asList("2", "3"), 4, DoorControllerAttrConf.baseAttrs()),
    GL8_Z10("GL8-Z10", "单门控制器", Arrays.asList("2", "3"), 1, DoorControllerAttrConf.znAttrs()),
    GL8_Z20("GL8-Z20", "双门控制器", Arrays.asList("2", "3"), 2, DoorControllerAttrConf.znAttrs()),
    GL8_Z40("GL8-Z40", "四门控制器", Arrays.asList("2", "3"), 4, DoorControllerAttrConf.znAttrs()),

    ;

    private String model;
    private String note;
    private List<String> deviceTypes;
    private Integer doorCount;
    private List<AttrsConfig> attrConfs;
    /**
     * @description 根据设备型号获取门控制器数据模型
     * @param  model
     * @return com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum
     * @author cyw
     * @time 2023-04-24 11:59
     */
    public static DoorControllerEnum getByModel(String model) {
        if (StringUtil.isEmpty(model)) {
            return null;
        }
        return Stream.of(DoorControllerEnum.values()).filter(d->d.getModel().equals(model)).findAny().orElse(null);
    }
    /**
     * @description 获取门控制器相关数据模型
     * @param
     * @return java.util.List<com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum.DoorControllerModel>
     * @throws
     * @author cyw
     * @time 2023-04-24 10:08
     */
    public static List<DoorControllerModel> getList() {
        return Stream.of(DoorControllerEnum.values()).map(d ->
                new DoorControllerModel(d.getModel(), d.getNote(), d.getDeviceTypes(), d.getDoorCount(), d.getAttrConfs())
        ).collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    static class DoorControllerModel {
        private String model;
        private String note;
        private List<String> deviceTypes;
        private Integer doorCount;
        private List<AttrsConfig> attrConfs;
    }

    @Getter
    @AllArgsConstructor
    public enum DoorControllerAttrConf {
        PASS_WORD("passwd", "通行密码"),
        DOOR_NO("doorNo", "设备门号"),
        NET_GATE("netGate", "网关"),
        NET_MASK("netMask", "子网掩码"),

        ;
        private String attrCode;
        private String name;

        public static class  DoorControllerAttrConfModel extends AttrsConfig{
            public DoorControllerAttrConfModel(String attrCode, String attrName, String defValue) {
                super(attrCode, attrName, defValue);
            }
        }
        public static List<AttrsConfig> baseAttrs() {
            return Stream.of(DoorControllerAttrConf.values()).map(d->{
                if(d.getAttrCode().equals(DoorControllerAttrConf.PASS_WORD.getAttrCode())){
                    return new DoorControllerAttrConfModel(d.getAttrCode(), d.getName(),"FFFFFF");
                }else{
                    return new DoorControllerAttrConfModel(d.getAttrCode(), d.getName(),null);
                }
            }).collect(Collectors.toList());
        }

        public static List<AttrsConfig> znAttrs() {
            return new ArrayList<AttrsConfig>(){{
                add(new DoorControllerAttrConfModel(DOOR_NO.attrCode,DOOR_NO.name,null));
                add(new DoorControllerAttrConfModel(NET_GATE.attrCode,NET_GATE.name,null));
                add(new DoorControllerAttrConfModel(NET_MASK.attrCode,NET_MASK.name,null));
            }};

        }

    }


}
