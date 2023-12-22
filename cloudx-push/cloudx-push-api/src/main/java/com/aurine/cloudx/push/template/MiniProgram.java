package com.aurine.cloudx.push.template;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @ClassName: MiniProgram
 * @author: 邹宇
 * @date: 2021-8-24 14:39:14
 * @Copyright:
 */
@Data
public class MiniProgram {
    /**
     * appid
     */
    private String appid;
    /**
     * pagepath
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String path;
}
