package com.aurine.cloudx.estate.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * @ClassName: JobConfig
 * @author: Administrator <>
 * @date:  2020年10月23日 下午01:46:32
 * @Copyright: 
*/
@Data
@Component
@RefreshScope
public class JobConfig {

	@Value("${cloudx.job.circle.visitor:20}")
	private Integer circle;
	
}
