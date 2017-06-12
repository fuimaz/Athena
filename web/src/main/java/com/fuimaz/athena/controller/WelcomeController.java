package com.fuimaz.athena.controller;

import com.fuimaz.athena.configure.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by juchen on 17/3/13.
 */
@Controller
public class WelcomeController {

    @Autowired
    Hello hello2;

    @Autowired
    Hello hello1;

    @RequestMapping("/")
    public @ResponseBody String index() {
        return String.valueOf(hello1.getA()) + String.valueOf(hello2.getA());
    }


    @RequestMapping("/file")
    public @ResponseBody String file(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String app = request.getParameter("app");
        return file.getOriginalFilename() + " " + app;
    }
}
