package com.xinsteel.epaike;

import com.alibaba.fastjson.JSONObject;
import com.xinsteel.epaike.dao.OrderInfoMapper;
import com.xinsteel.epaike.pojo.OrderInfo;
import com.xinsteel.epaike.sercice.HttpClientService;
import com.xinsteel.epaike.utils.Base64EncodeUtils;
import com.xinsteel.epaike.utils.ConstantPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class EpaikeApplicationTests {


    @Test
    void contextLoads() throws Exception {
        List<Map<String, String>> infoList = new ArrayList<>();
        Map<String, String> map = new HashMap();
        map.put("eventTime","2018-11-15 00:00:00");
        map.put("eventPlace","祁集镇煤化工大道 中安联合煤化有限公司");
        infoList.add(map);

        Map<String, String> map2 = new HashMap();
        map2.put("eventTime","2019-06-14 00:00:00");
        map2.put("eventPlace","喀什中石化油库：新疆喀什市世纪大道30号（中石化油库）");
        infoList.add(map2);

        for (int i = 0; i < 2; i++) {
            System.out.println(infoList.get(i).get("eventPlace"));
        }
    }

}
