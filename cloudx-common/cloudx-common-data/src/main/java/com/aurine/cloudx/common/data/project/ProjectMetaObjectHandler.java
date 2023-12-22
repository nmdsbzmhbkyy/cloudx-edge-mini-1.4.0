package com.aurine.cloudx.common.data.project;

import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import java.time.LocalDateTime;

/**
 * 项目字段填充
 *
 * @ClassName: ProjectMetaObjectHandler
 * @author: 林港 <ling@aurine.cn>
 * @date: 2020年5月7日 下午3:41:11
 * @Copyright:
 */
@Component
public class ProjectMetaObjectHandler implements MetaObjectHandler {

    /**
     * <p>MybatisPlus数据新增操作<p/>
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object projectId = getFieldValByName("projectId", metaObject);
        //当项目编码未赋值时，尝试从响应头中获取编码
        if (projectId == null) {
            Integer projId = ProjectContextHolder.getProjectId();

            if (projId != null) {
                setFieldValByName("projectId", projId, metaObject);
            }
        }

        // 填充操作者
        Object operator = getFieldValByName("operator", metaObject);

        Integer operatorId;
        try {
            operatorId = SecurityUtils.getUser().getId();
        } catch (Exception e) {
            operatorId = 1;
        }
        if (operator == null) {
            this.strictInsertFill(metaObject, "operator", Integer.class, operatorId);
        }


        // 填充操作者
        Object creator = getFieldValByName("creator", metaObject);
        Integer creatorId;
        try {
            creatorId = SecurityUtils.getUser().getId();
        } catch (Exception e) {
            creatorId = 1;
        }
        if (creator == null) {
            this.strictInsertFill(metaObject, "creator", Integer.class, creatorId);
        }


        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Integer operatorId;
        try {
            operatorId = SecurityUtils.getUser().getId();
        } catch (Exception e) {
            operatorId = 1;
        }

        Object operator = getFieldValByName("operator", metaObject);
        if (operator != null) {
            if (operatorId != null) {
                metaObject.setValue("operator", operatorId);
            }
        }

        Object ut = getFieldValByName("updateTime", metaObject);
        //置空 使用数据库时间
        if (ut != null) {
            metaObject.setValue("updateTime", null);
//            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
