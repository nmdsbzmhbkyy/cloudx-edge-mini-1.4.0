package com.aurine.cloudx.edge.sync.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/01/06 20:40
 * @Package: com.aurine.cloudx.edge.sync.biz.config
 * @Version: 1.0
 * @Remarks:
 **/
@Configuration
@Data
@Getter
public class GlobalVariable {

    /**
     * appId
     */
    @Value("${init.appId}")
    private String appId;

    /**
     * 版本
     */
    @Value("${init.topic.version}")
    private String version;

    /**
     * 本机回调端口
     */
    @Value("${server.port}")
    private String port;

    /**
     * 转发端口号
     */
    @Value("${init.transfer.port}")
    private String edgeSyncTransferPort;

    /**
     * 图片地址源
     */
    @Value("${server.base-uri}")
    private String imgUri;

    /**
     * 数据库批量入库数据阈值
     */
    @Value("${init.sqlCacheSize}")
    private long sqlCacheSize;

    /**
     * 数据库批量入库数据阈值
     */
    @Value("${init.sqlCacheFlushRate}")
    private long sqlCacheFlushRate;
}
