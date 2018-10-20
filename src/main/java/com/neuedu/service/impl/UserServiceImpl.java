package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
    public ServerResponse login(String username, String password) {

        //step1:校验用户名
        int result = userInfoMapper.checkUsername(username);
        //step2:校验用户名
        if(result>0){//存在用户
            //todo MD5 password
            //查询用户信息
            UserInfo userInfo = userInfoMapper.selectLogin(username,password);
            if(userInfo==null){//输入密码错误，没查出信息
                return ServerResponse.createByError("密码错误");
            }else{
                userInfo.setPassword("");
                return ServerResponse.createBySuccess(userInfo,"登录成功");
            }
        }else{//用户名不存在
            return ServerResponse.createByError("用户名不存在");


        }
    }
}
