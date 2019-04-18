package com.itheima.hchat.sercvice.impl;

import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.TbUserExample;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.sercvice.UserService;
import com.itheima.hchat.util.IdWorker;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Autowired
    private IdWorker idWorker;


    @Override
    public List<TbUser> findAll() {
        return tbUserMapper.selectByExample(null);
    }

    @Override
    public User login(String username, String password) throws InvocationTargetException, IllegalAccessException {
        TbUserExample example= new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers !=  null && tbUsers.size() == 1){
            String digestPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            for (TbUser tbUser : tbUsers) {
                if (StringUtils.equals(tbUser.getPassword(), digestPassword)) {
                    User user = new User();
                    BeanUtils.copyProperties(tbUsers.get(0), user);
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * 注册
     * @param tbUser
     */
    @Override
    public void register(TbUser tbUser) {

        // 查看用户名是否被占用
        TbUserExample example= new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(tbUser.getUsername());
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers != null && tbUsers.size() > 0){
            // 占用抛出异常
            throw  new RuntimeException("用户名已被占用");
        }
        // 没占用返回空，入库. 对密码进行加密
        tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        tbUser.setId(idWorker.nextId());
        tbUser.setPicSmall("");
        tbUser.setPicNormal("");
        tbUser.setNickname(tbUser.getUsername());
        tbUser.setQrcode("");
        tbUser.setCreatetime(new Date());
        tbUserMapper.insert(tbUser);
    }
}
