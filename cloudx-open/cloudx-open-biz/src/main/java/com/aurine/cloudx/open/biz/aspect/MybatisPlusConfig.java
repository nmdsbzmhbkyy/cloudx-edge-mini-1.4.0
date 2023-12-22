package com.aurine.cloudx.open.biz.aspect;

import com.aurine.cloudx.open.common.core.util.RedisUtils;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author huan
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@Component
public class MybatisPlusConfig {
    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());

        // 创建租户SQL解析器
        TenantSqlParser tenantSqlParser = new TenantSqlParser();

        // 设置租户处理器
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean select) {
                // 设置当前租户ID，实际情况你可以从cookie、或者缓存中拿都行。匹配数据库中的数据

                Integer projectId = ProjectContextHolderThird.getProjectId();
                log.info("当前项目为{}", Objects.requireNonNull(projectId));
                return new LongValue(Objects.requireNonNull(projectId));
            }

            @Override
            public String getTenantIdColumn() {
                // 对应数据库租户ID的列名，是数据库列名，不是实体类
                return "projectId";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                Object o = RedisUtils.get("sql");
                // 是否需要需要过滤某一张表，false为不过滤
                if(null == o) {
                    return true;
                }
                return false;
            }
        });
        TenantSqlParser tenantSqlParser2 = new TenantSqlParser();

        tenantSqlParser2.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean select) {
                // 设置当前租户ID，实际情况你可以从cookie、或者缓存中拿都行。匹配数据库中的数据
                log.info("当前租户为{}", Objects.requireNonNull(1));
                return new LongValue(Objects.requireNonNull(1));
            }

            @Override
            public String getTenantIdColumn() {
                // 对应数据库租户ID的列名，是数据库列名，不是实体类
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                Object o = RedisUtils.get("sql");
                if(null == o) {
                    return true;
                }
                // 是否需要需要过滤某一张表，false为不过滤
                return false;
            }
        });
        sqlParserList.add(tenantSqlParser);
        sqlParserList.add(tenantSqlParser2);
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}