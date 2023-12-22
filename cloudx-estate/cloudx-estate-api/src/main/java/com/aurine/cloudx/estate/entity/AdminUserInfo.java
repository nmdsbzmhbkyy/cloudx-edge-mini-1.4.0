package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>管理员信息对象</p>
 * @author : 王良俊
 * @date : 2021-12-21 09:35:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfo {

    /**
     * 用户ID
     */
    private Integer user_id;

    /**
     * 登录账号
     */
    private String userName;

    /**
     * 管理员姓名
     */
    private String true_name;

    /**
     * 管理员证件类型
     */
    private String credential_type;

    /**
     * 管理员证件号
     */
    private String credential_no;

    /**
     * 部门ID（项目管理员是项目ID）
     */
    private Integer dept_id;

    /**
     * 管理员联系电话
     */
    private String phone;

    /**
     * 管理员性别
     */
    private String sex;

    /**
     * 管理员头像
     */
    private String avatar;

    /**
     * 生效时间
     */
    private LocalDateTime eff_time;

    /**
     * 失效时间
     */
    private LocalDateTime exp_time;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目ID
     */
    private Integer projectId;


}
