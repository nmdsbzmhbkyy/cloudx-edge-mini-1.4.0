package com.aurine.cloudx.estate;

import com.aurine.cloudx.estate.service.ProjectLabelConfigService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class test {
	@Resource
	private ProjectLabelConfigService projectLabelConfigService;
	@Resource
	private ProjectPersonDeviceService projectPersonDeviceService;

	@Test
	void h() {
		List projectLabelConfigList = projectLabelConfigService.list();
		System.out.println("123");
	}
	@Test
	void h1() {
		List projectLabelConfigList = projectPersonDeviceService
				.listDeviceByPersonId("801a0cb793f211ee96a8ecd68afab98e");
		System.out.println("123");
	}
}
