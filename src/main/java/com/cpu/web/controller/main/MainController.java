package com.cpu.web.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
        System.out.println("Hello Main Page!!!!!!!");
        return "main";
    }

}
