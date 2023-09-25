package com.sipc.hospitalalarmsystem.config;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;

public class OssConfig {
    public static COSClient cosClient() {
        String secretId = "AKIDcI2rYIwPjrxxuxud7OWXP8j7VuDPOAl9";
        String secretKey = "MVVN8tgK9vWwQr57kIp9NadWDiz502wf";
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