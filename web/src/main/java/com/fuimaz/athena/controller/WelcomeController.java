package com.fuimaz.athena.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by juchen on 17/3/13.
 */
@Controller
public class WelcomeController {

    @RequestMapping("/")
    public @ResponseBody String index() {
        return "SUCCESS";
    }
}
