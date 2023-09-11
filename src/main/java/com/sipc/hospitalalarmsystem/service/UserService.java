package com.sipc.hospitalalarmsystem.service;

import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.res.user.BlankRes;
import com.sipc.hospitalalarmsystem.model.dto.res.user.LoginRes;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 17:27
 */
public interface UserService {

    String login(String username, String password);

    Boolean register(String username, String password, Integer role);
}
