package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @RestController 是让这个类下的返回值是json格式
 * */

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    IUserService userService;

    @RequestMapping(value = "/login.do")

    public ServerResponse  login(String username, String password, HttpSession session){
        ServerResponse serverResponse = userService.login(username,password);
        if(serverResponse.isSuccess()){//登陆成功
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());

        }
    return serverResponse;


    }




}
