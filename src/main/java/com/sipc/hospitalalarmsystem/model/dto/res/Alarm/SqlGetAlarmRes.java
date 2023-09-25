package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 17:53
 */
@Data
public class SqlGetAlarmRes {

    private Integer id;
    private String clipLink;
    private String name;
    private String caseType;
    private Integer warningLevel;
    private Timestamp createTime;
    private Boolean status;
    private String processingContent;
    private String area;

}
