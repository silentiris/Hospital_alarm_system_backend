package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import com.sipc.hospitalalarmsystem.model.po.Alarm.AlarmCaseTypeTotal;
import com.sipc.hospitalalarmsystem.model.po.Alarm.AlarmTotal;
import lombok.Data;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-21 15:07
 */

@Data
public class RealTimeAlarmRes
{
    private AlarmTotal alarmTotal;

    private List<AlarmCaseTypeTotal> alarmCaseTypeTotalList;
}
