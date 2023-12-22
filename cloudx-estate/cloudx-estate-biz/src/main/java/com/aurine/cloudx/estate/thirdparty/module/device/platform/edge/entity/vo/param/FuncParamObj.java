package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 服务器参数对象 json名：funcParam
 * </p>
 *
 * @ClassName: FuncParamObj
 * @author: 王良俊 <>
 * @date: 2020年11月25日 下午04:03:06
 * @Copyright:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("funcParam")
public class FuncParamObj {

    /**
     * 是否启用云电话	 0禁用（默认）、1启用
     */
    Integer cloudPhone;
    /**
     * 	是否启用TTS语音	 0禁用（默认）、1启用
     */
    Integer ttsVoice;
    /**
     * 是否启用云对讲留影留言		 0禁用（默认）、1启用
     */
    Integer cloudMsg;


}
