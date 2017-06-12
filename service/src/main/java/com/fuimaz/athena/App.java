package com.fuimaz.athena;

import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lsit", integers);

        System.out.println(jsonObject);


        try {
            Map<String, Object> result = JSONObject.parseObject("{\"msg\":\"api_code_ok\",\"code\":1001,\"data\":{\"user_badge_list\":[{\"userId\":23131034,\"badge\":12}]}}");
            Integer code = (Integer) result.get("code");
            if(code == 1001){
                Map<String, Object> data = (Map<String, Object>) result.get("data");
                List<Map<String, Integer>> badgeList = (List<Map<String, Integer>>) data.get("user_badge_list");
                for(Map<String, Integer> list : badgeList) {
                     System.out.println(list.get("badge"));
                }
            }
        } catch(Exception e) {
        }

    }
}
