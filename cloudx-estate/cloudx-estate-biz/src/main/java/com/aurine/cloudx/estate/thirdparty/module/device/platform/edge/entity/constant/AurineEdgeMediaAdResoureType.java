package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant;

import com.aurine.cloudx.estate.constant.MediaResourceTypeConstant;
import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-10-10
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEdgeMediaAdResoureType {

    IMAGE(MediaResourceTypeConstant.IMAGE, "1","图片"),
    VIDEO(MediaResourceTypeConstant.VIDEO, "2","视频"),
    SOUND(MediaResourceTypeConstant.SOUND, "2","音频");

    public String systemCode;
    public String edgeCode;
    public String desc;



    public static AurineEdgeMediaAdResoureType getBySystemCode(String systemCode) {
        AurineEdgeMediaAdResoureType[] enums = values();
        for (AurineEdgeMediaAdResoureType item : enums) {
            if (item.systemCode.equals(systemCode)) {
                return item;
            }
        }
        return null;
    }

}
