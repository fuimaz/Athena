package com.fuimaz.athena.jar.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Fuimaz on 2017/3/27.
 */
@Controller
public class web {

    @RequestMapping()
    @ResponseBody
    public String index() {
        return "hello world";
    }
}
