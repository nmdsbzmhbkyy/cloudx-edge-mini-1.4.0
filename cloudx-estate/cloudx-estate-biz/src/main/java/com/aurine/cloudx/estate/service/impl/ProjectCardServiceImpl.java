package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectCardMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectCardVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectProprietorDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private ProjectCardHisService projectCardHisService;

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    @Resource
    private ProjectVisitorService projectVisitorService;

    @Resource
    private ProjectProprietorDeviceProxyService projectProprietorDeviceProxyService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;



    @Override
    public boolean removeByPersonId(String personId) {
        List<ProjectCard> cardList = this.list(new QueryWrapper<ProjectCard>().lambda().eq(ProjectCard::getPersonId, personId));
        if (CollUtil.isNotEmpty(cardList)) {
            List<String> list = getPhoneAndPersonName(cardList.get(0));
            cardList.forEach(projectCard -> {
                int count = projectRightDeviceService.count(Wrappers.lambdaQuery(ProjectRightDevice.class).eq(ProjectRightDevice::getCertMediaInfo, projectCard.getCardNo()));
                saveCardHis(projectCard.getCardNo(),projectCard.getPersonType(),list.get(0),CardOperationTypeEnum.REMOVE_CARD.code,list.get(1),count==0,count,personId);
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
        //projectRightDeviceService.removeCertmedia(cardId);
        RedisUtil.lSet("DelCardList", ListUtil.toList(cardId));
        try {
            String phone = "";
            String personName = "";
            if (card.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code)) {
                ProjectPersonInfo personInfo = projectPersonInfoService.getById(card.getPersonId());
                phone = personInfo.getTelephone();
                personName = personInfo.getPersonName();
            } else if (card.getPersonType().equals(PersonTypeEnum.STAFF.code)) {
                ProjectStaff staff = projectStaffService.getById(card.getPersonId());
                phone = staff.getMobile();
                personName = staff.getStaffName();
            } else if (card.getPersonType().equals(PersonTypeEnum.VISITOR.code)) {
                ProjectVisitor visitor = projectVisitorService.getById(card.getPersonId());
                phone = visitor.getMobileNo();
                personName = visitor.getPersonName();
            }
            saveCardHis(card.getCardNo(),card.getPersonType(),phone,CardOperationTypeEnum.REMOVE_CARD.code,personName,true,0,card.getPersonId());
        } catch (Exception e) {
            log.error("添加卡删除的操作记录出现异常",e);
        }
        return this.updateById(card);
    }

    /**
     * 根据卡信息获取手机和人名
     * @param card 卡片信息
     * @return 结果列表 【0】手机号 【1】人名
     */
    private List<String> getPhoneAndPersonName(ProjectCard card) {
        if (card != null) {
            List<String> resultList = new ArrayList<>();

            if (card.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code)) {
                //住户
                ProjectPersonInfo personInfo = projectPersonInfoService.getOne(Wrappers.lambdaQuery(ProjectPersonInfo.class).eq(ProjectPersonInfo::getPersonId, card.getPersonId()));
                resultList.add(personInfo.getTelephone());
                resultList.add(personInfo.getPersonName());
            } else if (card.getPersonType().equals(PersonTypeEnum.STAFF.code)) {
                //员工
                ProjectStaff projectStaff = projectStaffService.getOne(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getStaffId, card.getPersonId()));
                resultList.add(projectStaff.getMobile());
                resultList.add(projectStaff.getStaffName());
            } else if (card.getPersonType().equals(PersonTypeEnum.VISITOR.code)) {
                //访客
                ProjectVisitorHis projectVisitorHis = projectVisitorHisService.getOne(Wrappers.lambdaQuery(ProjectVisitorHis.class).eq(ProjectVisitorHis::getVisitId, card.getPersonId()));
                ProjectVisitor projectVisitor = projectVisitorService.getOne(Wrappers.lambdaQuery(ProjectVisitor.class).eq(ProjectVisitor::getVisitorId, projectVisitorHis.getVisitorId()));
                resultList.add(projectVisitor.getMobileNo());
                resultList.add(projectVisitor.getPersonName());
            }
            return resultList;
        }
        return null;
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
                cardEntity.setCardStatus("1");
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

    @Override
    public List<ProjectCardVo> getCardAttribution(String cardNo) {
        ProjectCard projectCard = getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, cardNo));
        if(projectCard == null){
            return null;
        }
        List<ProjectCardVo> projectCardVoList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        //人员类型 1 住户 2 员工 3 访客
        if(projectCard.getPersonType().equals(PersonTypeEnum.PROPRIETOR.code)){
            ProjectCardVo projectCardVo = new ProjectCardVo();
            ProjectPersonInfo personInfo = projectPersonInfoService.getOne(Wrappers.lambdaQuery(ProjectPersonInfo.class).eq(ProjectPersonInfo::getPersonId, projectCard.getPersonId()));
            List<String> houseParam = projectHouseInfoService.getHouseParam(projectCard.getPersonId());
            if(CollUtil.isNotEmpty(houseParam)){
                for (String houseId : houseParam) {
                    List<String> addressList = projectHouseInfoService.getAddress(houseId);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = addressList.size() - 1; i >= 0; i--) {
                        stringBuilder.append(addressList.get(i));
                    }
                    list.add(String.valueOf(stringBuilder));
                }
                projectCardVo.setAddress(list);
            }
            projectCardVo.setCardNo(cardNo);
            projectCardVo.setPersonType(projectCard.getPersonType());
            projectCardVo.setPhone(personInfo.getTelephone());
            projectCardVo.setName(personInfo.getPersonName());
            projectCardVoList.add(projectCardVo);
        }else if(projectCard.getPersonType().equals(PersonTypeEnum.STAFF.code)){
            ProjectCardVo projectCardVo = new ProjectCardVo();
            ProjectStaff projectStaff = projectStaffService.getOne(Wrappers.lambdaQuery(ProjectStaff.class).eq(ProjectStaff::getStaffId, projectCard.getPersonId()));
            projectCardVo.setName(projectStaff.getStaffName());
            projectCardVo.setCardNo(cardNo);
            projectCardVo.setPersonType(projectCard.getPersonType());
            projectCardVo.setPhone(projectStaff.getMobile());
            String deptName = projectStaffService.getDeptName(projectStaff.getDepartmentId());
            list.add(deptName);
            projectCardVo.setAddress(list);
            projectCardVoList.add(projectCardVo);
        } else if(projectCard.getPersonType().equals(PersonTypeEnum.VISITOR.code)){
            ProjectCardVo projectCardVo = baseMapper.getVisitorInfo(cardNo);
            if(projectCardVo != null && StrUtil.isNotEmpty(projectCardVo.getHouseId())){
                List<String> addressList = projectHouseInfoService.getAddress(projectCardVo.getHouseId());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = addressList.size() - 1; i >= 0; i--) {
                    stringBuilder.append(addressList.get(i));
                }
                list.add(String.valueOf(stringBuilder));
                projectCardVo.setAddress(list);
                projectCardVoList.add(projectCardVo);
            }

        }
        return projectCardVoList;
    }

    /**
     * 分页查询门禁卡
     *
     * @param page
     * @param projectCardVo
     * @return
     */
    public Page<ProjectCardVo> cardPage(Page page, ProjectCardVo projectCardVo) {
        return baseMapper.cardPage(page, projectCardVo, ProjectContextHolder.getProjectId());
    }

    /**
     * 挂失卡
     *
     * @param cardNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> loseCard(String cardNo) {
        //修改卡状态为挂失
        update(Wrappers.lambdaUpdate(ProjectCard.class).set(ProjectCard::getCardStatus, "2").eq(ProjectCard::getCardNo, cardNo));

        //挂失
        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class)
                .eq(ProjectRightDevice::getCertMediaInfo, cardNo));
        boolean bool = projectRightDeviceService.delCerts(rightDeviceList);
        ProjectRightDevice projectRightDevice;

        //添加操作记录
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            projectRightDevice = rightDeviceList.get(0);
            saveCardHis(cardNo, projectRightDevice.getPersonType(), projectRightDevice.getMobileNo(), CardOperationTypeEnum.LOSE_CARD.code, projectRightDevice.getPersonName(), false, rightDeviceList.size(),projectRightDevice.getPersonId());
        } else {
            ProjectCard card = getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, cardNo).last("LIMIT 1"));
            List<String> list = getPhoneAndPersonName(card);
            if (CollUtil.isNotEmpty(list)) {
                saveCardHis(card.getCardNo(), card.getPersonType(), list.get(0), CardOperationTypeEnum.LOSE_CARD.code, list.get(1), true, rightDeviceList.size(),card.getPersonId());
            }

        }
        if (bool) {
            return R.ok(bool, "挂失成功");
        } else {
            return R.failed(bool, "挂失失败");
        }
    }

    /**
     * 解挂卡
     *
     * @param cardNo
     * @param readCardNo
     * @param phone
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> obtainCard(String cardNo, String readCardNo, String phone) {
        if (!cardNo.equals(readCardNo)) {
            return R.failed(false, "该卡不是本挂失门禁卡");
        }
        ProjectCard projectCard = getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, cardNo).isNotNull(ProjectCard::getPersonId));


        ProjectProprietorDeviceVo proprietorDeviceVo = projectProprietorDeviceProxyService.getVo(projectCard.getPersonId());
        List<ProjectPassDeviceVo> deviceList = proprietorDeviceVo.getDeviceList();

        //修改卡状态为正常
        boolean bool = update(Wrappers.lambdaUpdate(ProjectCard.class)
                .set(ProjectCard::getCardStatus, "1")
                .eq(ProjectCard::getCardNo, cardNo));
        List<String> phoneAndPersonName = getPhoneAndPersonName(projectCard);
        String personName = phoneAndPersonName.get(1);

        if (CollUtil.isNotEmpty(deviceList)) {
            boolean flag = false;
            List<ProjectRightDevice> rightDeviceList = new ArrayList<>();
            //访客
            if (projectCard.getPersonType().equals(PersonTypeEnum.VISITOR.code)) {
                Integer visitorStatus = baseMapper.getVisitorStatus(projectCard.getPersonId());
                //判断 已过期
                if (visitorStatus > 0) {
                    flag = false;
                    //下发
                    deviceList.forEach(e -> {
                        ProjectRightDevice projectRightDevice = new ProjectRightDevice();
                        projectRightDevice.setDeviceId(e.getDeviceId());
                        projectRightDevice.setCertMedia(CertmediaTypeEnum.Card.code);
                        projectRightDevice.setCertMediaId(projectCard.getCardId());
                        projectRightDevice.setCertMediaInfo(projectCard.getCardNo());
                        projectRightDevice.setPersonType(projectCard.getPersonType());
                        projectRightDevice.setPersonId(projectCard.getPersonId());
                        projectRightDevice.setPersonName(personName);
                        projectRightDevice.setMobileNo(phone);
                        rightDeviceList.add(projectRightDevice);
                    });
                }else{
                    flag = true;
                }
                // 住户/员工
            } else {
                //判断 已过期/已禁用
                Integer count = baseMapper.getPersonStatus(projectCard.getPersonId());
                if (count > 0) {
                    flag = false;
                    //下发
                    deviceList.forEach(e -> {
                        ProjectRightDevice projectRightDevice = new ProjectRightDevice();
                        projectRightDevice.setDeviceId(e.getDeviceId());
                        projectRightDevice.setCertMedia(CertmediaTypeEnum.Card.code);
                        projectRightDevice.setCertMediaId(projectCard.getCardId());
                        projectRightDevice.setCertMediaInfo(projectCard.getCardNo());
                        projectRightDevice.setPersonType(projectCard.getPersonType());
                        projectRightDevice.setPersonId(projectCard.getPersonId());
                        projectRightDevice.setPersonName(personName);
                        projectRightDevice.setMobileNo(phone);
                        rightDeviceList.add(projectRightDevice);
                    });
                } else {
                    flag = true;
                }
            }
            //添加操作记录
            saveCardHis(cardNo, projectCard.getPersonType(), phone, CardOperationTypeEnum.OBTAIN_CARD.code, personName, flag, deviceList.size(),projectCard.getPersonId());
            projectRightDeviceService.addCertsAndCardHis(rightDeviceList, false);
        } else {
            saveCardHis(cardNo, projectCard.getPersonType(), phone, CardOperationTypeEnum.OBTAIN_CARD.code, personName, true, deviceList.size(),projectCard.getPersonId());
        }
        if (bool) {
            return R.ok(bool, "解挂成功");
        } else {
            return R.failed(bool, "解挂失败");
        }

    }

    /**
     * 下发
     * @param phone 手机号
     * @param projectCard 卡片信息
     * @param deviceList 设备列表
     * @param personName 人名
     * @param rightDeviceList 权限设备关系表
     */
    private void deliver(String phone, ProjectCard projectCard, List<ProjectPassDeviceVo> deviceList, String personName, List<ProjectRightDevice> rightDeviceList) {
        deviceList.forEach(e -> {
            ProjectRightDevice projectRightDevice = new ProjectRightDevice();
            projectRightDevice.setDeviceId(e.getDeviceId());
            projectRightDevice.setCertMedia(CertmediaTypeEnum.Card.code);
            projectRightDevice.setCertMediaId(projectCard.getCardId());
            projectRightDevice.setCertMediaInfo(projectCard.getCardNo());
            projectRightDevice.setPersonType(projectCard.getPersonType());
            projectRightDevice.setPersonId(projectCard.getPersonId());
            projectRightDevice.setPersonName(personName);
            projectRightDevice.setMobileNo(phone);
            rightDeviceList.add(projectRightDevice);
        });
    }

    /**
     * 注销卡
     *
     * @param cardNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> removeCard(String cardNo) {
        //删除卡
        this.remove(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, cardNo));
        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class)
                .eq(ProjectRightDevice::getCertMediaInfo, cardNo));
        boolean bool = projectRightDeviceService.delCerts(rightDeviceList);

        ProjectRightDevice projectRightDevice;

        //添加操作记录
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            projectRightDevice = rightDeviceList.get(0);
            saveCardHis(cardNo, projectRightDevice.getPersonType(), projectRightDevice.getMobileNo(), CardOperationTypeEnum.REMOVE_CARD.code, projectRightDevice.getPersonName(), false, rightDeviceList.size(),projectRightDevice.getPersonId());
        } else {
            ProjectCardHis cardHis = projectCardHisService.list(Wrappers.lambdaQuery(ProjectCardHis.class).likeRight(ProjectCardHis::getCardNo, cardNo).orderByDesc(ProjectCardHis::getCreateTime)).get(0);
            saveCardHis(cardHis.getCardNo(), cardHis.getPersonType(), cardHis.getPhone(), CardOperationTypeEnum.REMOVE_CARD.code, cardHis.getPersonName(), true, rightDeviceList.size(),cardHis.getPersonId());
        }
        if (bool) {
            return R.ok(bool, "注销成功");
        } else {
            return R.failed(bool, "注销失败");
        }
    }

    /**
     * 添加卡操作记录
     *
     * @param cardNo 卡号
     * @param personType 人员类型
     * @param phone 手机号
     * @param operationType 操作类型
     * @param personName 人员名称
     * @param isEmpty 状态判断
     * @param deviceCount 关联设备数量
     * @param personId 人员id
     */
    private void saveCardHis(String cardNo, String personType, String phone, String operationType, String personName, boolean isEmpty, int deviceCount,String personId) {
        try {
            ProjectCardHis projectCardHis = new ProjectCardHis();
            projectCardHis.setCardNo(cardNo);
            projectCardHis.setPersonId(personId);
            projectCardHis.setPersonType(personType);
            projectCardHis.setPersonName(personName);
            projectCardHis.setDeviceCount(String.valueOf(deviceCount));
            projectCardHis.setState(isEmpty ? CardStateEnum.SUCCESS.code : CardStateEnum.ONGOING.code);
            projectCardHis.setOperationType(operationType);
            projectCardHis.setPhone(phone);
            projectCardHis.setCreateTime(LocalDateTime.now());
            projectCardHis.setUpdateTime(LocalDateTime.now());
            projectCardHisService.save(projectCardHis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 换卡
     *
     * @param projectCardVo
     * @param oldCardNo
     * @param newCardNo
     */
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> changeCard(ProjectCardVo projectCardVo, String oldCardNo, String newCardNo) {
        if(oldCardNo.equals(newCardNo)){
            return R.failed(Boolean.FALSE,"输入的卡号于旧卡号一致，请重新输入");

        }
        ProjectCard newCardInfo = getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, newCardNo).eq(ProjectCard::getStatus, "1"));
        //卡已存在, 校验
        if (newCardInfo != null && newCardInfo.getStatus().equals(PassRightTokenStateEnum.USED.code)) {
            return R.failed(Boolean.FALSE,"当前卡号已被其他用户使用");
        } else if (newCardInfo != null && newCardInfo.getStatus().equals(PassRightTokenStateEnum.FREEZE.code)) {
            return R.failed(Boolean.FALSE,"当前卡已被冻结");
        }
        String phone = projectCardVo.getPhone();
        String personName = projectCardVo.getName();

        ProjectCard projectCard = getOne(Wrappers.lambdaQuery(ProjectCard.class).eq(ProjectCard::getCardNo, oldCardNo).eq(ProjectCard::getStatus, "1"));

        //删除之前的卡
        List<ProjectRightDevice> oldRightDeviceList = projectRightDeviceService.list(Wrappers.lambdaQuery(ProjectRightDevice.class)
                .eq(ProjectRightDevice::getCertMediaInfo, oldCardNo));

        if (!projectCard.getPersonType().equals(PersonTypeEnum.VISITOR.code)) {
            ProjectPersonPlanRel projectPersonPlanRel = projectPersonPlanRelService.getOne(Wrappers.lambdaQuery(ProjectPersonPlanRel.class).eq(ProjectPersonPlanRel::getPersonId, projectCard.getPersonId()));
            //禁用
            if (projectPersonPlanRel.getIsActive().equals("0")) {
                if(!projectCard.getCardStatus().equals("2")){
                    oldRightDeviceList.forEach(e -> {
                        e.setCertMediaInfo(newCardNo);
                    });
                    projectRightDeviceService.updateBatchById(oldRightDeviceList);
                }
                projectCard.setCardNo(newCardNo);
                projectCard.setCardStatus("1");
                updateById(projectCard);

                String cardNo = oldCardNo + "变更为" + newCardNo;
                //添加卡操作记录
                saveCardHis(cardNo, projectCard.getPersonType(), phone, CardOperationTypeEnum.CHANGE_CARD.code, personName, true, oldRightDeviceList.size(),projectCard.getPersonId());
                return R.ok(Boolean.TRUE);
            }
        }
        List<ProjectRightDevice> newRightDeviceList = new ArrayList<>(oldRightDeviceList);

        String cardUid = UUID.randomUUID().toString().replaceAll("-", "");
        update(Wrappers.lambdaUpdate(ProjectCard.class)
                .set(ProjectCard::getCardNo,newCardNo)
                .set(ProjectCard::getCardStatus,"1")
                .set(ProjectCard::getCardId,cardUid)
                .eq(ProjectCard::getCardNo,oldCardNo));
        projectRightDeviceService.delCerts(oldRightDeviceList, true, false);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                if(projectCard.getCardStatus().equals("2")){
                    ProjectProprietorDeviceVo proprietorDeviceVo = projectProprietorDeviceProxyService.getVo(projectCard.getPersonId());
                    List<ProjectPassDeviceVo> deviceList = proprietorDeviceVo.getDeviceList();
                    //下发
                    deviceList.forEach(e -> {
                        ProjectRightDevice projectRightDevice = new ProjectRightDevice();
                        projectRightDevice.setDeviceId(e.getDeviceId());
                        projectRightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FREEZE.code);
                        projectRightDevice.setCertMedia(CertmediaTypeEnum.Card.code);
                        projectRightDevice.setCertMediaId(projectCard.getCardId());
                        projectRightDevice.setCertMediaInfo(newCardNo);
                        projectRightDevice.setPersonType(projectCard.getPersonType());
                        projectRightDevice.setPersonId(projectCard.getPersonId());
                        projectRightDevice.setPersonName(personName);
                        projectRightDevice.setMobileNo(phone);
                        newRightDeviceList.add(projectRightDevice);
                    });
                }else{
                    //下发更换后的卡
                    newRightDeviceList.forEach(e -> {
                        e.setCertMediaInfo(newCardNo);
                        e.setSeq(null);
                        e.setCertMediaId(cardUid);
                        e.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
                    });
                }
                projectRightDeviceService.addCerts(newRightDeviceList, true, false, false);
            }
        });
        String cardNo = oldCardNo + "变更为" + newCardNo;
        //添加卡操作记录
        ProjectCardHis projectCardHis = new ProjectCardHis();
        projectCardHis.setOldCardNo(oldCardNo);
        projectCardHis.setCardNo(newCardNo);
        projectCardHis.setPersonId(projectCard.getPersonId());
        projectCardHis.setPersonType(projectCard.getPersonType());
        projectCardHis.setPersonName(personName);
        projectCardHis.setDeviceCount(String.valueOf(newRightDeviceList.size()));
        projectCardHis.setState(CollUtil.isEmpty(newRightDeviceList) ? CardStateEnum.SUCCESS.code : CardStateEnum.ONGOING.code);
        projectCardHis.setOperationType(CardOperationTypeEnum.CHANGE_CARD.code);
        projectCardHis.setPhone(phone);
        projectCardHis.setCreateTime(LocalDateTime.now());
        projectCardHis.setUpdateTime(LocalDateTime.now());
        projectCardHisService.save(projectCardHis);


        return R.ok(Boolean.TRUE);
    }
}
