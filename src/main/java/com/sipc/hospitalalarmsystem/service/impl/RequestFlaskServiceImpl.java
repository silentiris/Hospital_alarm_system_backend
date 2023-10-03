package com.sipc.hospitalalarmsystem.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.hospitalalarmsystem.model.dto.res.FlaskResponse.updateMonitorAreaRes;
import com.sipc.hospitalalarmsystem.service.RequestFlaskService;
import com.sipc.hospitalalarmsystem.util.Base64Util;
import com.sipc.hospitalalarmsystem.util.HttpUtils;
import com.sipc.hospitalalarmsystem.util.JacksonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 15:08
 */
@Service
public class RequestFlaskServiceImpl implements RequestFlaskService {

    private final String FLASK_TOKEN = "sipc115";



    @Override
    public Boolean updateMonitorArea(String ip, List<Integer> area) throws Exception{
        if (area == null || area.isEmpty()) {
            return false;
        }
        Map<String,Object> mp = new HashMap<>();
        mp.put("areaList",area);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(mp);
        String response = HttpUtils.postJson("http://"+ip+"/api/v1/monitor-device/area",json,FLASK_TOKEN);
        updateMonitorAreaRes res = JacksonUtils.json2pojo(response, updateMonitorAreaRes.class);
        return res.getMsg().equals("success");
    }

    @Override
    public Boolean updateMonitorAbility(String ip,List<Boolean> ability) throws Exception{
        if (ability == null || ability.isEmpty()) {
            return false;
        }
        Map<String,Object> mp = new HashMap<>();
        mp.put("typeList",ability);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(mp);
        String response = HttpUtils.postJson("http://"+ip+"/api/v1/monitor-device/type",json,FLASK_TOKEN);
        updateMonitorAreaRes res = JacksonUtils.json2pojo(response, updateMonitorAreaRes.class);
        return res.getMsg().equals("success");
    }

    @Override
    public String getMonitorImg(String ip) throws Exception{
        InputStream inputStream = HttpUtils.GetInputStream("http://"+ip+"/api/v1/monitor-device/img",FLASK_TOKEN);
        if (inputStream == null) {
            return null;
        }
        return Base64Util.convertToBase64(inputStream);
    }
}
