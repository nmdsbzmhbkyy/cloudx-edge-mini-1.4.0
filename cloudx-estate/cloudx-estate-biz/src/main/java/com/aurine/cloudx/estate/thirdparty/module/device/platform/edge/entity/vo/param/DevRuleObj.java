package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 设备编号规则对象
 * </p>
 *
 * @ClassName: DevRuleObj 是DeviceNoObj里面的devNoRulejson对象
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午03:58:04
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("devNoRule")
public class DevRuleObj {

    /**
     * 梯口号长度
     * 默认4
     */
    @NotNull
    Integer stairNoLen;

    /**
     * 房号长度
     * 默认4
     */
    @NotNull
    Integer roomNoLen;

    /**
     * 单元号长度
     * 默认2
     */
    @NotNull
    Integer cellNoLen;

    /**
     * 是否启用单元号
     * 0：不启用 1：启用，默认1
     */
    @NotNull
    Integer useCellNo;

    /**
     * 分段参数
     * 0不分段，默认224
     */
    String subSection;
}
