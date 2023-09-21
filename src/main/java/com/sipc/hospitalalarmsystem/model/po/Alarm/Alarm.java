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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT,jdbcType = TIMESTAMP)
    private Timestamp createTime;
    private Boolean status;
    private String processingContent;

}
