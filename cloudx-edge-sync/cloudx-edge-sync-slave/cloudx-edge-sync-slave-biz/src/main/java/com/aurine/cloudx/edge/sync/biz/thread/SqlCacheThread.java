package com.aurine.cloudx.edge.sync.biz.thread;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.common.config.GlobalVariable;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.utils.SqlCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * @description: Sql缓存处理线程
 * @author: wangwei
 * @date: 2021/12/27 17:14
 **/
@Slf4j
@Component
@Order(5)
@Async
public class SqlCacheThread implements ApplicationRunner {

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private GlobalVariable globalVariable;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("SQL缓存线程已启动");

		while (true) {
			List<TaskInfo> list = SqlCacheUtil.list();
			if (CollUtil.isNotEmpty(list)) {
				log.info("[清空SQL缓存] 定期入库SQL {}", list.size());
                taskInfoService.saveBatch(list);
			}

			Thread.sleep(globalVariable.getSqlCacheFlushRate());
		}

	}
}
