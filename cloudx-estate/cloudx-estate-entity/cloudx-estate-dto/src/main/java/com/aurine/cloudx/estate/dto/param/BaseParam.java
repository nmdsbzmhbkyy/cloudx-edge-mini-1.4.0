package com.aurine.cloudx.estate.dto.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *  参数基类
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/23 14:51
 */
@Data
@NoArgsConstructor
public abstract class BaseParam implements Serializable {

    /*
     * 获取对象名枚举
     **/
    @JsonIgnore
    abstract String getObjName();
}
