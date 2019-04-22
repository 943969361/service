package com.itheima.hchat.sercvice.impl;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ImageUtil;
import com.alibaba.fastjson.JSON;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.TbUserExample;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.sercvice.UserService;
import com.itheima.hchat.util.FastDFSClient;
import com.itheima.hchat.util.IdWorker;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Resource
    private Environment environment;

    @Autowired
    private HttpServletRequest request;


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
                    //noinspection AlibabaAvoidApacheBeanUtilsCopy
                    BeanUtils.copyProperties(user,tbUser);
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

    /**
     * 上传头像
     * @param file
     * @param userid
     * @return
     */
    @Override
    public User upload(MultipartFile file, String userid) {

        try {
            // url 不带http
            String url = fastDFSClient.uploadFile(file);
            String property = environment.getProperty("fdfs.httpurl");
            String[] fileNameList = url.split("\\.");
            String fileName = fileNameList[0];
            String ext = fileNameList[1];
            String picSmallUrl = fileName + "_150x150." + ext;
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
            tbUser.setPicNormal(property + url);
            tbUser.setPicSmall(property + picSmallUrl);
            //保存本地修改小图失败
            // 保存 G:\Photo\big
//            approvalFile(file);
//            // 截取filename最后/文件名
//            String newFileName =  fileName.substring(fileName.lastIndexOf("/")+1);
//            ImgUtil.scale(
//                    FileUtil.file("F:\\netty_chat\\service\\src\\main\\webapp\\img\\big\\" + newFileName +"."+ ext),
//                    FileUtil.file("F:\\netty_chat\\service\\src\\main\\webapp\\img\\small\\" + newFileName+"."+ ext),
//                    0.1f
//                    //缩放比例
//            );
            tbUserMapper.updateByPrimaryKey(tbUser);
            // 用户返回User
            User user = new User();
            BeanUtils.copyProperties(user,tbUser);
            return  user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void approvalFile( MultipartFile filecontent){
        OutputStream os = null;
        InputStream inputStream = null;
        String fileName = null;
        try {
            inputStream = filecontent.getInputStream();
            fileName = filecontent.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String path = "F:\\netty_chat\\service\\src\\main\\webapp\\img\\big\\";
            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改昵称
     * @param id
     * @param nickname
     */
    @Override
    public void updateNickname(String id, String nickname) {

        if(StringUtils.isEmpty(id) || StringUtils.isEmpty(nickname)){
            throw new RuntimeException("昵称或id为空");
        }
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(id);
        tbUser.setNickname(nickname);
        tbUserMapper.updateByPrimaryKey(tbUser);
    }

    @Override
    public User findById(String userid) throws InvocationTargetException, IllegalAccessException {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
        User user = new User();
        BeanUtils.copyProperties(user,tbUser);
        return user;
    }


}
