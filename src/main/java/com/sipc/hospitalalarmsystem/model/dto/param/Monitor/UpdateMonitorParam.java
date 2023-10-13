package com.sipc.hospitalalarmsystem.model.dto.param.Monitor;

import lombok.Data;

import java.security.BasicPermission;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 14:37
 */

@Data
public class UpdateMonitorParam {
    //TODO 改字段要改这里
    private Integer id;
    private String name;
    private String area;
    private String leader;
    private Double longitude;
    private Double latitude;
    private Boolean fall;
    private Boolean flame;
    private Boolean smoke;
    private Boolean wave;
    private Boolean punch;
    private Integer leftX;
    private Integer leftY;
    private Integer rightX;
    private Integer rightY;
}
