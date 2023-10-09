package com.sipc.hospitalalarmsystem.model.po.Alarm;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 17:53
 */
@Data
public class SqlGetAlarm {

    private Integer id;
    private String clipLink;
    private String name;
    private String caseTypeName;
    private Integer warningLevel;
    private Timestamp createTime;
    private Boolean status;
    private String processingContent;
    private String area;
    private String phone;

}
