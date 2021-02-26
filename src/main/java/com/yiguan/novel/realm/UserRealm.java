package com.yiguan.novel.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiguan.novel.bean.User;
import com.yiguan.novel.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    /**
     * 执行授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


    @Autowired
    UserService userService;
    /**
     * 执行认证的逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username",token.getUsername());
        User user = userService.getOne(queryWrapper);

        //判断用户名
        if(user == null){
            return  null;//shiro底层会抛出UnknownError异常
        }
        //密码错误
        return new SimpleAuthenticationInfo("",user.getPassword(),"");//如果为null会抛出密码错误的异常
    }
}
