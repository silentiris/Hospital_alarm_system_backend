package com.sipc.hospitalalarmsystem.model.po.Alarm;

import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-21 14:55
 */
@Data
public class AlarmCaseTypeTotal
{
    private String caseTypeName;

    private Integer todayNew;

    private Integer total;


}
