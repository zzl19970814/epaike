package com.xinsteel.epaike.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zhouzx
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    @Value("${yipaike.client_id}")
    private String clientId;

    @Value("${yipaike.client_secret}")
    private String clientSecret;

    @Value("${yipaike.grant_type}")
    private String grantType;

    @Value("${yipaike.companyid}")
    private String companyId;

    @Value("${yipaike.username}")
    private String username;

    @Value("${yipaike.password}")
    private String password;

    @Value("${yipaike.url}")
    private String url;

    public static String CLIENT_ID;
    public static String CLIENT_SECRET;
    public static String GRANT_TYPE;
    public static String COMPANY_ID;
    public static String USERNAME;
    public static String PASSWORD;
    public static String URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        CLIENT_ID = clientId;
        CLIENT_SECRET = clientSecret;
        GRANT_TYPE = grantType;
        COMPANY_ID = companyId;
        USERNAME = username;
        PASSWORD = password;
        URL = url;
    }
}
