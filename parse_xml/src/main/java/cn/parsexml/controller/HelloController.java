package cn.parsexml.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("Hello")
public class HelloController {


    @GetMapping("sayHello")
    public String sayHello(){
        return "hello";
    }
}
