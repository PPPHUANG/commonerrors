package com.ppphuang.demo.controller;

import com.ppphuang.demo.dao.mappers.test.UserPOMapper;
import com.ppphuang.demo.dao.model.test.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MybatisController {

    UserPOMapper userPOMapper;

    @Autowired
    public void setUserPOMapper(UserPOMapper userPOMapper) {
        this.userPOMapper = userPOMapper;
    }

    @GetMapping(value = "user/get")
    public Object getUser() {
        UserPO userPO = userPOMapper.selectByPrimaryKey(1);
        return userPO;
    }
}
