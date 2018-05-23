package com.example.demo.Config;

import com.example.demo.entity.u_user;
import com.example.demo.service.Impl.u_userImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.*;

public class MyShiroRealm extends AuthorizingRealm {

    @Resource
    private u_userImpl u_userservice;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
//        ShiroToken token = (ShiroToken) authcToken;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nickname", token.getPrincipal());
        String a = new String((char[]) token.getCredentials());
        map.put("pswd", a);
        u_user user = null;
        // 从数据库获取对应用户名密码的用户
        List<u_user> userList = u_userservice.selectByMap(map);
        if(userList.size()!=0){
            user = userList.get(0);
        }
        if (null == user) {
            throw new AccountException("帐号或密码不正确！");
        }else if(user.getStatus()==0){
            /**
             * 如果用户的status为禁用。那么就抛出<code>DisabledAccountException</code>
             */
            throw new DisabledAccountException("帐号已经禁止登录！");
        }else{
            //更新登录时间 last login time
            user.setLastLoginTime(new Date());
            u_userservice.update(user);
        }
        return new SimpleAuthenticationInfo(user, user.getPswd(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        u_user token = (u_user) SecurityUtils.getSubject().getPrincipal();
        Long userId = token.getId();
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<String>();
        roleSet.add("100002");
        info.setRoles(roleSet);
        Set<String> permissionSet = new HashSet<String>();
        permissionSet.add("权限添加");
        info.setStringPermissions(permissionSet);
        return info;

    }
}
