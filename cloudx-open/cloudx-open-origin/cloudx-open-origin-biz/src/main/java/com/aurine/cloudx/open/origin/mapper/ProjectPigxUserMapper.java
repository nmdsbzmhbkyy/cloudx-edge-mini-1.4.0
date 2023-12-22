package com.aurine.cloudx.open.origin.mapper;


import com.aurine.cloudx.open.origin.entity.PigxxUserInfo;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * pigx管理员用户查询mapper
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/28 9:39
 */
@Mapper
public interface ProjectPigxUserMapper {

    /**
     * 查询pigx用户信息
     *
     * @param userId 系统用户ID
     * @return 系统用户信息
     */
    PigxxUserInfo getUserInfo(Integer userId);
}
