package com.sipc.hospitalalarmsystem.controller;

import com.sipc.hospitalalarmsystem.aop.Pass;
import com.sipc.hospitalalarmsystem.model.dto.CommonResult;
import com.sipc.hospitalalarmsystem.model.dto.param.user.LoginParam;
import com.sipc.hospitalalarmsystem.model.dto.param.user.RegisterParam;
import com.sipc.hospitalalarmsystem.model.dto.param.user.UpdatePasswordParam;
import com.sipc.hospitalalarmsystem.model.dto.res.BlankRes;
import com.sipc.hospitalalarmsystem.model.dto.res.User.LoginRes;
import com.sipc.hospitalalarmsystem.model.po.User.User;
import com.sipc.hospitalalarmsystem.service.UserService;
import com.sipc.hospitalalarmsystem.service.impl.UserServiceImpl;
import com.sipc.hospitalalarmsystem.util.JwtUtils;
import com.sipc.hospitalalarmsystem.util.TokenThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 17:26
 */
@Validated
@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;

    @Pass
    @PostMapping("/login")
    public CommonResult<LoginRes> login(@Valid @RequestBody LoginParam loginParam) {
        String token = userService.login(loginParam.getPhone(), loginParam.getPassword());
        if (token == null) {
            return CommonResult.fail("用户名或密码错误");
        }
        User user = JwtUtils.getUserByToken(token);
        user = userService.getById(user.getId());
        LoginRes loginRes = new LoginRes();
        loginRes.setName(user.getUserName());
        loginRes.setPhone(user.getPhone());

        loginRes.setRole(user.getRole());
        loginRes.setToken(token);
        loginRes.setId(user.getId());
        return CommonResult.success(loginRes);
    }


    @Pass
    @PostMapping("/register")
    public CommonResult<BlankRes> register(@Valid @RequestBody RegisterParam registerParam) {
        if (userService.register(registerParam.getUsername(), registerParam.getPassword(), registerParam.getRole())) {
            return CommonResult.success("注册成功");
        } else {
            return CommonResult.fail("注册失败");
        }
    }

    @PostMapping("/update/password")
    public CommonResult<BlankRes> updatePassword(@Valid @RequestBody UpdatePasswordParam updatePasswordParam) {
        User user = JwtUtils.getUserByToken(TokenThreadLocalUtil.getInstance().getToken());
        if (user == null) {
            return CommonResult.fail("token错误");
        }
        if (userService.updatePassword(user.getId(), updatePasswordParam.getOldPassword(), updatePasswordParam.getNewPassword())) {
            return CommonResult.success("修改成功");
        }

        return CommonResult.fail("密码错误");
    }

    @PostMapping("/update/name/{name}")
    public CommonResult<BlankRes> updateName(@PathVariable @NotNull String name) {
        User user = JwtUtils.getUserByToken(TokenThreadLocalUtil.getInstance().getToken());
        if (userService.updateName(user.getId(), name)) {
            return CommonResult.success("修改成功");
        }

        return CommonResult.fail("修改失败");
    }


}
