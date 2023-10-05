package com.sipc.hospitalalarmsystem.model.dto.param.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 20:54
 */
@Data
public class LoginParam {
    @NotNull(message = "phone不能为空")
    public String phone;

    @NotNull(message = "password不能为空")
    public String password;
}
