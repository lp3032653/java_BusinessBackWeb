package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface IUserService {
    /**
     * 登录
     * */
    public ServerResponse login(String username,String password);

    /**
     * 注册
     * */
    public ServerResponse register(UserInfo userInfo);

    /**
     * 校验用户名和邮箱
     * */
    public ServerResponse checkValid(String str,String type);

    /**
     * 忘记密码获取密保问题
     * */
    public ServerResponse forget_get_question(String username);

    /**
     * 忘记密码--获取答案
     * */
    public ServerResponse forget_answer(String username,String question,String answer);

    /**
     * 忘记密码--重置密码
     * */
    public  ServerResponse forget_reset_password(String username,String passwordNew,String forgetToken);

    /**
     * 登录状态--重置密码
     * */
    public  ServerResponse reset_password(String username,String passwordNew,UserInfo userInfo);

    /**
     * 登录状态--修改个人信息
     * */
    public  ServerResponse updateUserInfo(UserInfo userInfo);

    /**
     * 分页查询
     * @Param       pageNo      页码 （第几页）
     * @Param       pageSize    查询数据量
     * */
    public  ServerResponse selectUserByPageNo(int pageNo, int pageSize);

    /**
     * 判断是否为管理员
     * */
    public ServerResponse checkUserAdmin(UserInfo userInfo);












}
