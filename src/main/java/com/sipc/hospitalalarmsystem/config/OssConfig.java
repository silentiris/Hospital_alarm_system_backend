package com.sipc.hospitalalarmsystem.config;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OssConfig {
    @Value("${oss.id}")
    private  String secretId ;
    @Value("${oss.pass}")
    private  String secretKey ;
    public  COSClient cosClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // ClientConfig 中包含了后续请求 COS 的客户端设置：
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region("ap-beijing"));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        clientConfig.setSocketTimeout(30 * 1000);
        clientConfig.setConnectionTimeout(30 * 1000);
        return new COSClient(cred, clientConfig);
    }
}