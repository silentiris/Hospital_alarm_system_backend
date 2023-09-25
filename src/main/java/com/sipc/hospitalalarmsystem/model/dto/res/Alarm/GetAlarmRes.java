package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-20 18:30
 */

@Data
public class GetAlarmRes
{
    private Integer id;
    private String name;
    private String eventName;
    private Integer level;
    private String date;
    private String department;
    private String deal;
    private String content;
    private String video;


}
