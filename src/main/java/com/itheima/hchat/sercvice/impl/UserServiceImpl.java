package com.itheima.hchat.sercvice.impl;

import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.sercvice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public List<TbUser> findAll() {
        return tbUserMapper.selectByExample(null);
    }
}
