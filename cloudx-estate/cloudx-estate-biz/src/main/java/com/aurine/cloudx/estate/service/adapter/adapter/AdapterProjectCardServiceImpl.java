package com.aurine.cloudx.estate.service.adapter.adapter;

import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.service.ProjectCardService;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectCardService;
import com.aurine.cloudx.estate.util.DockModuleConfigUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AdapterWebProjectCardServiceImpl 适配器
 * 用于根据配置适配合适的业务实现，并允许多个业务实现顺序执行
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-14
 * @Copyright:
 */
@Service("abstractProjectCardService")
public class AdapterProjectCardServiceImpl extends AbstractProjectCardService {
    @Resource
    ProjectCardService wr20ProjectCardServiceImplV1;
    @Resource
    DockModuleConfigUtil dockModuleConfigUtil;


    /**
     * 保存卡片
     *
     * @param card
     * @return
     * @author: 王伟
     */
    @Override
    public boolean saveCard(ProjectCard card){
        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectCardServiceImplV1.saveCard(card);
        } else {

        }

        return super.saveCard(card);
    }

    /**
     * 删除卡
     *
     * @param cardId
     * @return
     */
    @Override
    public boolean delCard(String cardId) {

        //根据配置 调用对应实现类
        if (dockModuleConfigUtil.isWr20()) {
            return wr20ProjectCardServiceImplV1.delCard(cardId);
        } else {

        }

        return super.delCard(cardId);
    }

}
