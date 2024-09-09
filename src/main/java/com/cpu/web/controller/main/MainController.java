package com.cpu.web.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
        System.out.println("pull request 테스트");
        return "main";
    }

}