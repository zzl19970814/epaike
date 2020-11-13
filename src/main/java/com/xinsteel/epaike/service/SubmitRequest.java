package com.xinsteel.epaike.service;

import java.io.UnsupportedEncodingException;


/**
 * @author zhouzx
 */
public class SubmitRequest {

    public static String urlEncoderText(String text) {
        String result = "";
        try {
            result = java.net.URLEncoder.encode(text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
