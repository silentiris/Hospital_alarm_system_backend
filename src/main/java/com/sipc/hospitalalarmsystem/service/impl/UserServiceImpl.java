package com.sipc.hospitalalarmsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sipc.hospitalalarmsystem.dao.UserDao;
import com.sipc.hospitalalarmsystem.model.po.User.User;
import com.sipc.hospitalalarmsystem.service.UserService;
import com.sipc.hospitalalarmsystem.util.JwtUtils;
import com.sipc.hospitalalarmsystem.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 17:27
 */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Override
    public String login(String username, String password) {
        password = MD5Util.MD5Encode(password);
        User user = getOne(new QueryWrapper<User>().eq("user_name", username).eq("password", password));;
        if (user != null) {
            return JwtUtils.signUser(user);
        }
        return null;
    }

    @Override
    public Boolean register(String username, String password, Integer role) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(MD5Util.MD5Encode(password));
        user.setRole(role);
        try{
            save(user);
            return true;
        }catch (Exception e){
            log.error("注册失败");
            return false;
        }

    }

    @Override
    public Boolean updatePassword(Integer id, String oldPassword, String newPassword){
        User user = getOne(new QueryWrapper<User>().eq("id", id).eq("password", MD5Util.MD5Encode(oldPassword)));
        if (user == null){
            return false;
        }
        user.setPassword(MD5Util.MD5Encode(newPassword));
        try{
            updateById(user);
            return true;
        }catch (Exception e){
            log.error("修改密码失败");
            return false;
        }
    }

}
