package com.aurine.cloudx.common.core.util.canal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @ClassName: CanalConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24 8:37
 * @Copyright:
 */
@Data
public class CanalConfig {

    private String host;

    private int port;

    private String destination;

    private String username;

    private String password;

    private Integer batchSize;

    private String subscribeRegex;
}
