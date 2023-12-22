package com.aurine.cloudx.edge.sync.biz.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "sync")
@Data
@Getter
public class ImageConfig {

    /**
     * 按base64传输图片的项目
     */
    private List<Integer> imageList;


}
