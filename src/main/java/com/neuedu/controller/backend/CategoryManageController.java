package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.service.IcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@RequestMapping(value = "/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IcategoryService categoryService;

    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(@RequestParam(required = false,defaultValue = "0") int parentId,
                                       String categoryName, HttpSession session){

        //用户是否登录
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){ //需要登录
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        //是否有管理员权限
        ServerResponse serverResponse = userService.checkUserAdmin(userInfo);
        if(serverResponse.isSuccess()){ //有管理员权限
            categoryService.add_category(parentId,categoryName);

        }else{
            return ServerResponse.createByError("用户无权限操作");
        }
        return null;

    }

}
