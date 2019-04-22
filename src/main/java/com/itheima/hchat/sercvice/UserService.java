package com.itheima.hchat.sercvice;

import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.User;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService {
    List<TbUser> findAll();

    /**
     * 校验登陆信息，如果正确返回用户信息，如果不正确返回null
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password) throws InvocationTargetException, IllegalAccessException;

    /**
     * 用户注册，如果不返回注册成功，抛出异常注册失败返回信息
     * @param tbUser
     */
    void register(TbUser tbUser);

    /**
     * 用户上传头像
     * @param file
     * @param userid
     * @return
     */
    User upload(MultipartFile file, String userid);

    /**
     * 用户修改昵称
     * @param id
     * @param nickname
     */
    void updateNickname(String id, String nickname);

    /**
     * 刷新用户信息
     * @param userid
     * @return
     */
    User findById(String userid) throws InvocationTargetException, IllegalAccessException;
}
