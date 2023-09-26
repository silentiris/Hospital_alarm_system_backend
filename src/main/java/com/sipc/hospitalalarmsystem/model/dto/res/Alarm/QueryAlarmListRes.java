package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import lombok.Data;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-11 21:47
 */

@Data
public class QueryAlarmListRes {
    private Integer count;

    private List<GetAlarmRes> alarmList;
}
