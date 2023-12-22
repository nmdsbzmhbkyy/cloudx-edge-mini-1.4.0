package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.dto.qrPasscode.QrPasscodePageDto;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProjectQrPasscodeRecordMapper extends BaseMapper<ProjectQrPasscodeRecord> {
    @Select("select passenger,phone,credentialNo,startTime,endTime,times,uniqueCode from project_qr_passcode_record")
    Page<QrPasscodePageDto> queryPage(@Param("page")Page<Object> page);
}
