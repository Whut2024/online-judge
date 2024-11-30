package com.whut.onlinejudge.common.service;

import com.whut.onlinejudge.common.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.onlinejudge.common.model.dto.user.UserAddRequest;
import com.whut.onlinejudge.common.model.dto.user.UserLoginRequest;
import com.whut.onlinejudge.common.model.vo.UserVo;

/**
* @author laowang
* @description 针对表【t_user(用户表)】的数据库操作Service
* @createDate 2024-11-29 20:00:41
*/
public interface UserService extends IService<User> {


    /**
     * 用户注册服务
     * 该方法接收一个UserAddRequest对象，其中包含了用户注册所需的信息
     * 它负责处理用户添加到系统中的逻辑，并返回一个Long类型的值，通常代表新用户的ID
     *
     * @param userAddRequest 包含用户注册信息的请求对象
     * @return 通常返回新注册用户的ID如果注册失败，可能返回null或特定的错误代码
     */
    Long register(UserAddRequest userAddRequest);

    /**
     * 获取当前登录的用户信息
     * 此方法用于获取当前系统中登录的用户信息，以UserVo对象的形式返回
     * 主要用于需要获取当前用户信息的场景，以便进行权限验证、个性化展示等操作
     *
     * @return UserVo 当前登录用户的详细信息，如果无用户登录，则返回null
     */
    UserVo getLoginUser();

    /**
     * 用户登录方法
     * 该方法接收一个用户登录请求对象，其中包含了用户登录所需的信息，如用户名和密码
     * 验证用户凭据并返回一个用户信息对象（UserVo），如果登录成功
     * 如果登录失败，该方法可能抛出异常或返回null，具体行为应根据应用需求定义
     *
     * @param userLoginRequest 用户登录请求对象，包含用户登录所需的信息
     * @return 登录成功时返回一个包含用户信息的UserVo对象，否则可能返回null或抛出异常
     */
    UserVo login(UserLoginRequest userLoginRequest);
}

