package com.sipc.hospitalalarmsystem.model.dto.param.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 20:56
 */
@Data
public class RegisterParam {
    @NotNull(message = "username不能为空")
    private String username;

    @NotNull(message = "password不能为空")
    private String password;

    @NotNull(message = "role不能为空")
    private Integer role;
}
