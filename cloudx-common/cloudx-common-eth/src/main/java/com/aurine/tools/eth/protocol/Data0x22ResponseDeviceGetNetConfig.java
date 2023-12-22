package com.aurine.tools.eth.protocol;

import cn.hutool.crypto.SecureUtil;
import com.aurine.tools.eth.protocol.pojo.NetParams;
import com.aurine.tools.eth.protocol.pojo.NoInfo;
import com.aurine.tools.eth.protocol.pojo.NoRules;
import lombok.Data;

/**
 * @author chensl
 * @date 2022-06-10
 */
@Data
public class Data0x22ResponseDeviceGetNetConfig {
    private NetParams netParams;
    private int language;
    private NoInfo noInfo;
    private NoRules noRules;
    private String[] sectionDesc;
    private String checkValue;

    public void updateCheckValue(String mac){
//        校验值算法： checkValue = (ip + mask + gateway + dns1 + dns2 + centerIp + string(lanuage) +
//                string(type) + string(areaNo) + no + string(stairNolen) + string(roomNoLen) + string(cellNoLen) +
//                string(useCellNo) + subsection + sectionDesc[0]...sectionDesc[n] + mac)md5-32(lower)
        String modelStr = netParams.getIp() + netParams.getMask() + netParams.getGateway() + netParams.getDns1() + netParams.getDns2() +
                netParams.getCenterIp() + language + noInfo.getType() + noInfo.getAreaNo() + noInfo.getNo() +
                noRules.getStairNoLen() + noRules.getRoomNoLen() + noRules.getCellNoLen() + noRules.getUseCellNo() +
                noRules.getSubSection();
        for(int i = 0;  i < sectionDesc.length ; i ++){ //sectionDesc != null
            modelStr += sectionDesc[i];
        }
        modelStr += mac;
        checkValue = SecureUtil.md5(modelStr);
    }
}
