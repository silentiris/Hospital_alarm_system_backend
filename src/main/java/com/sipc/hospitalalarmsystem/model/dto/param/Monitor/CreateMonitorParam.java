package com.sipc.hospitalalarmsystem.model.dto.param.Monitor;

import lombok.Data;

import java.util.PrimitiveIterator;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 4:24
 */

@Data
public class CreateMonitorParam
{
    private String name;
    private String area;
    private String leader;
    private String ip;
    private Double longitude;
    private Double latitude;
    private Integer leftX;
    private Integer leftY;
    private Integer rightX;
    private Integer rightY;
}
