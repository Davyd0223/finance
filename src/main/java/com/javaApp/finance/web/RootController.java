package com.javaApp.finance.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/app")
    public String app() {
        return "app";
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }

    @GetMapping("/confirm-delete")
    public String confirmDelete() {
        return "confirm-delete";
    }
}
