package com.xinsteel.epaike;

import com.alibaba.fastjson.JSONObject;
import com.xinsteel.epaike.sercice.HttpClientService;
import com.xinsteel.epaike.utils.Base64EncodeUtils;
import com.xinsteel.epaike.utils.ConstantPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class EpaikeApplicationTests {

    @Test
    void contextLoads() throws Exception {
        String s = Base64EncodeUtils.imageToBase64("C:\\Users\\zhouzx\\Desktop\\images\\OIP.jpg");
        System.out.println(s);
    }

}
