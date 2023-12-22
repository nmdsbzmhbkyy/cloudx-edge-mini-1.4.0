package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.mapper.ProjectCardMapper;
import com.aurine.cloudx.estate.service.ProjectCardService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
 * </p>
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:18
 */
@Service
public class ProjectCardServiceImpl extends ServiceImpl<ProjectCardMapper, ProjectCard> implements ProjectCardService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    /**
     * 保存卡片
     *
     * @param card
     * @return
     * @author: 王伟
     */
    @Override
    public boolean saveCard(ProjectCard card) {
//        ProjectCard cardEntity = getByCardId(card.getCardId());
        ProjectCard cardEntity = getCardByNo(card.getCardNo());

        if (cardEntity == null) {//卡不存在，直接添加
            return this.save(card);
        } else {
            //卡已存在, 校验
            if (cardEntity.getStatus().equals(PassRightTokenStateEnum.USED.code)) {
                throw new RuntimeException("当前卡号已被其他用户使用");
            } else if (card.getStatus().equals(PassRightTokenStateEnum.FREEZE.code)) {
                throw new RuntimeException("当前卡已被冻结");
            } else {
                //更新人员信息到卡片
                cardEntity.setPersonId(card.getPersonId());
                cardEntity.setPersonType(card.getPersonType());
                cardEntity.setStatus(PassRightTokenStateEnum.USED.code);
                return this.updateById(cardEntity);
            }

        }
    }

    /**
     * 保存或更新卡片，如果已卡片被占用，则更新为最新的人员
     *
     * @param card
     * @return
     * @author: 王伟
     */
    @Override
    public boolean saveOrUpdateCard(ProjectCard card) {
        ProjectCard cardEntity = getCardByNo(card.getCardNo());

        if (cardEntity == null) {//卡不存在，直接添加
            return this.save(card);
        } else {
             if (card.getStatus().equals(PassRightTokenStateEnum.FREEZE.code)) {
                throw new RuntimeException("当前卡已被冻结");
            } else {
                //更新人员信息到卡片
                cardEntity.setPersonId(card.getPersonId());
                cardEntity.setPersonType(card.getPersonType());
                cardEntity.setStatus(PassRightTokenStateEnum.USED.code);
                return this.updateById(cardEntity);
            }

        }
    }

    /**
     * 修改卡片
     *
     * @param card
     * @return
     * @author: 王伟
     */
    @Override
    public boolean updateCard(ProjectCard card) {
        ProjectCard cardEntity = getByCardId(card.getCardId());

        if (BeanUtil.isEmpty(cardEntity)) {//卡不存在，直接添加
            return this.save(card);
        } else {
            //卡已存在, 校验
            if (card.getStatus().equals(PassRightTokenStateEnum.USED.code)) {
                throw new RuntimeException("当前卡号已被其他用户使用");
            } else if (card.getStatus().equals(PassRightTokenStateEnum.FREEZE.code)) {
                throw new RuntimeException("当前卡已被冻结");
            } else {
                //更新人员信息到卡片
                cardEntity.setPersonId(card.getPersonId());
                cardEntity.setPersonType(card.getPersonType());
                cardEntity.setStatus(PassRightTokenStateEnum.USED.code);
                return this.updateById(cardEntity);
            }

        }
    }

    /**
     * 检查卡片是否已存在
     *
     * @param cardId
     * @return
     */
    @Override
    public ProjectCard getByCardId(String cardId) {
        List<ProjectCard> cardList = this.list(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getCardId, cardId));
        if (CollectionUtil.isEmpty(cardList)) {
            return null;
        } else {
            return cardList.get(0);
        }
    }

    @Override
    public List<ProjectCard> listByPersonId(String personId) {
        return this.list(new QueryWrapper<ProjectCard>().lambda()
                .eq(ProjectCard::getPersonId, personId).eq(ProjectCard::getStatus, PassRightTokenStateEnum.USED.code));
    }

    @Override
    public List<ProjectCard> listByPersonIdList(List<String> personIdList) {
        if (CollUtil.isNotEmpty(personIdList)) {
            List<ProjectCard> cardList = this.list(new QueryWrapper<ProjectCard>().lambda().in(ProjectCard::getPersonId, personIdList));
            return cardList;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateCardBatch(String personId, List<String> cardIdList) {
        ProjectCard projectCard = new ProjectCard();
        projectCard.setPersonId(personId);
        this.update(projectCard, new QueryWrapper<ProjectCard>().lambda().in(ProjectCard::getCardId, cardIdList));
        return false;
    }

    @Override
    public boolean updateCardById(String personId, String cardId) {
        ProjectCard projectCard = new ProjectCard();
        projectCard.setStatus(PassRightTokenStateEnum.USED.code);
        projectCard.setPersonId(personId);
        projectCard.setCardId(cardId);
        return this.updateById(projectCard);
    }

    /**
     * 根据第三方id和项目编号，获取卡片
     *
     * @param code
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-08-20
     */
    @Override
    public ProjectCard getByCode(String code, int projectId) {
        return this.baseMapper.getByCode(projectId, code);
    }

    /**
     * 根据卡号和项目编号，获取卡片
     *
     * @param cardNo
     * @param projectId
     * @return
     * @author: 王伟
     * @since 2020-11-30
     */
    @Override
    public ProjectCard getByCardNo(String cardNo, int projectId) {
        return this.baseMapper.getByCardNo(projectId, cardNo);
    }

    /**
     * 验证卡号是否已存在
     *
     * @param cardNo
     * @return
     * @author: 王伟
     * @since 2020-08-19
     */
    private boolean checkCardExist(String cardNo) {
        int count = this.count(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getCardNo, cardNo));
        return count >= 1;
    }

    /**
     * 根据卡号获取卡片
     *
     * @param cardNo
     * @return
     * @author: 王伟
     * @since 2020-08-19
     */
    protected ProjectCard getCardByNo(String cardNo) {
        List<ProjectCard> cardList = this.list(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getCardNo, cardNo));
        if (CollUtil.isNotEmpty(cardList)) {
            return cardList.get(0);
        }
        return null;
    }

}
