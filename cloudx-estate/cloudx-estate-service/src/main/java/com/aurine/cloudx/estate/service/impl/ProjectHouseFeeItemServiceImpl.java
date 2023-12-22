package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.constant.FeeConstant;
import com.aurine.cloudx.estate.entity.ProjectHouseFeeItem;
import com.aurine.cloudx.estate.mapper.ProjectHouseFeeItemMapper;
import com.aurine.cloudx.estate.service.ProjectBillingInfoService;
import com.aurine.cloudx.estate.service.ProjectHouseFeeItemService;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemConfVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemUpdateBatchVo;
import com.aurine.cloudx.estate.vo.ProjectHouseFeeItemUpdateVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 房屋费用设置(ProjectHouseFeeItem)表服务实现类
 *
 * @author makejava
 * @since 2020-07-20 16:43:48
 */
@Service
public class ProjectHouseFeeItemServiceImpl extends ServiceImpl<ProjectHouseFeeItemMapper, ProjectHouseFeeItem> implements ProjectHouseFeeItemService {
    @Resource
    ProjectBillingInfoService projectBillingInfoService;

    @Override
    public Page<ProjectHouseFeeItemConfVo> selectHouseFeeItemConf(Page<ProjectHouseFeeItemConfVo> page) {
        return baseMapper.selectHouseFeeItemConf(page, null, null);
    }

    @Override
    public List<ProjectHouseFeeItemConfVo> listHouseFeeItemConf(String houseId, List<String> ids, List<String> feeCycleTypes, List<String> feeTypes) {
        return baseMapper.listHouseFeeItemConf(houseId, ids, feeCycleTypes, feeTypes);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R updateVo(ProjectHouseFeeItemUpdateVo projectHouseFeeItem) {
        List<String> deleteIds = projectHouseFeeItem.getDeleteIds();
        if (deleteIds != null && deleteIds.size() > 0) {
            baseMapper.deleteBatchIds(deleteIds);
        }
        List<ProjectHouseFeeItemConfVo> houseFee = listHouseFeeItemConf(projectHouseFeeItem.getHouseId(), null, null, null);
        List<String> houseFeeIdList = new ArrayList<>();
        List<String> houseFeeIdIncidentalList = new ArrayList<>();
        if (houseFee != null && houseFee.size() > 0) {
            for (ProjectHouseFeeItemConfVo e : houseFee) {
                if (FeeConstant.FIXED_CHARGE.equals(e.getFeeCycleType())) {
                    //获取固定费用列表
                    houseFeeIdList.add(e.getFeeId());
                }
                if (FeeConstant.INCIDENTAL_EXPENSES.equals(e.getFeeCycleType())) {
                    //获取临时费用列表
                    houseFeeIdIncidentalList.add(e.getFeeId());
                }
            }
        }
        List<ProjectHouseFeeItem> projectHouseFeeItems = projectHouseFeeItem.getProjectHouseFeeItems();
        List<ProjectHouseFeeItem> next = new ArrayList<>();
        if (projectHouseFeeItems != null && projectHouseFeeItems.size() > 0) {
            List<String> feeIds = new ArrayList<>();
            for (ProjectHouseFeeItem m : projectHouseFeeItems) {
                m.setRecordId(null);
                m.setHouseId(projectHouseFeeItem.getHouseId());

                if (m.getFeeId() != null && houseFeeIdList.contains(m.getFeeId())) {
                    continue;
                }
                if (m.getBillMonth() == null || "".equals(m.getBillMonth())) {
                    m.setBillMonth(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
                }
                //添加临时费用生成列表
                feeIds.add(m.getFeeId());
                //临时费用不加入房屋费用配置中
                if (m.getFeeId() != null && houseFeeIdIncidentalList.contains(m.getFeeId())) {
                    continue;
                }
                next.add(m);
            }
            // 新增临时费用账单
            if (feeIds.size() > 0) {
                projectBillingInfoService.saveBill(projectHouseFeeItem.getHouseId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")), feeIds);
            }

        }
        saveBatch(next);
        //重新生成固定账单
//        projectBillingInfoService.resentBillingInfo();
        return R.ok();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public R updateBatchVo(ProjectHouseFeeItemUpdateBatchVo projectHouseFeeItemUpdateBatchVo) {
        List<String> houseIds = projectHouseFeeItemUpdateBatchVo.getHouseIds();
        houseIds.forEach(e -> {
            ProjectHouseFeeItemUpdateVo projectHouseFeeItemUpdateVo = new ProjectHouseFeeItemUpdateVo();
            projectHouseFeeItemUpdateVo.setHouseId(e);
            projectHouseFeeItemUpdateVo.setProjectHouseFeeItems(projectHouseFeeItemUpdateBatchVo.getProjectHouseFeeItems());
            updateVo(projectHouseFeeItemUpdateVo);
        });
        return R.ok();
    }


}