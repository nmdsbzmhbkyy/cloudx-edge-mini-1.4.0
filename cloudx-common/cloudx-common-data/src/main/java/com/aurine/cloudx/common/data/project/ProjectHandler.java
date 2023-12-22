package com.aurine.cloudx.common.data.project;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

/**
 * @ClassName:  ProjectHandler   
 * @author: 林港 <ling@aurine.cn>
 * @date:   2020年5月7日 下午6:09:28      
 * @Copyright:
 */
@Slf4j
public class ProjectHandler implements TenantHandler {
    @Autowired
    private ProjectConfigProperties properties;

	@Override
	public boolean doTableFilter(String tableName) {
		Integer projectId = ProjectContextHolder.getProjectId();
		if (projectId == null) {
			return Boolean.TRUE;
		}

		return !properties.getTables().contains(tableName);
	}

    @Override
    public Expression getTenantId(boolean where) {
        Integer projectId = ProjectContextHolder.getProjectId();
        log.debug("当前项目为 >> {}", projectId);

        if (projectId == null) {
            return new NullValue();
        }
        return new LongValue(projectId);
    }

    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }
}
