package com.aurine.cloudx.common.data.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aurine.cloudx.common.data.project.ProjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.pig4cloud.pigx.common.data.tenant.PigxTenantHandler;

/**
 * @ClassName:  MybatisProjectConfiguration   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年5月7日 下午6:32:36      
 * @Copyright:
 */
@Configuration
public class MybatisProjectConfiguration {
    /**
     * 创建租户维护处理器对象
     *
     * @return 处理后的租户维护处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public ProjectHandler projectHandler() {
        return new ProjectHandler();
    }

	/**
	 * 分页插件
	 *
	 * @param tenantHandler 租户处理器
	 * @return PaginationInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "mybatisPlus.tenantAndProjectEnable", havingValue = "true", matchIfMissing = true)
	public PaginationInterceptor paginationInterceptor(PigxTenantHandler tenantHandler, ProjectHandler projectHandler) {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		List<ISqlParser> sqlParserList = new ArrayList<>();
		TenantSqlParser tenantSqlParser = new TenantSqlParser();
		tenantSqlParser.setTenantHandler(tenantHandler);
		sqlParserList.add(tenantSqlParser);
		TenantSqlParser projectSqlParser = new TenantSqlParser();
		projectSqlParser.setTenantHandler(projectHandler);
		sqlParserList.add(projectSqlParser);
		paginationInterceptor.setSqlParserList(sqlParserList);
		return paginationInterceptor;
	}
	
	/**
	 * 自定义拦截器实现模糊查询中的特殊字符处理
	 * @return
	 */
	@Bean
	public EscapeInterceptor escapeInterceptor() {
	    EscapeInterceptor escape = new EscapeInterceptor();
	    return escape;
	}
}
