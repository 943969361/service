package com.itheima.hchat.sercvice;

import com.itheima.hchat.pojo.TbUser;

import java.util.List;

public interface UserService {
    List<TbUser> findAll();
}
