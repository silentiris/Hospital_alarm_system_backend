package com.sipc.hospitalalarmsystem.model.po.Alarm;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

import static org.apache.ibatis.type.JdbcType.TIMESTAMP;

@TableName(value = "alarm_info")
@Data
public class Alarm {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String clipLink;
    private Integer monitorId;
    private Integer caseType;
    private Integer warningLevel;
    @TableField(fill = FieldFill.INSERT,jdbcType = TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private Boolean status;
    private String processingContent;

}
