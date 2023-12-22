package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectCardDto;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.mapper.ProjectCardMapper;
import com.aurine.cloudx.estate.service.OpenApiProjectCardService;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/05/23 15:15
 * @Package: com.aurine.openv2.service.impl
 * @Version: 1.0
 * @Remarks: 卡信息管理，因涉及事务，调用本类方法使用代理
 **/
@Service
public class OpenApiProjectCardServiceImpl extends ServiceImpl<ProjectCardMapper, ProjectCard> implements OpenApiProjectCardService {

    @Resource
    private OpenApiProjectPersonDeviceService openApiProjectPersonDeviceService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private OpenApiProjectCardServiceImpl openApiProjectCardServiceImpl;

    /**
     * 复合型接口
     * 1、保存/更新卡信息
     * 2、添加通行权限
     *
     * @param projectCardDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<OpenApiProjectCardDto> saveCardInfo(OpenApiProjectCardDto projectCardDto) {
        // 保存卡片信息
        ProjectCard projectCard = openApiProjectCardServiceImpl.saveCard(projectCardDto);

        // 添加设备上卡通行权限
        Boolean savePassRightResult = openApiProjectPersonDeviceService.addPersonPassRightDevice(projectCardDto.getPersonId(), projectCardDto.getPersonType(), projectCardDto.getPlanId());

        if (!savePassRightResult) {
            throw new OpenApiServiceException(String.format("卡片[%s]新增下发失败", projectCardDto.getCardNo()));
        }

        // 构建返回值
        OpenApiProjectCardDto respProjectCardDto = new OpenApiProjectCardDto();
        BeanUtil.copyProperties(projectCard, respProjectCardDto);

        return R.ok(respProjectCardDto);
    }

    /**
     * 单一接口
     * 保存卡片信息到数据库
     *
     * @param projectCardDto
     * @return
     */
    private ProjectCard saveCard(OpenApiProjectCardDto projectCardDto) {
        ProjectCard projectCard = new ProjectCard();

        // 查询卡号是否已被使用
        ProjectCard cardEntity = getCardByNo(projectCardDto.getCardNo());

        if (BeanUtil.isEmpty(cardEntity)) {
            // 卡不存在直接添加
            BeanUtil.copyProperties(projectCardDto, projectCard);
            projectCard.setStatus(PassRightTokenStateEnum.USED.code);

            boolean saveCardResult = super.save(projectCard);

            if (!saveCardResult) {
                throw new OpenApiServiceException(String.format("卡片[%s]新增失败", projectCardDto.getCardNo()));
            }

        } else {
            // 卡已存在, 校验
            if (cardEntity.getStatus().equals(PassRightTokenStateEnum.USED.code)) {
                throw new OpenApiServiceException(String.format("当前卡号[%s]已被使用", projectCardDto.getCardNo()));
            } else if (cardEntity.getStatus().equals(PassRightTokenStateEnum.FREEZE.code)) {
                throw new OpenApiServiceException(String.format("当前卡[%s]已被冻结", projectCardDto.getCardNo()));
            } else {
                // 更新人员信息到卡片
                cardEntity.setPersonId(projectCardDto.getPersonId());
                cardEntity.setPersonType(projectCardDto.getPersonType());
                cardEntity.setStatus(PassRightTokenStateEnum.USED.code);

                boolean updateResult = super.updateById(cardEntity);

                if (!updateResult) {
                    throw new OpenApiServiceException(String.format("卡片[%s]新增失败", projectCardDto.getCardNo()));
                }

            }

            BeanUtil.copyProperties(cardEntity, projectCard);
        }

        return projectCard;
    }

    /**
     * 删除卡片，本质上是解绑卡人员关系, 伪删除
     *
     * @param projectCardDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> unbindPersonCardRelation(OpenApiProjectCardDto projectCardDto) {

        // 删除卡信息
        ProjectCard projectCard = openApiProjectCardServiceImpl.deleteCard(projectCardDto);

        // 删除卡设备关系
        boolean removeCertMediaResult = projectRightDeviceService.removeCertmedia(projectCard.getCardId());

        if (!removeCertMediaResult) {
            throw new OpenApiServiceException(String.format("卡片[%s]删除下发失败", projectCardDto.getCardNo()));
        }

        return R.ok(projectCard.getPersonId());
    }

    /**
     * 解绑人员卡片关系
     * 返回卡信息对象
     *
     * @param projectCardDto
     * @return
     */
    private ProjectCard deleteCard(OpenApiProjectCardDto projectCardDto) {
        String personTypeName = PersonTypeEnum.getType(projectCardDto.getPersonType());

        ProjectCard card = getCardByNo(projectCardDto.getCardNo());

        if (card == null) {
            throw new OpenApiServiceException(String.format("卡号为%s的门禁卡不存在", projectCardDto.getCardNo()));
        }

        if (!card.getPersonId().equals(projectCardDto.getPersonId())) {
            throw new OpenApiServiceException(String.format("该%s未拥有卡号为[%s]的门禁卡", personTypeName, projectCardDto.getCardNo()));
        }

        card.setStatus(PassRightTokenStateEnum.UNUSE.code);
        card.setPersonType("");
        card.setPersonId("");

        boolean delCardResult = this.updateById(card);

        if (!delCardResult) {
            throw new OpenApiServiceException(String.format("删除卡号为[%s]的%s门禁卡失败", projectCardDto.getCardNo(), personTypeName));
        }

        return card;
    }

    /**
     * 根据卡号获取卡片
     *
     * @param cardNo
     * @return
     */
    private ProjectCard getCardByNo(String cardNo) {
        List<ProjectCard> cardList = this.list(new QueryWrapper<ProjectCard>().lambda()
                .eq(ProjectCard::getCardNo, cardNo)
        );

        if (CollUtil.isNotEmpty(cardList)) {
            return cardList.get(0);
        }

        return null;
    }

}
