package com.whut.onlinejudge.common.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    private final static long serialVersionUID = -1L;
}
