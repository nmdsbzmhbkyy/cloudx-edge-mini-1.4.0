package com.aurine.cloudx.estate.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 图片配置
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-02
 * @Copyright:
 */
@RefreshScope
@Configuration
@ConfigurationProperties("image.watermark")
@Data
public class ImageConfig {
    private List<WaterMarkConfig> templates = new ArrayList<>();


    /**
     * 根据模板名称获取模板
     *
     * @param templateName
     * @return
     */
    public WaterMarkConfig getByTemplateName(String templateName) {
        if (CollUtil.isNotEmpty(templates)) {
            WaterMarkConfig result = null;
            for (WaterMarkConfig waterMarkConfig : templates) {
                if (waterMarkConfig.getName().equalsIgnoreCase(templateName)) {
                    result = waterMarkConfig;
                    break;
                }
            }
            if (result == null) {
                result = templates.stream().filter(e -> e.getIsDefault() == true).findFirst().get();
            }
            return result;
        } else {
            throw new NullPointerException("未找到水印模板配置");
        }

    }
}
