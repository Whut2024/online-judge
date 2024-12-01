package com.whut.onlinejudge.backgrounddoor.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.exception.BusinessException;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.backgrounddoor.lock.DistributedLockSupport;
import com.whut.onlinejudge.backgrounddoor.mapper.UserMapper;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.model.dto.user.UserAddRequest;
import com.whut.onlinejudge.common.model.dto.user.UserLoginRequest;
import com.whut.onlinejudge.common.model.entity.User;
import com.whut.onlinejudge.common.model.vo.UserVo;
import com.whut.onlinejudge.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author laowang
 * @description 针对表【t_user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-29 20:00:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private final DistributedLockSupport lockSupport;

    private final StringRedisTemplate redisTemplate;

    public UserServiceImpl(DistributedLockSupport lockSupport, StringRedisTemplate redisTemplate) {
        this.lockSupport = lockSupport;
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    @Lazy
    private UserServiceImpl proxy;

    @Override
    public Long register(UserAddRequest userAddRequest) {
        final String userAccount = userAddRequest.getUserAccount();
        final String userAvatar = userAddRequest.getUserAvatar();
        final String userName = userAddRequest.getUserName();
        final String userPassword = userAddRequest.getUserPassword();
        final String userRole = userAddRequest.getUserRole();

        final LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount);
        ThrowUtils.throwIf(this.baseMapper.exists(wrapper), ErrorCode.OPERATION_ERROR, "用户已经存在");

        final User user = new User();
        lockSupport.lock(RedisConstant.USER_REGISTER_LOCK + userAccount,
                () -> {
                    user.setUserAccount(userAccount);
                    user.setUserPassword(SecureUtil.md5(userPassword));
                    user.setUserName(userName);
                    user.setUserAvatar(userAvatar);
                    user.setUserRole(userRole);
                    proxy.save(user);
                }, () -> {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能同时注册相同用户");
                });

        return user.getId();
    }

    @Override
    public UserVo getLoginUser() {
        final User user = UserHolder.get();
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        return UserVo.getUserVo(user);
    }

    @Override
    public UserVo login(UserLoginRequest userLoginRequest) {
        final String userAccount = userLoginRequest.getUserAccount();
        final String userPassword = userLoginRequest.getUserPassword();

        final LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount).eq(User::getUserPassword, SecureUtil.md5(userPassword));
        // wrapper.select();

        final User user = this.getOne(wrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "账号或者密码错误");

        // 缓存版本更新
        final String versionKey = RedisConstant.USER_LOGIN_VERSION_KEY + user.getId();
        final Long version = redisTemplate.opsForValue().increment(versionKey);
        redisTemplate.expire(versionKey, RedisConstant.USER_LOGIN_VERSION_TIME, TimeUnit.MILLISECONDS);
        user.setVersion(version);

        // 缓存 token
        final String userKey = RedisConstant.USER_CACHE + SecureUtil.md5(System.currentTimeMillis() + RedisConstant.SALT);
        redisTemplate.opsForValue().set(userKey, JSONUtil.toJsonStr(user), RedisConstant.USER_CACHE_TIME, TimeUnit.MILLISECONDS);

        final UserVo userVo = UserVo.getUserVo(user);
        userVo.setToken(userKey);

        return userVo;
    }


}




