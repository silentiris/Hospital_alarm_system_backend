package com.sipc.hospitalalarmsystem.model.dto.param.user;

import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 1:30
 */

@Data
public class UpdatePasswordParam {
    private String oldPassword;
    private String newPassword;
}
