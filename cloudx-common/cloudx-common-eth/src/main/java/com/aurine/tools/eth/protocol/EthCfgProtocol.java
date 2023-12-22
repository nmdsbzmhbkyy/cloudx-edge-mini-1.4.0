package com.aurine.tools.eth.protocol;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.tools.eth.protocol.constants.Commands;
import com.aurine.tools.eth.protocol.constants.Directs;

/**
 * @author chensl
 * @date 2022-06-10
 */
public class EthCfgProtocol {
    public static EthCfgData<String> parse(String jsonStr){
        if(!JSONUtil.isJson(jsonStr)){
            System.out.println("非json对象");
            return null;
        }

        JSONObject jobj = JSONUtil.parseObj(jsonStr);
        if(jobj == null){
            System.out.println("非json对象 null");
            return null;
        }
        if(!(jobj.containsKey("msgId") && jobj.containsKey("commandId") && jobj.containsKey("srcType") && jobj.containsKey("direct"))){
            System.out.println("缺少必需数据域");
            return null;
        }
        String cmdIdStr = jobj.get("commandId").toString();
        if(!isNumeric(cmdIdStr)){
            System.out.println("数据格式错误");
            return null;
        }
        int cmdId = Integer.parseInt(cmdIdStr);
        if(!(cmdId == Commands.Cmd0x21Broadcast || cmdId == Commands.Cmd0x22GetNetConfig || cmdId == Commands.CmdCmd0x23UpdateConfigResult)){
            System.out.println("命令码错误");
            return null;
        }
        EthCfgData<String> ecdResult = JSONUtil.toBean(jsonStr, new TypeReference<EthCfgData<String>>(){}, false);
//        if(cmdId == Commands.Cmd0x21Broadcast){
//            ecdResult = JSONUtil.toBean(jsonStr, new TypeReference<EthCfgData<String>>(){}, false);
//        }else if(cmdId == Commands.Cmd0x22GetNetConfig){
//            ecdResult = JSONUtil.toBean(jsonStr, new TypeReference<EthCfgData<String>>(){}, false);
//        }else if(cmdId == Commands.CmdCmd0x23UpdateConfigResult){
//            ecdResult = JSONUtil.toBean(jsonStr, new TypeReference<EthCfgData<String>>(){}, false);
//        }else{
//            System.out.println("命令码错误");
//            return null;
//        }
        return ecdResult;
    }



    public static <T> boolean createResponse(EthCfgData request, EthCfgData<T> response, int echoCode){
        if(request.getDirect() != Directs.Request){
            return false;
        }
        response.setMsgId(request.getMsgId());
        response.setCommandId(request.getCommandId());
        response.setSrcType(request.getSrcType());
        response.setDirect(Directs.Response);
        response.setSessionId(request.getSessionId());
        response.setEchoCode(echoCode);
        //response.setEchoCode(request.getEchoCode());
        //response.setBody(request.getBody());
        return true;
    }

    private final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }
}
