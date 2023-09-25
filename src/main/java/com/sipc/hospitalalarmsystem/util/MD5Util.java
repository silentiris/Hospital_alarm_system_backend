package com.sipc.hospitalalarmsystem.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author CZCZCZ
 * &#064;date 2023-09-10 17:36
 */
public class MD5Util {

    public static final String salt = "HuaWuWin!";

    public static String MD5Encode(String password) {
        String md5 = null;
        md5 = DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
        return md5;
    }


}
