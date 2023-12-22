
package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonLabel;
import com.aurine.cloudx.estate.mapper.ProjectPersonLabelMapper;
import com.aurine.cloudx.estate.service.ProjectPersonLabelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员标签
 *
 * @author 王伟
 * @date 2020-05-13 10:42:38
 */
@Service
public class ProjectPersonLabelServiceImpl extends ServiceImpl<ProjectPersonLabelMapper, ProjectPersonLabel> implements ProjectPersonLabelService {


    @Override
    public boolean addLabel(String[] labelUidArray, String personId) {
        //删除该人员下的所有标签
        this.remove(new QueryWrapper<ProjectPersonLabel>().lambda().eq(ProjectPersonLabel::getPersonId, personId));


        //如果存在标签，则添加, 否则结束
        if (ArrayUtil.isNotEmpty(labelUidArray)) {
            List<ProjectPersonLabel> labelList = new ArrayList<>();
            ProjectPersonLabel labelObj = null;

            for (int i = 0; i < labelUidArray.length; i++) {
                labelObj = new ProjectPersonLabel();
                labelObj.setLabelId(labelUidArray[i]);
                labelObj.setPersonId(personId);
                labelList.add(labelObj);
            }

            this.saveBatch(labelList);

        }
        return true;
    }

    @Override
    public List<ProjectPersonLabel> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectPersonLabel>().lambda().eq(ProjectPersonLabel::getPersonId, personId));
    }
}
