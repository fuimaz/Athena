package com.fuimaz.athena;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Map<String, String> map = new HashMap<>();
        map.put("99", "ddddd");
        map.put("98", "bbbbb");
        map.put("96", "aaaaa");
        map.put("97", "ccccc");


        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>() {
                    @Override
                    public int compare(String str1, String str2) {
                        return str2.compareTo(str1);
                    }
                });

        sortMap.putAll(map);


        for (String key : sortMap.keySet()) {
            System.out.println(key);
        }
    }
}
