package com.whut.onlinejudge.common.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.whut.onlinejudge.common.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Data
public class UserVo implements Serializable {

    /**
     * 题目 ID
     */
    private Long id;

    /**
     * 头像网络地址
     */
    private String userAvatar;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 用户登录 Token
     */
    private String token;


    private final static long serialVersionUID = -1;

    public static UserVo getUserVo(User user) {
        return BeanUtil.copyProperties(user, UserVo.class);
    }
}
