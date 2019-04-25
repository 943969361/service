package com.itheima.hchat.Realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.*;

public class CuseomerRealm  extends AuthorizingRealm {

    Map<String,String> map = new HashMap<>(16);
    {
        map.put("jack","f51703256a38e6bab3d9410a070c32ea");
        super.setName("realName");
    }
    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();
        // 获取权限
        Set<String> limits = getLimits(username);
        // 获取角色
        Set<String> roles = getRolesByUsername(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(limits);
        return simpleAuthorizationInfo;

    }

    private Set<String> getRolesByUsername(String username) {

        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        return  roles ;
    }

    private Set<String> getLimits(String username) {

        Set<String> limits = new HashSet<>();
        limits.add("user:delete");
        limits.add("user.select");
        return limits;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username  = (String) token.getPrincipal();
        String password = getUsernameByPassword(username);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("jack",password ,"realName");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("salt"));
        return authenticationInfo;
    }

    /**
     * 模拟数据库
     * @param username
     * @return
     */
    private String getUsernameByPassword(String username) {
        return map.get(username);
    }

    /**
     * dm5加密算法
     */
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456","salt");
        System.out.println(md5Hash);

    }
}
