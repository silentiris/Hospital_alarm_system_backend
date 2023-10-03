package com.sipc.hospitalalarmsystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 15:05
 */
public interface RequestFlaskService
{
    Boolean updateMonitorArea(String ip, List<Integer> area) throws Exception;

    Boolean updateMonitorAbility(String ip,List<Boolean> ability) throws Exception;

    String getMonitorImg(String ip) throws Exception;
}
