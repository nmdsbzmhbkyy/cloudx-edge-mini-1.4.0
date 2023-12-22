package com.aurine.cloudx.estate.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 要用Jackson
 * <p>设备参数表单对象</p>
 *
 * @author : 王良俊
 * @date : 2021-10-28 10:44:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceParamForm {

    /**
     * 参数所属服务名（前端Tab组件使用）
     */
    private String serviceName;

    /**
     * 参数所属服务ID
     */
    private String serviceId;

    /**
     * 某个参数类别下面的参数表单数据
     */
    @JsonProperty("formItems")
    private List<FormItem> formItemList;



    /**
     * <p>参数表单项对象</p>
     *
     * @author : 王良俊
     * @date : 2021-10-28 10:49:40
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormItem extends SysServiceParamConfVo {

        public static FormItem createFormItem(SysServiceParamConfVo paramConfVo) {
            FormItem formItem = new FormItem();
            BeanUtils.copyProperties(paramConfVo, formItem);
            return formItem;
        }

        /**
         * 现在这里额外存储了根服务ID(因为参数数据结构是如此的)
         * 具体格式如下（这种格式原本是想给jackson的RequiredAt方法使用但是现在已经没必要了）：
         * DeviceNoObj/devNoRule/stairNoLen
         * */
        String props;

        /**
         * 根参数节点serviceId（如：DeviceNoObj）
         * */
        String rootServiceId;

        /**
         * 父参数节点的参数ID（这里是各个参数的ID如：devNoRule/stairNoLen）
         * */
        String parParamIds;

        /**
         * 异常提示的正则表达式
         */
        private String exceptionNoticeReg;

        /**
         * 异常提示内容
         */
        private String exceptionNotice;

        /**
         * 如 设备编号参数里面的编号规则参数就需要放到这里面，方便前端设置编号规则的标题
         */
        private List<FormItem> formItemList = new ArrayList<>();

        @JsonIgnore
        private LocalDateTime createTime;

        @JsonIgnore
        private LocalDateTime updateTime;

        // 这里只是为了重新注释一下

        /**
         * 表单验证的正则表达式
         */
        private String valueRange;

        /**
         * 表单验证未通过的提示内容
         */
        private String errorMsg;

        public void setException(String exceptionNoticeReg, String exceptionNotice) {
            this.exceptionNoticeReg = exceptionNoticeReg;
            this.exceptionNotice = exceptionNotice;
        }

        public void setFormValid(String valueRange, String errorMsg) {
            this.valueRange = valueRange;
            this.errorMsg = errorMsg;
        }


    }
}
