package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 锁属性对象 json名：lockParam
 * </p>
 *
 * @ClassName: LockparamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:10:04
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("lockParam")
public class LockparamObj {

    /**
     * 锁类型
     * 0：常闭 1：常开
     */
    @NotNull
    Integer lockType;

    /**
     * 开锁时间
     * 0、3、6、9，单位：秒
     */
    @NotNull
    Integer lockTime;
}
