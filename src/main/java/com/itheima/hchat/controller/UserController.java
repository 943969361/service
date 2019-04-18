package com.itheima.hchat.controller;

import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.sercvice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询所有用户
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbUser> findAll (){
        return  userService.findAll();
    }

    /**
     * 登陆判断登陆名和密码
     */
    @RequestMapping("/login")
    public Result login(@RequestBody TbUser tbUser){

        try {
            User _user = userService.login(tbUser.getUsername(),tbUser.getPassword());
            if(_user == null){
                return new Result(false,"登陆失败，请检查用户名或密码");
            }else {
                return new Result(true,"登陆成功",_user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"登陆错误");
        }
    }

    /**
     * 用注册
     */
    @RequestMapping("/register")
    public  Result register(@RequestBody TbUser tbUser){
        try {
            userService.register(tbUser);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        }
    }
}
