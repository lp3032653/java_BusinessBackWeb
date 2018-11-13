package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.GuavaCacheUtil;
import com.neuedu.utils.MD5Utils;
import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.neuedu.common.Const.USERNAME;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    UserInfoMapper userInfoMapper;

    /**
     * 用户登录
     * */
    @Override
    public ServerResponse login(String username, String password) {
        //step1:校验用户名
        int result = userInfoMapper.checkUsername(username);
        //step2:校验用户名
        if(result>0){//存在用户
            //todo MD5 password
            password = MD5Utils.GetMD5Code(password);
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
    /**
     * 用户注册
     * */
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //校验用户名
    /*  int result=  userInfoMapper.checkUsername(userInfo.getUsername());
      if(result>0){//用户名存在
          return ServerResponse.createByError("用户名已存在");
      }*/
        ServerResponse serverResponse=checkValid(userInfo.getUsername(),Const.USERNAME);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }
        //校验邮箱
     /* int eamil_result=userInfoMapper.checkEmail(userInfo.getEmail());
       if(eamil_result>0){//邮箱存在
           return ServerResponse.createByError("邮箱存在");
       }*/
        ServerResponse serverResponse1=checkValid(userInfo.getEmail(),Const.EMAIL);
        if(!serverResponse1.isSuccess()){
            return serverResponse1;
        }
        //加密算法: hash算法(不对称加密)--》md5、RSA
        //MD5加密
        userInfo.setPassword(MD5Utils.GetMD5Code(userInfo.getPassword()));
        userInfo.setRole(Const.USERROLE.CUSTOMERUSER);//0:管理员 1;普通用户
        //将用户插入到数据库
        int  insert_result=  userInfoMapper.insert(userInfo);
        if(insert_result>0){
            //注册成功
            return ServerResponse.createBySuccess("注册成功");
        }

        return ServerResponse.createByError("注册失败");
    }
    /**
     * 校验用户名和邮箱
     * */
    @Override
    public ServerResponse checkValid(String str, String type) {
        if(type==null||type.equals("")){
            return  ServerResponse.createByError("参数必须传递");
        }
        if(str==null||str.equals("")){
            return  ServerResponse.createByError("参数必须传递");
        }
        if(type.equals(USERNAME)){
            //校验用户名
            int username_result= userInfoMapper.checkUsername(str);
            if(username_result>0){
                return ServerResponse.createByError("用户名已经存在");
            }
        }
        if(type.equals(Const.EMAIL)){
            //校验邮箱
            int email_result=userInfoMapper.checkEmail(str);
            if(email_result>0){
                return ServerResponse.createByError("邮箱已经存在");
            }
        }
        return ServerResponse.createBySuccess("校验成功");
    }
    /**
     * 根据用户名获取密保问题
     * */
    @Override
    public ServerResponse forget_get_question(String username) {
        //校验username
        if(username==null||username.equals("")){
            return ServerResponse.createByError("用户名不能为空");
        }
        //校验用户名是否存在
        int username_result=userInfoMapper.checkUsername(username);
        if(username_result>0){
            //用户名存在
            //根据用户名查询密码问题
            String question=userInfoMapper.selectQuestionByUsername(username);
            if(question==null||question.equals("")){
                return ServerResponse.createByError("未找到密保问题");
            }
            return ServerResponse.createBySuccess(question,"成功");
        }
        return ServerResponse.createByError("不存在此用户");
    }
    /**
     * 获取密保答案
     * */
    @Override
    public ServerResponse forget_answer(String username, String question, String answer) {

        if (username == null || username.equals("")) {
            return ServerResponse.createByError("用户名必须传");
        }
        if (question == null || question.equals("")) {
            return ServerResponse.createByError("密保问题必须传");
        }
        if (answer == null || answer.equals("")) {
            return ServerResponse.createByError("密保答案必须传");
        }
        int restult=userInfoMapper.check_forget_answer(username,question,answer);
        if(restult>0){
            //答案正确
            String token= UUID.randomUUID().toString();
            //保存在服务端一份-->guava cache
            GuavaCacheUtil.put(username,token);
            String value= GuavaCacheUtil.get(username);
            return  ServerResponse.createBySuccess(token,"成功");
        }
        return null;
    }
    /**
     * 忘记密码--重置密码
     * */
    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")) {
            return ServerResponse.createByError("用户名必须传");
        }
        if (forgetToken == null || forgetToken.equals("")) {
            return ServerResponse.createByError("token必须传");
        }    if (passwordNew == null || passwordNew.equals("")) {
            return ServerResponse.createByError("新密码必须传");
        }
        //校验用户名是否存在
        ServerResponse serverResponse=  checkValid(username, USERNAME);
        if(serverResponse.isSuccess()){//不存在
            return  ServerResponse.createByError("用户名不存在");
        }
        //获取用户存在服务器的token
        String token=GuavaCacheUtil.get(username);
        if(token==null){
            return ServerResponse.createByError("token不存在或者已经过期");
        }
        //判断用户传递的token与服务端保存的用户的token是否一致
        if(forgetToken.equals(token)){
            //修改密码
            passwordNew=MD5Utils.GetMD5Code(passwordNew);
            int result= userInfoMapper.updatePasswordByUsername(username,passwordNew);
            if(result>0){
                return  ServerResponse.createBySuccess("修改成功");
            }else{
                return ServerResponse.createByError("修改失败");
            }
        }else{
            return ServerResponse.createByError("token错误，请重新获取");
        }

    }
    /**
     * 登录状态--重置密码
     * */
    @Override
    public ServerResponse reset_password(String passwordOld, String passwordNew, UserInfo userInfo) {
        //非空校验
        if (passwordOld == null || passwordOld.equals("")) {
            return ServerResponse.createByError("旧密码不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")) {
            return ServerResponse.createByError("新密码不能为空");
        }
        //根据用户id和passwordOld进行校验，防止横向越权
        int result = userInfoMapper.selectCountByUserIdAndPassowrd(userInfo.getId(),MD5Utils.GetMD5Code(passwordOld));
        if(result>0){  //校验通过(旧密码正确)
            userInfo.setPassword(MD5Utils.GetMD5Code(passwordNew));
            //修改密码
            int update_result = userInfoMapper.updateBySelectedActive(userInfo);
            if(update_result>0){
                return ServerResponse.createBySuccess("密码更新成功");
            }else{
                return ServerResponse.createByError("密码更新失败");
            }
        }else{
            return ServerResponse.createByError("旧密码错误");
        }
    }

    @Override
    public ServerResponse updateUserInfo(UserInfo user) {
        //校验邮箱是否存在 userid 和 email
        int result = userInfoMapper.checkEmailByUseridAndEamil(user.getId(),user.getEmail());
        if(result>0){   //邮箱已存在
            return ServerResponse.createByError("邮箱存在");
        }
       int update_result =  userInfoMapper.updateBySelectedActive(user);
        if(update_result>0){
            return ServerResponse.createBySuccess("更新成功");
        }else{
            return ServerResponse.createByError("更新失败");
        }
    }

    @Override
    public ServerResponse selectUserByPageNo(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);  //pagehelper 一定要放在获取数据之前
        List <UserInfo> userInfoList = userInfoMapper.selectAll();

        //分页模型
        PageInfo pageInfo = new PageInfo(userInfoList);
        return ServerResponse.createBySuccess(pageInfo,"success");
    }

    @Override
    public ServerResponse checkUserAdmin(UserInfo userInfo) {
       if(userInfo.getRole().intValue()==0){
           return ServerResponse.createBySuccess("success");
       }
        return ServerResponse.createByError("fail");
    }

}
