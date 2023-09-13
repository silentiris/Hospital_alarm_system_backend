package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import com.sipc.hospitalalarmsystem.model.po.Alarm.Alarm;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-13 17:53
 */
@Data
public class GetAlarmRes {

    private Integer id;
    private String clipLink;
    private Integer monitorId;
    private Integer caseType;
    private Integer warningLevel;
    private Timestamp time;
    private Boolean status;
    private String processingContent;
    private String area;

}
