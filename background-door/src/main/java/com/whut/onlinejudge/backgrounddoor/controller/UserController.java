package com.whut.onlinejudge.backgrounddoor.controller;

import cn.hutool.core.util.StrUtil;
import com.whut.onlinejudge.backgrounddoor.annotation.TimeLimit;
import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.common.model.dto.user.UserAddRequest;
import com.whut.onlinejudge.common.model.dto.user.UserLoginRequest;
import com.whut.onlinejudge.common.model.enums.UserRoleEnum;
import com.whut.onlinejudge.common.model.vo.UserVo;
import com.whut.onlinejudge.common.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuqiao
 * @since 2024-11-29
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@TimeLimit(prefix = "global:")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final String userAccount = userAddRequest.getUserAccount();
        final String userAvatar = userAddRequest.getUserAvatar();
        final String userName = userAddRequest.getUserName();
        final String userPassword = userAddRequest.getUserPassword();
        final String userRole = userAddRequest.getUserRole();

        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(userAccount, userPassword, userRole),
                ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userAccount.length() > 16 ||
                        userPassword.length() > 32 ||
                        userRole.length() > 8 ||
                        (StrUtil.isNotBlank(userAvatar) && userAvatar.length() > 512) ||
                        (StrUtil.isNotBlank(userName) && userName.length() > 16),
                ErrorCode.PARAMS_ERROR, "参数长度过长");
        ThrowUtils.throwIf(!UserRoleEnum.exists(userRole), ErrorCode.PARAMS_ERROR, "角色错误");

        return ResultUtils.success(userService.register(userAddRequest));
    }


    @GetMapping("/get/login")
    public BaseResponse<UserVo> getLoginUser() {
        return ResultUtils.success(userService.getLoginUser());
    }

    @PostMapping("/login")
    public BaseResponse<UserVo> login(@RequestBody UserLoginRequest userLoginRequest) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final String userAccount = userLoginRequest.getUserAccount();
        final String userPassword = userLoginRequest.getUserPassword();

        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(userAccount, userPassword),
                ErrorCode.PARAMS_ERROR, "账号或者密码长度为0");
        ThrowUtils.throwIf(userAccount.length() > 16 || userPassword.length() > 32,
                ErrorCode.PARAMS_ERROR, "账号或者密码过长");

        return ResultUtils.success(userService.login(userLoginRequest));
    }
}
