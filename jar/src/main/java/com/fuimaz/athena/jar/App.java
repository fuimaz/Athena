package com.fuimaz.athena.jar;

import com.fuimaz.athena.jar.mixed.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) {

        List<Test> tests = Arrays.asList(new Test(1, 1), new Test(2, 2), new Test(3, 3));

        Map<Integer, Integer> map = tests.stream().collect(Collectors.toMap((Test test) -> test.getItem(), (Test test) -> test.getPrice()));
//        Map<Integer, Integer> map2 = tests.stream().collect(Collectors.mapping((Test test) -> test.getItem(), (Test test) -> test.getItem() ));

        System.out.println(map);
    }
}
