package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 设备编号对象 json名：deviceNo
 * </p>
 *
 * @ClassName: DeviceNoObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午03:57:18
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("deviceNo")
public class DeviceNoObj {

    /**
     * 设备类型
     * 0:梯口机（默认） 1:区口机 2:门前机 3:门禁一体机 4.门岗机
     */
    @NotNull
    Integer deviceType;

    /**
     * 小区编号 默认：01
     */
    @NotNull
    String areaNo;

    /**
     * 设备编号
     * 010100001
     */
    @NotNull
    String deviceNo;

    /**
     * 设备编号描述 默认为空
     */
    String devnoDesc;

    /**
     * 设备编号规则
     * 见设备编号规则对象定义
     */
    DevRuleObj devNoRule;

    /**
     * 设备分段描述
     * 示例：
     * [\”栋\”,\”单元\”,\”房号\”,\”…\”]
     * 注：
     * 1.单个描述不超过10字符
     * 2.分段描述需按从前到后顺序排列
     * 3.分段个数要跟编号规则分段参数段数一致不超过7段
     */
    ArrayNode devSubDesc;

}
