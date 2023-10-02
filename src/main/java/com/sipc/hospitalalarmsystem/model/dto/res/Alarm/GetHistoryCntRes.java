package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import com.sipc.hospitalalarmsystem.model.po.Alarm.TimePeriod;
import lombok.Data;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-15 17:27
 */

@Data
public class GetHistoryCntRes
{
    List<TimePeriod> graph1;

    List<TimePeriod> graph2;

    List<TimePeriod> graph3;

}
