package com.whut.onlinejudge.backgrounddoor.controller;

import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.backgrounddoor.model.dto.user.UserAddRequest;
import com.whut.onlinejudge.backgrounddoor.model.dto.user.UserLoginRequest;
import com.whut.onlinejudge.backgrounddoor.model.vo.UserVo;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuqiao
 * @since 2024-11-29
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserAddRequest userAddRequest) {
        return ResultUtils.success(null);
    }


    @GetMapping("/get/login")
    public BaseResponse<UserVo> getLoginUser() {
        return ResultUtils.success(null);
    }

    @PostMapping("/login")
    public BaseResponse<UserVo> login(@RequestBody UserLoginRequest userLoginRequest) {
        return ResultUtils.success(null);
    }
}
