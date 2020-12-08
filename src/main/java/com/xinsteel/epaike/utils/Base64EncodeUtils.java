package com.xinsteel.epaike.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.junit.platform.commons.util.StringUtils;


public class Base64EncodeUtils {
    /**
     * 本地图片转base64编码
     * @param filePath 文件图片所在路径
     * @return base64编码
     */
    public static String imageToBase64(String filePath) throws Exception{
        if(StringUtils.isBlank(filePath)){
            return null;
        }
        String encode="";
        try{
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            encode = Base64.encode(bytes);
        }catch (Exception e){
            throw e;
        }
        return encode;
    }
}
