package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-20 18:30
 */

@Data
public class GetAlarmRes
{

    private Integer id;
    @ExcelProperty("事件")
    private String name;
    @ExcelProperty("类别")
    private String eventName;
    @ExcelProperty("等级")
    private Integer level;
    @ExcelProperty("时间")
    private String date;
    @ExcelProperty("地点")
    private String department;
    @ExcelProperty("是否处理")
    private String deal;
    @ExcelProperty("处理结果")
    private String content;
    @ExcelProperty("视频")
    private String video;


}
