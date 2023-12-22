package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
import com.aurine.cloudx.open.origin.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.aurine.cloudx.open.origin.mapper.ProjectCardMapper;
import com.aurine.cloudx.open.origin.service.ProjectCardService;
import com.aurine.cloudx.open.origin.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 记录项目卡资源信息，供辖区内已开放通行权限的卡识别设备下载
 * </p>
 *
 * @author 王良俊
 * @date 2020-05-22 11:21:18
 */
@Service
@Primary
public class ProjectCardServiceImpl extends ServiceImpl<ProjectCardMapper, ProjectCard> implements ProjectCardService {

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;


    @Override
    public boolean removeByPersonId(String personId) {
        List<ProjectCard> cardList = this.list(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getPersonId, personId));
        if (CollUtil.isNotEmpty(cardList)) {
            cardList.forEach(projectCard -> {
                projectCard.setStatus(PassRightTokenStateEnum.UNUSE.code);
                projectCard.setPersonType("");
                projectCard.setPersonId("");
                String cardId = projectCard.getCardId();
                // 删除卡片和设备的权限关系
                if (StrUtil.isNotBlank(cardId)) {
                    projectRightDeviceService.removeCertmedia(cardId);
                }
            });
            return this.updateBatchById(cardList);
        }
        return true;
    }


    /**
     * 删除卡
     *
     * @param cardId
     * @return
     */
    @Override
    public boolean delCard(String cardId) {

        ProjectCard card = this.getById(cardId);

        card.setCardId(cardId);
        card.setStatus(PassRightTokenStateEnum.UNUSE.code);
        card.setPersonType("");
        card.setPersonId("");
        //如果是实时删除数据库中的卡片与人员的关系则要删除其与设备的关系
        projectRightDeviceService.removeCertmedia(cardId);
        return this.updateById(card);
    }

    @Override
    public void operateCard(List<ProjectCard> cardList, String visitorId) {
        // 卡片操作
        List<ProjectCard> oldCardList = this.list(new QueryWrapper<ProjectCard>()
                .lambda().eq(ProjectCard::getPersonId, visitorId));
        // 作为存储在本次操作中被移除的卡片id
        List<String> beRemoveCardList = oldCardList.stream().map(ProjectCard::getCardId).collect(Collectors.toList());
        // 存储本次操作中未被移除的卡片id
        List<String> unRemoveCardList = new ArrayList<>();
        // 存储本次新增加的卡片对象列表用于后面进行存储操作
        List<ProjectCard> beSaveCardList = new ArrayList<>();
        // 循环筛选出未被删除的和要被保存的卡片
        cardList.forEach(projectCard -> {
            // 通过是否有卡片id判断是否是新添加的卡片还是原本就是从数据库中取出该人员的卡片
            if (StrUtil.isNotBlank(projectCard.getCardId())) {
                unRemoveCardList.add(projectCard.getCardId());
            } else {
                projectCard.setStatus(PassRightTokenStateEnum.USED.code);
                beSaveCardList.add(projectCard);
            }
        });
        // 通过去除未被删除的卡片获得这次操作中被删除的卡片（原本就存在于人员和介质关系中的卡片但是本次被删除了）
        beRemoveCardList.removeAll(unRemoveCardList);

        ProjectCard card = new ProjectCard();
        card.setStatus(PassRightTokenStateEnum.UNUSE.code);
        card.setPersonType("");
        card.setPersonId("");
        if (CollUtil.isNotEmpty(beRemoveCardList)) {
            // 根据id移除被删除的卡片与人员之间的关联
            this.update(card, new QueryWrapper<ProjectCard>().lambda().in(ProjectCard::getCardId, beRemoveCardList));
        }
        // 要被保存的卡片中可能存在已经在数据库中但是没有关联人员的卡片在这里对其进行关联操作(关联其personId为本次用户)，另外就是完全新的加入的卡片在本次操作中添加到数据库并关联人员
        beSaveCardList.forEach(projectCard -> {
            projectCard.setPersonId(visitorId);
            this.saveCard(projectCard);
        });
        // 删除卡片权限，删除本次操作中被删除卡片的设备权限记录
        projectRightDeviceService.removeCertmedias(beRemoveCardList);
    }

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
    public ProjectCard saveOrUpdateCard(ProjectCard card) {
        ProjectCard cardEntity = getCardByNo(card.getCardNo());

        if (cardEntity == null) {//卡不存在，直接添加
            this.save(card);
            return card;
        } else {
            if (card.getStatus().equals(PassRightTokenStateEnum.FREEZE.code)) {
                throw new RuntimeException("当前卡已被冻结");
            } else {
                //更新人员信息到卡片
                cardEntity.setPersonId(card.getPersonId());
                cardEntity.setPersonType(card.getPersonType());
                cardEntity.setStatus(PassRightTokenStateEnum.USED.code);
                this.updateById(cardEntity);
                return cardEntity;
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

    @Override
    public Page<CardInfoVo> page(Page page, CardInfoVo vo) {
        ProjectCard po = new ProjectCard();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
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
