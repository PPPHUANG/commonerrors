package com.ppphuang.demo.controller;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class MdController {

    @GetMapping(value = "md51")
    public String md51Controller(String pass) {
        String s = DigestUtils.md5DigestAsHex(pass.getBytes());
        return s;
    }

    @GetMapping(value = "md52")
    public String md52Controller(String pass) {
        String s = DigestUtils.md5DigestAsHex(DigestUtils.md5DigestAsHex(pass.getBytes()).getBytes());
        return s;
    }
    @GetMapping(value = "md5salt")
    public String md5SaltController(String pass) {
        String s = DigestUtils.md5DigestAsHex(("salt" + pass).getBytes());
        return s;
    }
}
