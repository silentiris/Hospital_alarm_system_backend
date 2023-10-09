package com.sipc.hospitalalarmsystem.model.dto.res.Alarm;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sipc.hospitalalarmsystem.model.po.Alarm.SqlGetAlarm;
import lombok.Data;
import org.apache.commons.collections4.Get;

import java.text.SimpleDateFormat;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-20 18:30
 */

@Data
public class GetAlarmRes
{

    public GetAlarmRes(SqlGetAlarm sqlGetAlarm){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
        this.setId(sqlGetAlarm.getId());
        this.setName(sqlGetAlarm.getName());
        this.setEventName(sqlGetAlarm.getCaseTypeName());
        this.setLevel(sqlGetAlarm.getWarningLevel());
        this.setDate(sdf.format(sqlGetAlarm.getCreateTime()));
        this.setDepartment(sqlGetAlarm.getArea());
        this.setDeal(sqlGetAlarm.getStatus() ? "已处理":"未处理");
        this.setContent(sqlGetAlarm.getProcessingContent());
        this.setVideo(sqlGetAlarm.getClipLink());
        this.setPhone(sqlGetAlarm.getPhone());
    }

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
    @ExcelProperty("电话")
    private String phone;

}
