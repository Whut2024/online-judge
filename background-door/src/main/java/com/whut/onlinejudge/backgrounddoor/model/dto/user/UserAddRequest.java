package com.whut.onlinejudge.backgrounddoor.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 头像网络地址
     */
    private String userAvatar;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户角色
     */
    private String userRole;

    private final static long serialVersionUID = -1L;
}
