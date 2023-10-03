package com.sipc.hospitalalarmsystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author CZCZCZ
 * &#064;date 2023-10-03 17:32
 */
public class Base64Util
{
    public static String convertToBase64(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
