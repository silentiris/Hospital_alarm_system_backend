package com.sipc.hospitalalarmsystem.model.po.Monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "monitor")
public class Monitor {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String area;
    private String leader;
    private Integer alarmCnt;
    private String streamLink;
    private Double latitude;
    private Double longitude;
    private Boolean dangerArea;
    private Boolean fall;
    private Boolean flame;
    private Boolean smoke;
    private Boolean wave;
    private Boolean punch;
    private Boolean running;
    private Integer leftX;
    private Integer leftY;
    private Integer rightX;
    private Integer rightY;
}
