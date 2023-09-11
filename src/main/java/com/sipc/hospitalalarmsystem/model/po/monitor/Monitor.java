package com.sipc.hospitalalarmsystem.model.po.monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName()
public class Monitor {
    @TableId(type = IdType.AUTO)
    private int id;
    private String name;
    private String area;
    private double latitude;
    private double longitude;
    private int dangerArea;
    private int fall;
    private int stayedTooLong;
    private int flame;
    private int smoke;
}
