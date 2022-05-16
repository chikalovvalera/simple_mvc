package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    private Logger logger = Logger.getLogger(ErrorController.class);

    @GetMapping("/404")
    public String notFoundError() {
        System.out.println(404);
        logger.info("404...");
        return "error/404";
    }


}