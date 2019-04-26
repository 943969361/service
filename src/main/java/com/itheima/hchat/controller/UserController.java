package com.itheima.hchat.controller;

import com.itheima.hchat.filter.AspectDemo;
import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.sercvice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AspectDemo aspectDemo;

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

    /**
     * 文件上传
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile file ,String userid){

        try {
            User user = userService.upload(file,userid);
            if(user == null){
                return new Result(false,"上传失败");
            }else {
                System.out.println(user);
                return new Result(true,"上传成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"上传错误");
        }
    }

    @RequestMapping("/updateNickname")
    public Result updateNickname(@RequestBody User user){

        try {
            userService.updateNickname(user.getId(),user.getNickname());
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/findById")
    public User findById(String userid) throws InvocationTargetException, IllegalAccessException {
        return userService.findById(userid);
    }

    /**
     * 查找用户信息
     * @param userid
     * @param friendUsername
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/findUserById")
    public User findUserById(String userid,String friendUsername) throws InvocationTargetException, IllegalAccessException {
        return userService.findUserById(userid,friendUsername);
    }

    /**
     * 测试filter过滤器
     */
    @RequestMapping("/test")
    public void test(){
        aspectDemo.attack();
        System.out.println("执行filter的test方法");
    }

}
