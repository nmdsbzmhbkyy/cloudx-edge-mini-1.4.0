package com.aurine.cloudx.estate.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-05
 * @Copyright:
 */
@Configuration
public class MultipartSupportConfig {


	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;

	@Bean
	public Encoder feignFormEncoder() {
		return new SpringFormEncoder(new SpringEncoder(messageConverters));
	}

}
