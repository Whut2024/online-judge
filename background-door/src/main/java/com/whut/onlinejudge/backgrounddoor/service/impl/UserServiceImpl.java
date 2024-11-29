package com.whut.onlinejudge.backgrounddoor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.mapper.UserMapper;
import com.whut.onlinejudge.common.entity.User;
import com.whut.onlinejudge.common.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author laowang
* @description 针对表【t_user(用户表)】的数据库操作Service实现
* @createDate 2024-11-29 20:00:41
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




