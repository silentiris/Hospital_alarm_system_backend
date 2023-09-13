package com.sipc.hospitalalarmsystem.model.dto.param.alarm;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 17:42
 */

@Data
public class UpdateAlarmParam {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "status不能为空")
    private Boolean status;


    @NotNull(message = "processingContent不能为空")
    private String processingContent;

}
